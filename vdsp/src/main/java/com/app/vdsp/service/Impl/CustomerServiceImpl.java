package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.entity.Customer;
import com.app.vdsp.repository.CustomerRepository;
import com.app.vdsp.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        try{
            if(customerRepository.findByEmail(customerDto.getEmail()).isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
            }
            Customer map = modelMapper.map(customerDto, Customer.class);
            customerRepository.save(map);

            return customerDto;

        }catch (Exception e){
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | registerUser | {}", e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenResponseDto login(String email, String password) {
        try{
            Optional<Customer> customer = customerRepository.findCustomerByEmailAndPassword(email, password);
            if(customer.isPresent()){
                return null;
            }
        }catch (Exception e){
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            }
            log.error("UserServiceImpl | userLogin | {}", e.toString());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return List.of();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {

    }

    @Override
    public void updateCustomer(Long id, CustomerDto customerDto) {

    }
}
