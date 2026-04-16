package com.amit.service;


import com.amit.entity.Customer;
import com.amit.exception.EKartException;
import com.amit.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private CustomerServiceImpl customerService;
 //Test cases here
    @Test
    public void authenticateCustomerValidCase() throws EKartException {
        Customer customer  = new Customer();
        customer.setEmailId("test23@gmail.com");

        when(customerRepository.findById(customer.getEmailId())).thenReturn(Optional.of(customer));

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(jwtService.generateToken("test23@gmail.com"))
                .thenReturn("mock-jwt-token");

        String token = customerService.authenticateCustomer("test23@gmail.com", "password");

        assertNotNull(token);
        assertEquals("mock-jwt-token", token);
    }

    @Test
    public void authenticateCustomerNonExistingEmail(){
        Customer customer = new Customer();
        customer.setEmailId("test123@gmail.com");

        when(customerRepository.findById(customer.getEmailId())).thenReturn(Optional.empty());

        assertThrows(EKartException.class,()-> customerService.authenticateCustomer("test123@gmail.com","password"));
    }

    @Test
    public void authenticateCustomerInvalidCredential(){
        Customer customer = new Customer();

        customer.setEmailId("test123@gmail.com");
        when(customerRepository.findById("test123@gmail.com")).thenReturn(Optional.of(customer));

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());

        assertThrows(EKartException.class,()-> customerService.authenticateCustomer("test123@gmail.com","wrong_password"));
    }
}
