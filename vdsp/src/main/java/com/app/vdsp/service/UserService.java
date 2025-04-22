package com.app.vdsp.service;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto registerUser(UserDto userDto);
    TokenResponseDto loginUser(String email, String password);
    UserDetailsService userDetailsService();
    List<User> getAllUsers();
    User getUserById(Long id);
    void updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
