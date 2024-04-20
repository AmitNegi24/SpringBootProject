package com.amit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amit.dto.ProductDTO;
import com.amit.entity.Product;
import com.amit.exception.EKartException;
import com.amit.repository.ProductRepository;

@Service(value = "productService")
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	

	@Override
	public List<ProductDTO> getAllProducts() throws EKartException {
		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		List<Product> products = productRepository.findAll(sort);
		
		if(products.isEmpty()) {
			throw new EKartException("Service.PRODUCT_NOT_FOUND");
		}
        return products.stream()
        		.map(product->{
        			ProductDTO productDTO = new ProductDTO();
        			productDTO.setBrand(product.getBrand());
        			productDTO.setCategory(product.getCategory());
        			productDTO.setDescription(product.getDescription());
        			productDTO.setName(product.getName());
        			productDTO.setPrice(product.getPrice());
        			productDTO.setProductId(product.getProductId());
        			productDTO.setAvailableQuantity(product.getAvailableQuantity());
        			productDTO.setProductImage(product.getProductImage());
        			productDTO.setTitle(product.getTitle());
        			
        			return productDTO;
        		}).collect(Collectors.toList());
	}

	@Override
	public ProductDTO getProductById(Integer productId) throws EKartException {
	  
	    // Use the String productIdString to query the database
	    Optional<Product> productOptional = productRepository.findByProductId(productId);
	    Product product = productOptional.orElseThrow(() -> new EKartException("ProductService.PRODUCT_NOT_AVAILABLE"));

	    ProductDTO productDTO = new ProductDTO();
	    productDTO.setBrand(product.getBrand());
	    productDTO.setCategory(product.getCategory());
	    productDTO.setDescription(product.getDescription());
	    productDTO.setName(product.getName());
	    productDTO.setPrice(product.getPrice());
	    productDTO.setProductId(product.getProductId());
	    productDTO.setAvailableQuantity(product.getAvailableQuantity());
	    productDTO.setProductImage(product.getProductImage());
	    productDTO.setTitle(product.getTitle());

	    return productDTO;
	}

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) throws EKartException {
		
		 Integer productId = productDTO.getProductId();

		    // Check if the product with the same ID already exists
		    Optional<Product> existingProductOptional = productRepository.findByProductId(productId);
		    if (existingProductOptional.isPresent()) {
		        throw new EKartException("Product with ID " + productId + " already exists");
		    }
		    
		Product product = new Product();
		
		product.setBrand(productDTO.getBrand());
		product.setCategory(productDTO.getCategory());
		product.setDescription(productDTO.getDescription());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setProductId(productDTO.getProductId());
		product.setAvailableQuantity(productDTO.getAvailableQuantity());
		product.setProductImage(productDTO.getProductImage());
		product.setTitle(productDTO.getTitle());
		
		Product savedProduct = productRepository.save(product);
		
		ProductDTO productDTO1= new ProductDTO();
		
		productDTO1.setBrand(savedProduct.getBrand());
		productDTO1.setCategory(savedProduct.getCategory());
		productDTO1.setDescription(savedProduct.getDescription());
		productDTO1.setName(savedProduct.getName());
		productDTO1.setPrice(savedProduct.getPrice());
		productDTO1.setProductId(savedProduct.getProductId());
		productDTO1.setAvailableQuantity(savedProduct.getAvailableQuantity());
		productDTO1.setProductImage(savedProduct.getProductImage());
		productDTO1.setTitle(savedProduct.getTitle());
		
		return productDTO1;
		
	}

	@Override
	public ProductDTO updateProduct(Integer productId, ProductDTO productDTO)throws EKartException {
		
		Optional<Product> optionalProduct=productRepository.findByProductId(productId);
		if(optionalProduct.isEmpty()) {
			throw new EKartException("ProductService.PRODUCT_NOT_AVAILABLE");
		}
		else if(optionalProduct.isPresent()) {
			Product existingProduct = optionalProduct.get();
			existingProduct.setBrand(productDTO.getBrand());
			existingProduct.setCategory(productDTO.getCategory());
			existingProduct.setDescription(productDTO.getDescription());
			existingProduct.setName(productDTO.getName());
			existingProduct.setPrice(productDTO.getPrice());
			existingProduct.setProductId(productDTO.getProductId());
			existingProduct.setAvailableQuantity(productDTO.getAvailableQuantity());
			existingProduct.setProductImage(productDTO.getProductImage());
			existingProduct.setTitle(productDTO.getTitle());
			
			Product updatedProduct=productRepository.save(existingProduct);
			
			ProductDTO productDTO1= new ProductDTO();
			
			productDTO1.setBrand(updatedProduct.getBrand());
			productDTO1.setCategory(updatedProduct.getCategory());
			productDTO1.setDescription(updatedProduct.getDescription());
			productDTO1.setName(updatedProduct.getName());
			productDTO1.setPrice(updatedProduct.getPrice());
			productDTO1.setProductId(updatedProduct.getProductId());
			productDTO1.setAvailableQuantity(updatedProduct.getAvailableQuantity());
			productDTO1.setProductImage(updatedProduct.getProductImage());
			productDTO1.setTitle(updatedProduct.getTitle());
			
			return productDTO1;
		}
		return null;
	}

	@Override
	public void deleteProduct(Integer productId) throws EKartException {
		Optional<Product> productOptional = productRepository.findByProductId(productId);
	    Product product = productOptional.orElseThrow(() -> new EKartException("ProductService.PRODUCT_NOT_AVAILABLE"));
		productRepository.deleteByProductId(product.getProductId());
		
	}

	@Override
	public void reduceAvailableQuantity(Integer productId, Integer quantity) throws EKartException {
	
		Optional<Product> productOptional = productRepository.findByProductId(productId);
		Product product = productOptional.orElseThrow(()-> new EKartException("ProductService.PRODUCT_NOT_AVAILABLE"));
		product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
		productRepository.save(product);
		
	}

	

}
