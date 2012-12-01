package com.fan107.data;

import java.io.Serializable;

public class OrderDish implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4558063658122228216L;
	
	private String dishName;
	private float oldPrice;
	private float newPrice;
	private int orderNum;
	
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public float getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}
	public float getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
	
}
