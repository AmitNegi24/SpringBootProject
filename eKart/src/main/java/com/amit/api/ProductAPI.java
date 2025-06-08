package com.amit.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amit.customMethods.MultipartFileToStringConverter;
import com.amit.dto.ProductDTO;
import com.amit.exception.EKartException;
import com.amit.service.ProductService;

@RestController

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/api/product-api")
public class ProductAPI {

	@Autowired
	private ProductService productService;

	@Autowired
	private Environment environment;

	Log logger = LogFactory.getLog(ProductAPI.class);

//	public static String uploadDirectory = System.getProperty("user.dir")+"/eKart/src/main/resources/static/images";
public static String uploadDirectory;
static {
	// Check environment variable to decide path
	String environment = System.getenv("ENVIRONMENT");
	if ("container".equals(environment)) {
		uploadDirectory = "/app/static/images";  // Path inside Docker container
	} else {
		uploadDirectory = System.getProperty("user.dir") + "/eKart/src/main/resources/static/images";  // Local path
	}
}


	@GetMapping(value = "/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");

	}

	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws EKartException {
		logger.info("Received a request to display all Products");
		List<ProductDTO> products = productService.getAllProducts();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping(value = "/products/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Integer id) throws EKartException {

		logger.info("Received a request to get product details for product with id as " + id);
		try {
			ProductDTO product = productService.getProductById(id);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (EKartException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(environment.getProperty(e.getMessage()));
		}

	}

	@PostMapping(value = "/product")
	public ResponseEntity<String> createProduct( ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile imageFile) throws EKartException {
		logger.info("Received a request to create a product");
		System.out.println("GOING IN TRY BLOCK");

		try {
			String originalFileName = imageFile.getOriginalFilename();
			System.out.println(originalFileName);
			 String base64Image = new MultipartFileToStringConverter().convert(imageFile);
			 Path fileNameAndPath = Paths.get(uploadDirectory, originalFileName);
			System.out.println(fileNameAndPath);
			 Files.write(fileNameAndPath, imageFile.getBytes());
			 productDTO.setProductImage(base64Image);
			productService.createProduct(productDTO);
			
			return ResponseEntity.ok("Product created successfully");
		} catch (EKartException e) {
			logger.error("Error occurred while creating product", e);
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Error occurred while creating product", e);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create product");
		}
	}

	@DeleteMapping(value = "/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) throws EKartException {
		logger.info("Received a request to delete product details for product with id as " + id);
		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok("Product deleted Successfully");
		} catch (EKartException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(environment.getProperty(e.getMessage()));
		}

	}

	@PutMapping(value = "/updateProduct/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO)
			throws EKartException {
		logger.info("Received a request to update product details for product with id as " + id);
		try {
			productService.getProductById(id);
			ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
			return ResponseEntity.ok(updatedProduct);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(environment.getProperty(e.getMessage()));
		}
	}

	@PutMapping(value = "/updateQuantity/{id}")
	public ResponseEntity<String> reduceAvailableQuantity(@PathVariable Integer id, @RequestBody Integer quantity)
			throws EKartException {

		logger.info("Received a request to update the available quantity for product with id " + id);
		try {
			productService.reduceAvailableQuantity(id, (quantity));
			return new ResponseEntity<>(environment.getProperty("ProductAPI.REDUCE_QUANTITY_SUCCESSFULL"),
					HttpStatus.OK);
		} catch (EKartException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(environment.getProperty(e.getMessage()));
		}

	}
}
