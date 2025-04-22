package com.app.vdsp.controller;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.User;
import com.app.vdsp.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated UserDto user) {
        return ResponseEntity.of(Optional.of(userService.registerUser(user)));
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody JsonNode user) {
        return userService.loginUser(user.get("email").asText(), user.get("password").asText());
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.of(Optional.of(userService.getAllUsers()));
    }
}
