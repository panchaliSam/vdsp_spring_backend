package com.app.vdsp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    @NotBlank(message = "First name should not be blank")
    private String firstName;

    @NotBlank(message = "Last name should not be blank")
    private String lastName;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
}