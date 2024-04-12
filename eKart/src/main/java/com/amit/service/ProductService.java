package com.amit.service;

import com.amit.dto.ProductDTO;
import com.amit.exception.EKartException;

import java.util.List;

public interface ProductService {
	
    List<ProductDTO> getAllProducts()throws EKartException;
    ProductDTO getProductById(Integer productId)throws EKartException;
    ProductDTO createProduct(ProductDTO productDTO) throws EKartException;
    ProductDTO updateProduct(Integer productId, ProductDTO productDTO) throws EKartException;
    void deleteProduct(Integer productId)throws EKartException;
    void reduceAvailableQuantity(Integer productId, Integer quantity) throws EKartException;
}
