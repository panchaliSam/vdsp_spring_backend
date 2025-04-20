package com.app.vdsp.entity;

import com.app.vdsp.type.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name =" users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First name should not be blank")
    private String firstName;
    @NotBlank(message = "Last name should not be blank")
    private String lastName;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Invalid email pattern"
    )
    private String email;
    @NotBlank(message = "Password should not be blank")
    @Size(min = 60)
    private String password;
    @NotBlank(message = "Phone number should not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}
