package com.leadtorev.product.entity;

/**Availability class responsible for creating field of availability attribute 
 * in entity product class. The class has 2 fields inStock and quantity.
 *
 */
 
public class Availability {
	
	//fields
	private boolean inStock;
	private int quantity;
	

	//Getter and setter method to fetch and udpate the fields in availability
	public boolean getInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	

}
