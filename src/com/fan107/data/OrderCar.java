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
	public float orderPoint;	//使用的积分
	public String userTel;
	public String userName;
	public String userAddress;	
	public String remark;
	public int discount;	//商家的折扣
	
	public float userPoint;
	
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
			totalOldPrice += mCar.get(i).getOldPrice() * mCar.get(i).getOrderNum();
		}
		
		return totalOldPrice;
	}
	
	public float getTotalNewPrice() {
		totalNewPrice = 0.0f;
		float oldPrice = getTotalOldPrice();
		
		if(discount != 0) {
			//使用折扣方式
			orderPoint = oldPrice * discount / 100;
			totalNewPrice = oldPrice - orderPoint;
		} else {
			//使用非折扣政策的方式
			for(int i=0; i<mCar.size(); i++) {
				totalNewPrice += mCar.get(i).getNewPrice();
			}
			orderPoint = oldPrice - totalNewPrice;
		}
		return totalNewPrice;
	}
	
	public float getNeedOrderPoint() {
		float needPoint = 0.0f;
		float oldPrice = getTotalOldPrice();
		float newPrice = 0.0f;
		
		if(discount != 0) {
			//使用折扣方式
			needPoint = oldPrice * discount / 100;
		} else {
			//使用非折扣政策的方式			
			for(int i=0; i<mCar.size(); i++) {
				newPrice += mCar.get(i).getNewPrice();
			}
			needPoint = oldPrice - newPrice;
		}
		
		return needPoint;
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
			orderObject.put("userTel", userTel);
			orderObject.put("userName", userName);
			orderObject.put("userAddress", userAddress);
			orderObject.put("areaId", areaId);
			orderObject.put("remark", remark);
//			orderObject.put("totalOldPrice", getTotalOldPrice());
			orderObject.put("totalNewPrice", getTotalNewPrice());
			orderObject.put("orderPoint", orderPoint);
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		return orderObject.toString();
	}
}
