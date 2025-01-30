package com.amit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.amit.entity.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {

    Optional<Customer> findByUserName(String username);

    Optional<Customer> findByEmailId(String email);
}
