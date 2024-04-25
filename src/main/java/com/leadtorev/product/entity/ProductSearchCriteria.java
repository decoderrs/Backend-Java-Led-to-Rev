package com.leadtorev.product.entity;

import java.util.List;
import java.util.Map;

/*This class is used for handling criteria for filtering records
 * The criteria class has 3 fields name,category,attributes.
 * These fields will be used for filtering records.
 * The criteria Object will accept the field values for each of the 3 fields
 * and using those values we will filter the records accordingly.
 * It provide getter and setter methods and constructor to handle the criteria object. 
 */

public class ProductSearchCriteria {
	//The criteria field are declared as private to maintain data integrity and security
	private String name;
	
	private List<String> categories;
	
	private List<Map<String, String>> attributes; // Map to represent key-value pairs for attributes

	//Class constructor to initialize the criteria object
	public ProductSearchCriteria(String name, List<String> categories, List<Map<String, String>> attributes) {
		super();
		this.name = name;
		this.categories = categories;
		this.attributes = attributes;
	}
	//Getters and Setters methods to fetch and update the values of fields
	//since the private fields cannot be called directly using class object
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategory(List<String> categories) {
		this.categories = categories;
	}

	public List<Map<String, String>> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Map<String, String>> attributes) {
		this.attributes = attributes;
	}

	

}
