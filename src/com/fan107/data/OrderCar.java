package com.fan107.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderCar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5900346972848341241L;
	
	private List<OrderDish> mCar;
	private float totalOldPrice;
	private float totalNewPrice;
	
	
	public OrderCar() {
		mCar = new ArrayList<OrderDish>();
	}
	
	public void addItem(OrderDish dish) {
		boolean isNew = true;
		for(int i=0; i<mCar.size(); i++) {
			OrderDish child = mCar.get(i);
			if(child.getDishName().equals(dish.getDishName())) {
				int sum = child.getOrderNum() + dish.getOrderNum();
				child.setOrderNum(sum);
				isNew = false;
			}
		}
		if(isNew) mCar.add(dish);
	}
	
	public void removeItem(int location) {
		mCar.remove(location);
	}
	
	public OrderDish getItem(int location) {
		return mCar.get(location);
	}
	
	public int getItemNum() {
		return mCar.size();
	}
	
	public float getTotalOldPrice() {
		totalOldPrice = 0.0f;
		
		for(int i=0; i<mCar.size(); i++) {
			totalOldPrice += mCar.get(i).getOldPrice();
		}
		
		return totalOldPrice;
	}
	
	public float getTotalNewPrice() {
		totalNewPrice = 0.0f;
		
		for(int i=0; i<mCar.size(); i++) {
			totalNewPrice += mCar.get(i).getNewPrice();
		}
		
		return totalNewPrice;
	}
}
