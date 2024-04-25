package com.leadtorev.product.entity;

import org.springframework.data.mongodb.core.index.Indexed;

/**This class is used to handle the ratings field in product entity
 * 
 * @author mayur
 *
 */
public class Ratings {
	
	/**@Indexed annotation is used to index the field in the entity.
	 * Index is used improve the speed and efficienct of data
	 * retrieval operations.Indexes provide rapid access paths to 
	 * data based on the values of specific columns. 
	 */
	@Indexed(unique = true)
	private String userId;
	private int rating;
	private String comment;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
