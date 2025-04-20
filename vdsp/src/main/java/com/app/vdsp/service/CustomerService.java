package com.app.vdsp.service;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    TokenResponseDto login(String email, String password);
    UserDetailsService userDetailsService();
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);
    void updateCustomer(Long id, CustomerDto customerDto);

    void createCustomer(@NotBlank(message = "First name should not be blank") String firstName, @NotBlank(message = "Last name should not be blank") String lastName, @Email @NotBlank(message = "Email should not be blank") String email, @NotBlank(message = "Password should not be blank") @Size(min = 60) String password, @NotBlank(message = "Phone number should not be blank") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") String phoneNumber, @NotBlank String role);
}
