package com.jvdw.product.data.repository;

import com.jvdw.product.data.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The ProductRepository interface will be used at runtime to create an implementation and a JDK Dynamic proxy
 * used to intelligently implement database operations from method names and the base CrudRepository interface
 * @see org.springframework.data.repository.CrudRepository
 */
@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String>
{}
