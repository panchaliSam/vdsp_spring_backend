package com.app.vdsp.service;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    TokenResponseDto login(String email, String password);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);
    void updateCustomer(Long id, CustomerDto customerDto);
}
