package com.leadtorev.product.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadtorev.product.entity.Product;
import com.leadtorev.product.entity.ProductSearchCriteria;
import com.leadtorev.product.entity.Ratings;
import com.leadtorev.product.repository.ProductRepository;

import com.mongodb.client.result.UpdateResult;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final MongoTemplate mongoTemplate;
	
	/* Creating and instance of ProductRepository and MongoTemplate
	 * ProductRepository- Provide CRUD operations to be performed on entities stored in database.
	 * MongoTemplate- Primary implementation of MongoOperations. 
	 * It simplifies the use of imperative MongoDB usage and helps toavoid common errors. 
	 * It executes core MongoDB workflow, leaving application code to provide Document andextract results. */
	@Autowired
	public ProductService( ProductRepository productRepository,MongoTemplate mongoTemplate) {
		this.productRepository = productRepository;
		this.mongoTemplate = mongoTemplate;
	}
	
	//Save the product entities from json data file
	 public ResponseEntity<List<Product>> saveEntitiesFromJsonFile(String filePath) throws IOException {
	        ObjectMapper objectMapper = new ObjectMapper();
	        
	        //Storing the product object from file stored in temporary location into List<Product>
	        List<Product> entities = objectMapper.readValue(new File(filePath),
	            new TypeReference<List<Product>>() {});
	        
	        //Send entities list to database for storing
	        productRepository.saveAll(entities);

	        return ResponseEntity.ok(productRepository.findAll());
	    }
	 
	 //Fetch single product document from collection 
	 public ResponseEntity<Product> getProduct(String id) {
		 return ResponseEntity.ok(productRepository.findById(id).orElse(null));
	 }
	 
	 //Fetch all the products from collection
	 public ResponseEntity<List<Product>> getAllProducts(Integer pageNumber, Integer pageSize) {
		 
		//Create pageable object if pageNumber and pageSize are not null, else create unpageable object
		 Pageable pageable = pageNumber != null && pageSize != null ? PageRequest.of(pageNumber, pageSize) : Pageable.unpaged();
		 
		 //This is used to return the query request into Page object
		 Page<Product> pageProduct = this.productRepository.findAll(pageable);
		 
		 //using getContent() to get list of products in a list
		 List<Product> productList = pageProduct.getContent();
		 
		 //Return product list of fetched product
		 return ResponseEntity.ok(productList);
	 }
	 
	 // Implement the search logic based on the provided criteria
	 public List<Product> searchProducts(ProductSearchCriteria criteria, Integer pageNumber, Integer pageSize){
		 
		 //Variables to store the attributes of the criteria object 
		 String name = criteria.getName();
		 List<String> categories = criteria.getCategories();
		 List<Map<String, String>> attributes =criteria.getAttributes();
		 
		 //Create pageable object if pageNumber and pageSize are not null, else create unpageable object
		 Pageable pageable = pageNumber != null && pageSize != null ? PageRequest.of(pageNumber, pageSize) : Pageable.unpaged();
		 
		 
		 //A list that has Product datatype. It will be used to store filtered product objects from the
		 //database. After all the criterias are used to filter the data we return the list to ProductController
		 
		 List<Product> filteredProduct = new ArrayList<Product>() ;			
		 
		 if (categories != null) {
		        
	        	for(String cat: categories ) {
	        		filteredProduct.addAll(productRepository.findByCategories(cat, pageable).getContent());
	        	} 	
	        	
	        }
		 
	        // Add criteria for each field
	        if (name != null) {
	        	filteredProduct.addAll(productRepository.findByName(name,pageable).getContent());
	        }
	        
	        if (attributes != null ) {

	        	System.out.println(attributes.get(0));
	        	filteredProduct.addAll(productRepository.findByAttributes(attributes.get(0), pageable).getContent());
	        	List<Product> resultList = new ArrayList<Product>(filteredProduct);
	        	
	        	if(!filteredProduct.isEmpty()) {
	        		for(Product p:resultList) {
		        			if(!ProductService.isMatch(p.getAttributes(), attributes)) {
		        				filteredProduct.remove(p);
		        			};
		        		}		        	
		        	}

	        	}
	        
	        filteredProduct  = matchAllFilters(filteredProduct,criteria);
	        
	       // filteredProduct.addAll(mongoTemplate.find(query, Product.class));
	        System.out.print("Filtered Product:"+ filteredProduct);
	        return filteredProduct;
		 
	 }
	 
	 //Saves a single product to database and returns the saved document
	 public ResponseEntity<String> addProduct(Product product) {
		 if(product != null) {
			 Product savedprd = productRepository.save(product);
			 
			 //return saved record
			 if(savedprd != null) {
				 return ResponseEntity.status(HttpStatus.CREATED)
						 .header("Content-Type","application/json")
						 .body("Product added successfully! \n" + savedprd);
			 }
			 else {//if no record return, then the product did not saved
				 return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						 .header("Content-Type", "application/json")
						 .body("Product could not be added!!");
			 }
		 }
		 else {//If the request body is empty
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					 .header("Content-Type", "application/json")
					 .body("Please add a product attributes!");
		 }
	 }
	 
	 
	 //Finds the record using id and updating the record  
	 public String updateRecord(String id, Product product){
		 
		 //Collection should not be null
		 Assert.notNull(id, "ID must not be null");
		 
		 //Create query to find document by id
		 Query query = new Query(Criteria.where("id").is(id));
		 
		 //Update object to specify the update operation.
		 Update update = new Update();
		 
		 //If name field is not null, then set the name field in product entity with new name. 
		 if(product.getName() != null) {
			 update.set("name", product.getName());
		 }
		 
		 //If price field is not null, then set the price field in product entity with new price. 
		 if(product.getPrice() > 0) {
			 update.set("price", product.getPrice());
	
		 }
		 
		 /*If availability field is not null, then set the availability field in product entity with new availability.
		  * The update query targets the specific field within the nested object using dot notation
		  */ 
		
		 if(product.getAvailability() != null) {
			 
			 //If isInStock value is true and getQuantity value is positive integer then update the value
			 if(product.getAvailability().getInStock() && product.getAvailability().getQuantity() >= 0
					 && product.getAvailability().getQuantity()%1 == 0) {
				 update.set("availability.quantity", product.getAvailability().getQuantity());
			 }
			 //Else respond invalid value
			 else if(product.getAvailability().getInStock() && product.getAvailability().getQuantity() < 0) {
				 return "Invalid value: " + product.getAvailability().getQuantity();
			 }
		 }
		 //If categories field is not null, then set the categories field with new value.
		 if(product.getCategories() != null) {
			 update.set("categories",  product.getCategories());
		 }
		 //If description field is not null, then set the description field with new value
		 if(product.getDescription() != null) {
			 update.set("description", product.getDescription());
		 }
		 //If Attributes field is not null, then set the Attributes field with new value
		 if(product.getAttributes() != null) {
			 update.set("attribute", product.getAttributes());
		 }
		 //If ratings field is not null,then set the ratings field with new value
		 if(product.getRatings() != null) {
			 update.set("ratings", product.getRatings());
		 }
		 
		 //Send the query , update and entity class to construct a updation query and send it to the mongodb 
		 //for processing. Returns a result which contains the updated document.
		 UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
		
		 // return the result object containing the product entity
		return "Updated Product: " + result;
	 }
	 
	 //Finds the record using id and deletes the product from database
	 public boolean deleteProduct(String id) {
		 productRepository.deleteById(id);
		 
		 //Search for deleted record
		 Optional<Product> product = productRepository.findById(id);
		 
		 //if null return true else false
		 return product== null ? true : false;
		 
	 }
	 
	 //Adde new rating for the product
	 public Product rateProduct(String productId, Ratings rating) {
		 /* Query to find the element using id. Criteria.where is used to create search queries.
		  * The field value which will be used to search for document is provided to 
		  * Criteria.where("fieldname").is(field_value), and it will generate a search query.
		  */
		   
		 Query query = new Query(Criteria.where("_id").is(productId));
		 
		 //Adding a new ratings Object to ratings field array
		 Update update = new Update().push("ratings", rating);
		 
		 //Sends the query to the database to execute 
		 return mongoTemplate.findAndModify(query, update, Product.class);
	 }
	 
	 //Change the rating field of the already present rating object
	public Product updateRating(String productId,String userId, int newrating) {
		
		//Find the Product entity using the productId
		Product prdEntity = productRepository.findById(productId).orElse(null);
		
		//If entity is not null
		if( prdEntity != null ) {
			//Storing the ratings field array in the ratings list for ease of performing operations  
			List<Ratings> ratings = prdEntity.getRatings();
			
			//Iterates over the list and finds the correct ratings object using userId and
			//then update the rating with the new rating provided by "newrating" integer
			for(Ratings rating: ratings) {
				if(rating.getUserId().equals(userId)) {
					rating.setRating(newrating);
					break;
				}
			}
			//Set the updated ratings array in the product entity.
			prdEntity.setRatings(ratings);
			
			//Save the product entity
			productRepository.save(prdEntity);
			
		}else {//If there is no product entity of given id
			
			ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.header("Cotnent-Type", "application/json")
			.body("No such product found with given id");
		}
		
		//return the updated product entity to verify the updation
		return productRepository.findById(productId).orElse(null);
		}
	
	 public List<Product> filterProductList(List<Product> list1, List<Product> list2){
		 for ( Product p: list2) {
			 if(!list1.contains(p)) {
				 list1.remove(p);
				 System.out.println("Reach here:");
			 };
		 }
			return list1;
    	
	 }
	 
	 
     private static boolean isMatch(List<Map<String, String>> largerList, List<Map<String, String>> smallerList) {
	        
	    	if (largerList.containsAll(smallerList)) {
	                return true;
	            }else 
	            {
	            	return false;
	            }
	    }
     
     //Helper function to combine all the results of each filter and check if each entity satifies the each criteria condition or not
     //If the entity doesn't satisfy the condition, then remove that entity from the product list.
     //After iteration through the whole list, return the final modified list. 
     private static List<Product> matchAllFilters(List<Product> filteredProduct, ProductSearchCriteria criteria) {
    	 
    	 String name = criteria.getName();
		 List<String> categories = criteria.getCategories();
		 List<Map<String, String>> attributes =criteria.getAttributes();
		 
    	 List<Product> resultList = new ArrayList<Product>(filteredProduct);
	        
	        //Combining the result of all the filters 
    	 
	        for(Product p: resultList) {
	        	System.out.println("filter"+ filteredProduct.size());
	        	
	        	//If name criteria is not null
	        	if( name != null) {
	        		//Check whether an entity contains name criteria in their name fields 
	        		if(!p.getName().equals(name)) {
	        				filteredProduct.remove(p);
	        			}
	        		}
	        	//If attributes criteria is not null
	        	if( attributes != null) {
	        		//Check whether an entity contains attributes criteria in their attributes field
	        		//It should satisfy all the attribute values, then only it will keep the entity.Otherwise remove it.
	        		if(!p.getAttributes().containsAll(attributes)) {
	        			filteredProduct.remove(p);
	        		}
	        	}
	        	//If categories criteria is not null
	        	if(categories !=null) {
	        		boolean flag = true;
	        		//Check whether an entity contains categories criteria in their categories field
	        		//If atleast one category value is present, then keep the entity. Else remove that entity.
	        		for(String i: categories) {
	        			if(!p.getCategories().contains(i)) {
	        				flag = false;
	        			}
	        			else {
	        				flag = true;
	        			}
	        		}
	        		if(!flag) {//If flag is false for all the categories then remove that entity
	        			filteredProduct.remove(p);
	        		}
	        	}

	        }
	        
	        //Removing duplicate values and returning the list
			return removeDuplicates(filteredProduct);
	       
     }
     
     //This methods takes product list as input, iterates over it,
     //and each unique element to a 'HashSet' (which automatically removes the duplicates.
     private static List<Product> removeDuplicates(List<Product> list){
    	 // Create a set to store unique elements
    	 Set<Product> set = new HashSet<>();
    	 
    	 //Iterate over the original list and add each element to the set
    	 for(Product element : list) {
    		 set.add(element);
    	 }
    	 
    	 //Clear the original list
    	 list.clear();
    	 
    	 //Add all the elements from the set back to the original list
    	 list.addAll(set);
    	 
    	 return list;
     }
}