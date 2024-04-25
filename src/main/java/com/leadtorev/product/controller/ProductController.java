package com.leadtorev.product.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

import com.leadtorev.product.entity.Product;
import com.leadtorev.product.entity.ProductSearchCriteria;
import com.leadtorev.product.entity.Ratings;
import com.leadtorev.product.repository.ProductRepository;
import com.leadtorev.product.service.ProductService;

//Control class to create API endpoints and serve up request and response
@RestController
@RequestMapping("/products")
public class ProductController {
	
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	//Testing the rest Server
	@GetMapping("/hello")
	public ResponseEntity<String> helloWorld() {
		String message = "hello world!!!";
		
		return ResponseEntity.status(HttpStatus.OK)
				.header("Content-Type", "application/json")
				.body(message);
	}
	
	//To find all the products
	@GetMapping("/all-products")
	public ResponseEntity<List<Product>> findAllProducts(
			@RequestParam(value= "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", required = false) Integer pageSize
		){
		return productService.getAllProducts(pageNumber,pageSize);
	}
	
	//To find product using productId
	@GetMapping("/find-product/{productId}")
	public ResponseEntity<Product> findProduct(@PathVariable String productId){
		ResponseEntity<Product> prd = productService.getProduct(productId);
		return prd;
	}
	
	//To find products according to given criteria
	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestBody ProductSearchCriteria criteria, 
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize",required = false) Integer pageSize){
		List<Product> searchProducts = productService.searchProducts(criteria,pageNumber, pageSize);
		return ResponseEntity.status(HttpStatus.OK)
				.header("Content-Type", "application/json")
				.body(searchProducts);
	}
	
	//Adds a single product document to the collection
	@PostMapping("/add-product")
	public ResponseEntity<String> addProduct(@RequestBody Product product){

		ResponseEntity<String> productAdded = productService.addProduct(product);

		return productAdded;
	}
	

	//Modify the product document in mongodb
	@PutMapping("/update-product/{productId}")
	public ResponseEntity<String> updateProduct(@PathVariable String productId,@RequestBody Product product){
		String message = productService.updateRecord(productId, product);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.header("Content-Type", "application/json")
				.body(message);
	}
	
	//Delete product document in mongodb
	@DeleteMapping("/delete-product/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable String productId){
		productService.deleteProduct(productId) ;
		
		Optional<Product> prd = productRepository.findById(productId);
		
		if(prd == null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
			.header("Content-Type", "application/json")
			.body("message :The product is deleted successfully!!");
		}
		else {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.header("Content-Type", "application/json")
					.body("message :The product could not be deleted");
		}
	}
	
	//Update ratings of products
	@PostMapping("/{productId}/add-ratings")
	public ResponseEntity<Product> rateProduct(@PathVariable  String productId, @RequestBody Ratings rating){
		//calling and setting parameters of the rateProduct method of service class to updated ratings
		Product updatedProduct = productService.rateProduct(productId, rating);
		//return updated products rating as response
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}
	
	@PutMapping("/{productId}/{userId}/update-rates")
	public ResponseEntity<Product> updateRate(@PathVariable  String productId,@PathVariable String userId, @RequestParam int newRating){
		//calling and setting parameters of the rateProduct method of service class to updated ratings
		Product updatedProduct = productService.updateRating(productId,userId, newRating);
		//return updated products rating as response
		return ResponseEntity.ok(updatedProduct);
	}
	
	//import json data from json file containing collection of product documents
	@PostMapping(path = "/import-json")
	public ResponseEntity<String> importJson(@RequestParam("file") MultipartFile file){
		try {
			if(file.isEmpty()) {//If file is empty
				return ResponseEntity.badRequest().body("Please upload a file.");
			}
			
			//Get the file name
			String fileName= StringUtils.cleanPath(file.getOriginalFilename());
			
			//Save file to a temporary location
			Path tempLocation = Files.createTempFile("temp-json", ".json");
			file.transferTo(tempLocation);
			
			//Process the file
			productService.saveEntitiesFromJsonFile(tempLocation.toString());
			
			//Delete the temporary file
			Files.delete(tempLocation);
			
			return ResponseEntity.ok("Data imported Successfully.");
		} catch(IOException e) {
			//If IOexception occurs return error message response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error importing data: " + e.getMessage());
		}
	}
}
