package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.User;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.UserService;
import com.app.vdsp.utils.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        log.info("Registering user with email: {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Registration failed: Email {} is already in use", userDto.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        try {
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);

            log.info("User registered successfully with email: {}", userDto.getEmail());
            return userDto;
        } catch (Exception e) {
            log.error("Error occurred during registration for email {}: {}", userDto.getEmail(), e.toString());
            throw new RuntimeException("User registration failed", e);
        }
    }

    @Override
    public TokenResponseDto loginUser(String email, String password) {
        log.info("Attempting to log in user with email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("Login successful for email: {}", email);
                return TokenResponseDto.builder()
                        .accessToken(jwtService.generateToken(user))
                        .refreshToken(jwtService.generateRefreshToken(user))
                        .userDetails(user)
                        .build();
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
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("UserServiceImpl | getAllUsers | Exception: {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(Long id) {
        try{
            Optional<User> user = userRepository.findById(id);
            if(user.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            return user.get();
        } catch (Exception e) {
            if(e instanceof ResponseStatusException){
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | getUserById | Exception: {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(Long id, UserDto updatedUser) {
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

        } catch (Exception e) {
            if(e instanceof ResponseStatusException){
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | updateUser | Exception: {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        try{
            Optional<User> existUser = userRepository.findById(id);
            if(existUser.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            userRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | deleteUser | {}", e.toString());
            throw new RuntimeException(e);
        }
    }
}
