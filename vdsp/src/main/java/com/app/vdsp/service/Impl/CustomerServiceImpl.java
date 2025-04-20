package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.CustomerDto;
import com.app.vdsp.dto.TokenResponseDto;
import com.app.vdsp.entity.Customer;
import com.app.vdsp.repository.CustomerRepository;
import com.app.vdsp.service.CustomerService;
import com.app.vdsp.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        try {
            if (customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
            }
            Customer customer = modelMapper.map(customerDto, Customer.class);
            Customer savedCustomer = customerRepository.save(customer);

            return modelMapper.map(savedCustomer, CustomerDto.class);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("CustomerServiceImpl | createCustomer | {}", e.getMessage());
            throw new RuntimeException("Error creating customer", e);
        }
    }

    @Override
    public TokenResponseDto login(String email, String password) {
        try {
            Optional<Customer> customerOptional = customerRepository.findCustomerByEmailAndPassword(email, password);
            if (customerOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }
            Customer customer = customerOptional.get();
            String token = jwtTokenProvider.generateRefreshToken(customer);
            return TokenResponseDto.builder()
                    .accessToken(token)
                    .build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("CustomerServiceImpl | login | {}", e.getMessage());
            throw new RuntimeException("Error during login", e);
        }
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));

        modelMapper.map(customerDto, existingCustomer);
        customerRepository.save(existingCustomer);
    }

    @Override
    public void createCustomer(String firstName, String lastName, String email, String password, String phoneNumber, String role) {

    }
}
