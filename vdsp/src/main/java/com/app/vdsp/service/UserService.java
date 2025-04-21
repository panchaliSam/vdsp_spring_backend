package com.app.vdsp.service;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto registerUser(UserDto userDto);
    TokenResponseDto loginUser(String email, String password);
    UserDetailsService userDetailsService();
}
