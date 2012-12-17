package com.fan107.data;

import java.io.Serializable;

public class OrderInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1375790656246570136L;
	
	public int id;
	public String orderNo;
	public int shopId;
	public String shopName;
	public float totalPrice;
	public float orderPoint;
	public int userId;
	public String username;
	public String userTel;
	public String userAddress;
	public int areaId;
	public String remark;
	public int orderStatus;
	public int payStatus;
	public String addTime;
}
