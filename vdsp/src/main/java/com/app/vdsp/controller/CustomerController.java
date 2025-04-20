package com.app.vdsp.controller;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.entity.Customer;
import com.app.vdsp.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.createCustomer(
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getEmail(),
                customerDto.getPassword(),
                customerDto.getPhoneNumber(),
                customerDto.getRole()
        );
        return ResponseEntity.ok("Customer registered successfully");
    }
}
