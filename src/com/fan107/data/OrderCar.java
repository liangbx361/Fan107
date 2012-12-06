package com.fan107.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderCar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5900346972848341241L;
	
	private List<OrderDish> mCar;
	private float totalOldPrice;
	private float totalNewPrice;
	
	public int shopId;
	public int userId;
	public int areaId;
	public float orderPoint;
	public String userTel;
	public String userName;
	public String userAddress;	
	public String remark;
	
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
	
	public String getJsonString() {
		JSONObject orderObject = new JSONObject();
		JSONArray dishArray = new JSONArray();
		
		for(int i=0; i<mCar.size(); i++) {						
			try {
				OrderDish rOrderDish = mCar.get(i);
				JSONObject dishObject = new JSONObject();
				dishObject.put("productId", rOrderDish.getProductId());
				dishObject.put("oldPrice", rOrderDish.getOldPrice());
				dishObject.put("dishName", rOrderDish.getDishName());
				dishObject.put("orderNum", rOrderDish.getOrderNum());
				dishArray.put(i, dishObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {			
			orderObject.putOpt("dishArray", dishArray);
			orderObject.put("shopId", shopId);
			orderObject.put("userId", userId);
			orderObject.put("orderPoint", orderPoint);
			orderObject.put("userTel", userTel);
			orderObject.put("userName", userName);
			orderObject.put("userAddress", userAddress);
			orderObject.put("areaId", areaId);
			orderObject.put("remark", remark);
			orderObject.put("totalOldPrice", getTotalOldPrice());
			orderObject.put("totalNewPrice", getTotalNewPrice());
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		return orderObject.toString();
	}
}
