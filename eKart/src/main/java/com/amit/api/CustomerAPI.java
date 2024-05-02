package com.amit.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amit.dto.CustomerCredDTO;
import com.amit.dto.CustomerDTO;
import com.amit.exception.EKartException;
import com.amit.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping(value = "/api/customer-api")
public class CustomerAPI {

	@Autowired
	CustomerService customerService;

	@Autowired
	Environment environment;

	static Log logger = LogFactory.getLog(CustomerAPI.class);

	@PostMapping(value = "/login")
	public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerCredDTO custCredDTO)
			throws EKartException {

		logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAILID :" + custCredDTO.getEmailId());
		CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(custCredDTO.getEmailId(),
				custCredDTO.getPassword());
		logger.info("CUSTOMER LOGIN SUCCESS,CUSTOMER EMAIL: " + customerDTOFromDB.getEmailId());
		return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
	}

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws EKartException {

		try {
			logger.info("CUSTOMER TRYING TO REGISTER, CUSTOMER EMAIL ID:" + customerDTO.getEmailId());
			String registeredWithEmailId = customerService.registerNewCustomer(customerDTO);
			registeredWithEmailId = environment.getProperty("CustomerAPI.CUSTOMER_REGISTERATION_SUCCESS")
					+ registeredWithEmailId;

			return new ResponseEntity<>(registeredWithEmailId, HttpStatus.OK);
		} catch (EKartException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(environment.getProperty(e.getMessage()));
		}

	}

	@PutMapping(value = "/customer/{customerEmailId:.+}/address/")
	public ResponseEntity<String> updateShippingAddress(
			@PathVariable @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
			@RequestBody String address) throws EKartException {
		customerService.updateShippingAddress(customerEmailId, address);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
	}

	@DeleteMapping(value = "/customer/{customerEmailId:.+}")
	public ResponseEntity<String> deleteShippingAddress(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {

		customerService.deleteShipingAddress(customerEmailId);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
	}
}
