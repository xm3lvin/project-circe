package com.kjt.circe.domain;

/**
 * 	Represents a particular condition of an object.
 * 
 *	@since	1.0
 */
public interface Status {
	
	/**
	 * @return Returns the description that describes this {@code Status}.
	 */
	String getDescription();
	
	/**
	 * @return Returns the <strong>unique</strong> code that identifies this {@code Status}.
	 */
	int getCode();
	
	/**
	 * 
	 * @param statuses
	 * 
	 * @return
	 */
	default boolean equalsAny(Status ... statuses) {
		boolean found = false;
		for(Status status : statuses) {
			if(equalTo(status)) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * 
	 * @param other
	 * 
	 * @return
	 */
	default boolean equalTo(Status other) {
		return getCode() == other.getCode();
	}

}
