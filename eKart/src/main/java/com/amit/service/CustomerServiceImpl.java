package com.amit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amit.dto.CustomerDTO;
import com.amit.entity.Customer;
import com.amit.exception.EKartException;
import com.amit.repository.CustomerRepository;

@Service(value = "customerService")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public CustomerDTO authenticateCustomer(String emailId, String password) throws EKartException {
		
		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(()-> new EKartException("CustomerService.CUSTOMER_NOT_FOUND"));
		if(!password.equals(customer.getPassword())) {
			throw new EKartException("CustomerService.INVALID_CREDENTIALS");
		}
		
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		//customerDTO.setPassword(customer.getPassword());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setAddress(customer.getAddress());
		customerDTO.setRole(customer.getRole());
		return customerDTO;
	}

	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws EKartException {
		String registeredWithEmailId = null;
		boolean isEmailNotAvailable = customerRepository.findById(customerDTO.getEmailId().toLowerCase()).isEmpty();
		boolean isPhoneNumberNotAvailable = customerRepository.findById(customerDTO.getPhoneNumber()).isEmpty();
		
		if(isEmailNotAvailable){
			if(isPhoneNumberNotAvailable) {
				Customer customer = new Customer();
				customer.setEmailId(customerDTO.getEmailId().toLowerCase());
				customer.setAddress(customerDTO.getAddress());
				customer.setName(customerDTO.getName());
				customer.setPassword(customerDTO.getPassword());
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
		customerDTO.setName(customer.getName());
		customerDTO.setPassword(customer.getPassword());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setRole("user");
		return customerDTO;
	}

}
