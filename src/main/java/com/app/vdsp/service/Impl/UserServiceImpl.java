package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.dto.UserUpdateDto;
import com.app.vdsp.entity.*;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.*;
import com.app.vdsp.service.UserService;
import com.app.vdsp.type.RoleType;
import com.app.vdsp.type.StaffAssignStatus;
import com.app.vdsp.utils.JWTService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final StaffRoleRepository staffRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthorizationHelper authorizationHelper;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final JavaMailSender javaMailSender; // assuming mail is configured

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, JWTService jwtService, PasswordEncoder passwordEncoder, StaffRepository staffRepository, StaffRoleRepository staffRoleRepository, RefreshTokenRepository refreshTokenRepository, AuthorizationHelper authorizationHelper, ResetPasswordTokenRepository resetPasswordTokenRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
        this.staffRoleRepository = staffRoleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authorizationHelper = authorizationHelper;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ApiResponse<UserDto> registerUser(UserDto userDto) {
        log.info("Registering user with email: {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Registration failed: Email {} is already in use", userDto.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        try {
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User savedUser = userRepository.save(user);

            if (user.getRole() == RoleType.ROLE_STAFF) {
                // Save staff entity
                Staff staff = Staff.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .userId(savedUser.getId())
                        .build();
                Staff savedStaff = staffRepository.save(staff);

                StaffRole staffRole = StaffRole.builder()
                        .staff(savedStaff)
                        .assignStatus(StaffAssignStatus.NOT_ASSIGNED)
                        .role(null)
                        .assignedAt(null)
                        .build();
                staffRoleRepository.save(staffRole);
            }

            log.info("User registered successfully with email: {}", userDto.getEmail());
            return new ApiResponse<>(true, "User registered successfully", userDto);
        } catch (Exception e) {
            log.error("Error occurred during registration for email {}: {}", userDto.getEmail(), e.toString());
            throw new RuntimeException("User registration failed", e);
        }
    }

    @Override
    public ApiResponse<TokenResponseDto> loginUser(String email, String password) {
        log.info("Attempting to log in user with email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("Login successful for email: {}", email);

                // Generate tokens
                String accessToken = jwtService.generateToken(user);
                String refreshTokenValue = jwtService.generateRefreshToken(user);

                // Create and save refresh token entity
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setToken(refreshTokenValue);
                refreshToken.setUser(user);
                refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
                refreshToken.setRevoked(false);

               // refreshTokenRepository.revokeAllActiveTokensForUser(user.getId());
                refreshTokenRepository.save(refreshToken);

                TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenValue)
                        .userDetails(user)
                        .build();

                return new ApiResponse<>(true, "Login successful", tokenResponseDto);
            } else {
                log.warn("Login failed for email {}: Invalid password", email);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
            }
        }

        log.warn("Login failed: User with email {} not found", email);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }



    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Override
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse<>(true, "Fetched all users", users);
    }

    @Override
    public ApiResponse<User> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new ApiResponse<>(true, "User fetched successfully", user);
    }

    @Override
    public ApiResponse<String> updateUser(Long id, UserDto updatedUser)  {
        try{
            Optional<User> existUser = userRepository.findById(id);
            if(existUser.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            User user = existUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setRole(updatedUser.getRole());

            userRepository.save(user);
            return new ApiResponse<>(true, "User updated successfully", null);

        } catch (Exception e) {
            if(e instanceof ResponseStatusException){
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | updateUser | Exception: {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<String> deleteUser(Long id) {
        try{
            Optional<User> existUser = userRepository.findById(id);
            if(existUser.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            userRepository.deleteById(id);
            return new ApiResponse<>(true, "User deleted successfully", null);
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | deleteUser | {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<String> logoutUser(String refreshToken){
        log.info("Logging out user with refresh token: {}", refreshToken);
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);

        if (tokenOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Refresh token not found");
        }

        RefreshToken token = tokenOptional.get();
        token.setRevoked(true);
        refreshTokenRepository.save(token);
        log.info("Refresh token revoked successfully.");
        return new ApiResponse<>(true, "Logged out successfully", null);
    }

    @Override
    public ApiResponse<String> refreshAccessToken(String refreshToken) {
        log.info("Refreshing access token with refresh token: {}", refreshToken);
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);

        if (tokenOptional.isEmpty() || tokenOptional.get().isRevoked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or revoked refresh token");
        }

        RefreshToken token = tokenOptional.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        Long userId = token.getUser().getId();
        log.info("User ID from refresh token: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String accessToken = jwtService.generateToken(user);
        return new ApiResponse<>(true, "Token refreshed", accessToken);
    }

    @Override
    public ApiResponse<Long> patchOwnProfile(String authHeader,
                                             @Valid UserUpdateDto updates) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = authorizationHelper.extractUserId(authHeader);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        user.setFirstName(updates.getFirstName());
        user.setLastName(updates.getLastName());
        user.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getPassword() != null && !updates.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updates.getPassword()));
        }

        userRepository.save(user);
        log.info("User {} updated their profile", userId);

        // return the userId in the data field
        return new ApiResponse<>(true, "Profile updated successfully", userId);
    }

    @Override
    @Transactional
    public ApiResponse<String> sendResetPasswordEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not registered");
        }

        User user = userOptional.get();

        resetPasswordTokenRepository.deleteByUserId(user.getId());
        resetPasswordTokenRepository.flush();

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        ResetPasswordToken resetToken = ResetPasswordToken.builder()
                .user(user)
                .token(token)
                .expiryDate(expiry)
                .used(false)
                .build();

        resetPasswordTokenRepository.save(resetToken);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("Click here to reset your password: " + resetLink);
        javaMailSender.send(message);

        return new ApiResponse<>(true, "Reset link sent to your email", null);
    }

    @Override
    public ApiResponse<String> resetPassword(String token, String newPassword) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token"));

        if (resetToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetPasswordTokenRepository.save(resetToken);

        return new ApiResponse<>(true, "Password reset successfully", null);
    }
}
