package com.app.vdsp.service;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ApiResponse<UserDto> registerUser(UserDto userDto);
    ApiResponse<TokenResponseDto> loginUser(String email, String password);
    UserDetailsService userDetailsService();
    ApiResponse<List<User>> getAllUsers();
    ApiResponse<User> getUserById(Long id);
    ApiResponse<String> updateUser(Long id, UserDto userDto);
    ApiResponse<String> deleteUser(Long id);
    ApiResponse<String> logoutUser(String refreshToken);
    ApiResponse<String> refreshAccessToken(String refreshToken);
}