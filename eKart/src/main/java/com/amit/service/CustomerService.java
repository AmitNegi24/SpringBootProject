package com.amit.service;

import com.amit.dto.CustomerDTO;
import com.amit.exception.EKartException;

public interface CustomerService {

	CustomerDTO authenticateCustomer(String emailId,String password) throws EKartException;
	
	String registerNewCustomer(CustomerDTO customerDTO) throws EKartException;
	
	void updateShippingAddress(String customerEmailId,String address)throws EKartException;
	
	void deleteShipingAddress(String customerEmailId)throws EKartException;
	
	CustomerDTO getCustomerByEmailId(String emailId)throws EKartException;
}
