package com.app.vdsp.service;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.entity.Customer;
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
}
