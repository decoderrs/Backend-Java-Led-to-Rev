package com.leadtorev.product.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.leadtorev.product.entity.Product;

/*Repository class for creating "products" collections in MongoDb
  and writing custom queries for the application.
  The collection will hold Product entity with which will have specified schema.*/
@Repository
public interface ProductRepository  extends MongoRepository<Product, String>{
	
	//Filter products using name field and return the result
	public Page<Product> findByName(String name, Pageable pageable);
	
	//Filter products using categories field and return the result
	public Page<Product> findByCategories(String categories, Pageable pageable);
	
	//Filter products using attributes field and return the result
	public Page<Product> findByAttributes(Map<String, String> subAttributes,Pageable pageable );
	
	
	 
}
