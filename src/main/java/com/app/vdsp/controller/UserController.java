package com.app.vdsp.controller;

import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.dto.UserDto;
import com.app.vdsp.dto.UserUpdateDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.User;
import com.app.vdsp.service.UserService;
import com.app.vdsp.type.RoleType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody @Validated UserDto user) {
        ApiResponse<UserDto> response = userService.registerUser(user);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@RequestBody JsonNode user) {
        ApiResponse<TokenResponseDto> response = userService.loginUser(user.get("email").asText(), user.get("password").asText());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'STAFF')")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if (isAdminOrOwner(currentUser, id)) {
            return ResponseEntity.ok(userService.getUserById(id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Not authorized", null));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'STAFF')")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated UserDto updatedUser,
            @AuthenticationPrincipal User currentUser) {
        if (isAdminOrOwner(currentUser, id)) {
            return ResponseEntity.ok(userService.updateUser(id, updatedUser));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Not authorized", null));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody JsonNode request) {
        String refreshToken = request.get("refresh_token").asText();
        return ResponseEntity.ok(userService.logoutUser(refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody JsonNode request) {
        String refreshToken = request.get("refresh_token").asText();
        return ResponseEntity.ok(userService.refreshAccessToken(refreshToken));
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<ApiResponse<Long>> patchMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid UserUpdateDto updates) {

        ApiResponse<Long> resp = userService.patchOwnProfile(authHeader, updates);
        return ResponseEntity.ok(resp);
    }

    private boolean isAdminOrOwner(User currentUser, Long targetUserId) {
        return currentUser.getRole() == RoleType.ROLE_ADMIN || currentUser.getId().equals(targetUserId);
    }
}