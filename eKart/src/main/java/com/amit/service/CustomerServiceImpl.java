package com.amit.service;

import java.util.Optional;

import com.amit.customMethods.ProjectSecurityConfig;
import com.amit.entity.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amit.dto.CustomerDTO;
import com.amit.entity.Customer;
import com.amit.exception.EKartException;
import com.amit.repository.CustomerRepository;

@Service(value = "customerService")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	@Lazy
	private ProjectSecurityConfig projectSecurityConfig;

	@Autowired
	@Lazy
	AuthenticationManager authenticationManager;

	@Autowired
	JWTService jwtService;

	@Override
	public String authenticateCustomer(String emailId, String password) throws EKartException {

		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("CustomerService.CUSTOMER_NOT_FOUND"));

		try {
			// Attempt authentication
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(customer.getEmailId(), password));

			// If authentication is successful, populate and return the CustomerDTO
			if (authentication.isAuthenticated()) {

				return jwtService.generateToken(emailId);

//				CustomerDTO customerDTO = new CustomerDTO();
//				customerDTO.setEmailId(customer.getEmailId());
//				customerDTO.setUserName(customer.getUserName());
//				customerDTO.setPassword(customer.getPassword()); // Make sure to avoid returning password directly in DTOs
//				customerDTO.setPhoneNumber(customer.getPhoneNumber());
//				customerDTO.setAddress(customer.getAddress());
//				customerDTO.setRole(customer.getRole());
//				return customerDTO;
			}
			else {
				throw new EKartException("CustomerService.INVALID_CREDENTIALS");
			}
		} catch (Exception e) {
			throw new EKartException("CustomerService.INVALID_CREDENTIALS");
		}
	}



//	@Override
//	public String verifyLogin(CustomerDTO customerDTO) throws EKartException {
//		Authentication authentication =
//				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerDTO.getEmailId(), customerDTO.getPassword()));
//		if(authentication.isAuthenticated()) {
//			return "Login Successfully";
//		}
//		else{
//			return "Login Failed";
//		}
//	}

	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws EKartException {
		String registeredWithEmailId = null;
		boolean isEmailNotAvailable = customerRepository.findById(customerDTO.getEmailId().toLowerCase()).isEmpty();
		boolean isPhoneNumberNotAvailable = customerRepository.findById(customerDTO.getPhoneNumber()).isEmpty();
		
		if(isEmailNotAvailable){
			if(isPhoneNumberNotAvailable) {
				PasswordEncoder passwordEncoder = projectSecurityConfig.passwordEncoder();
				String encodedPassword = passwordEncoder.encode(customerDTO.getPassword());
				Customer customer = new Customer();
				customer.setEmailId(customerDTO.getEmailId().toLowerCase());
				customer.setAddress(customerDTO.getAddress());
				customer.setUserName(customerDTO.getUserName());
				customer.setPassword(encodedPassword);
				customer.setPhoneNumber(customerDTO.getPhoneNumber());
				customer.setRole("user");
				
				customerRepository.save(customer);
				
				registeredWithEmailId = customer.getEmailId();
			}
			else {
				throw new EKartException("CustomerService.PHONE_NUMBER_ALREADY_IN_USE");
			}
		}else {
			throw new EKartException("CustomerService.EMAIL_ID_ALREADY_IN_USE");
		}
		return registeredWithEmailId;
	}

	@Override
	public void updateShippingAddress(String customerEmailId, String address) throws EKartException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(()->new EKartException("CustomerService.Customer_NOT_FOUND"));
		customer.setAddress(address);
		
	}

	@Override
	public void deleteShipingAddress(String customerEmailId) throws EKartException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(()->new EKartException("CustomerService.Customer_NOT_FOUND"));
		customer.setAddress(null);
		
	}

	@Override
	public CustomerDTO getCustomerByEmailId(String emailId) throws EKartException {
		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(()->new EKartException("CustomerService.Customer_NOT_FOUND"));
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setAddress(customer.getAddress());
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setUserName(customer.getUserName());
		customerDTO.setPassword(customer.getPassword());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setRole("user");
		return customerDTO;
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Optional<Customer> optionalCustomer = customerRepository.findByUserName(username);
//		Customer customer = optionalCustomer.orElseThrow(()->new UsernameNotFoundException("CustomerService.Customer_NOT_FOUND"));
//		return new UserPrincipal(customer);
//	}

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		Optional<Customer> optionalCustomer = customerRepository.findByEmailId(emailId);  // Change here to use email
		Customer customer = optionalCustomer.orElseThrow(() -> new UsernameNotFoundException("CustomerService.Customer_NOT_FOUND"));
		return new UserPrincipal(customer);
	}

}
