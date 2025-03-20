package com.app.vdsp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @NotBlank(message = "First name should not be blank")
    private String firstName;
    @NotBlank(message = "Last name should not be blank")
    private String lastName;
    @Email
    @NotBlank(message = "Email should not be blank")
    private String email;
    @NotBlank(message = "Password should not be blank")
    @Size(min = 60)
    private String password;
    @NotBlank(message = "Phone number should not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotBlank
    private String role = "CUSTOMER";
}
