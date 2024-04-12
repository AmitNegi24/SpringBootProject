package com.amit.repository;

import com.amit.entity.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
	List<Product> findAll(Sort sort);
	Optional<Product> findByProductId(Integer productId);

	void deleteByProductId(Integer productId);
}
