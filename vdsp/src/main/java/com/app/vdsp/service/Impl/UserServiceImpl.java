package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.User;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        try {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            User user = modelMapper.map(userDto, User.class);
            userRepository.save(user);

            return userDto;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserServiceImpl | registerUser | Error: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred during user registration", e);
        }
    }

    @Override
    public TokenResponseDto loginUser(UserDto userDto) {
        throw new UnsupportedOperationException("loginUser method is not implemented yet.");
    }

    @Override
    public UserDetailsService userDetailsService() {
        throw new UnsupportedOperationException("userDetailsService method is not implemented yet.");
    }
}
