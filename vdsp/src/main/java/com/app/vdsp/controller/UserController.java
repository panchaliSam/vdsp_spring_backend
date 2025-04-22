package com.app.vdsp.controller;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.entity.User;
import com.app.vdsp.service.UserService;
import com.app.vdsp.type.Role;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.of(Optional.of(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'STAFF')")
    public ResponseEntity<User> getUserById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if (isAdminOrOwner(currentUser, id)) {
            return ResponseEntity.of(Optional.of(userService.getUserById(id)));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'STAFF')")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated UserDto updatedUser,
            @AuthenticationPrincipal User currentUser) {
        if (isAdminOrOwner(currentUser, id)) {
            userService.updateUser(id, updatedUser);
            return ResponseEntity.ok("User updated successfully.");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this user.");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    private boolean isAdminOrOwner(User currentUser, Long targetUserId) {
        return currentUser.getRole() == Role.ROLE_ADMIN || currentUser.getId().equals(targetUserId);
    }
}
