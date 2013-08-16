package com.thesys.opencms.laphone.cart.dao;


/**
 * shopping cart and next buy both use this class
 * @author maggie
 *
 */
public class ThesysCartVO {

	public static final String JSON_ITEM_ID = "itemId";	
	public static final String JSON_QUANTITY = "quantity";			
	

	public static final int ITEM_TYPE_MAIN = 1;
	public static final int ITEM_TYPE_ADDITIONAL = 2;
	
	private String itemId;	
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private int quantity;
	
	public ThesysCartVO(){
		super();

	}
	
	public static ThesysCartVO getInstance(String itemId,int quantity) throws Exception{
		ThesysCartVO result = new ThesysCartVO();
		
		result.setItemId(itemId);
		result.setQuantity(quantity);
		return result;
		
	}
	
	
	
	
	
}
