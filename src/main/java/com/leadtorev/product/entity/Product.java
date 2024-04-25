package com.leadtorev.product.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;

/*
 * Entity class play a crucial role in ORM(Object Relational Mapping frameworks like Hibernate,
 * which is used by Spring data MongoDB. MongoDB driver translates Java objects (entities) into corresponding database records and vice versa, 
 * allowing you to work with persistent data in an object-oriented manner. 
 */
@Entity
@Document(collection = "products")
public class Product {
	
	
	
	//The @Id annotation specifies the primary key field of the entity.
	@Id
	private String id;
	
	/*The fields declared below represent attributes of the Product entity, 
	 * which will be mapped to columns in the database table.
	 * All the fields are declared private to maintain data integrity and security
	 */
	private String name;
	private String description;
	private double price;
	private ArrayList<String> categories;
	private ArrayList<Map<String,String>> attributes;
	private Availability availability;
	private List<Ratings> ratings;
	
	//Using setter and getter to update and fetch values of product entity
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ArrayList<String> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	public ArrayList<Map<String, String>> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<Map<String, String>> attributes) {
		this.attributes = attributes;
	}
	public Availability getAvailability() {
		return availability;
	}
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	public List<Ratings> getRatings() {
		return ratings;
	}
	public void setRatings(List<Ratings> ratings) {
		this.ratings = ratings;
	}
	
	/* Below are the 2 functions used to remove duplicate product enitities
	 * from the product list. This product list is generated from 
	 * filtering the the collection records in databse and contains duplicate values.  
	 * So we use hasMap to remove duplicates present in product list.
	 * The equals and hashCode methods are overridden based on the 'id' field
	 * which uniquely identifies each product entity. 
	 */
	
	// Override equals method to define object equality based on id
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        
	        if (o == null || getClass() != o.getClass()) return false;
	        
	        Product product = (Product) o;
	       // return true if the id of both entities are equal
	        return id.equals(product.id);
	    }

	 // Override hashCode method to generate hash code based on id and name
	 @Override
     public int hashCode() {
		 //return a hashcode generated using id field
	        return id.hashCode();
	    }
		
}
