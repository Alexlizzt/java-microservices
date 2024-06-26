package com.company.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.company.productservice.entity.ProductEntity;
public interface ProductRepository extends MongoRepository<ProductEntity, String>{

}
