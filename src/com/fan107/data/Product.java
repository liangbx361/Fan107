package com.fan107.data;

public class Product {
	private int Id;
	private String ProductName;
	private int ShopId;
	private String ImgSrc;
	private float Price;
	private float Price2;
	private int SortId;
	private int SellStatus;
	private int TypeId;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public int getShopId() {
		return ShopId;
	}
	public void setShopId(int shopId) {
		ShopId = shopId;
	}
	public String getImgSrc() {
		return ImgSrc;
	}
	public void setImgSrc(String imgSrc) {
		ImgSrc = imgSrc;
	}
	public float getPrice() {
		return Price;
	}
	public void setPrice(float price) {
		Price = price;
	}
	public float getPrice2() {
		return Price2;
	}
	public void setPrice2(float price2) {
		Price2 = price2;
	}
	public int getSortId() {
		return SortId;
	}
	public void setSortId(int sortId) {
		SortId = sortId;
	}
	public int getSellStatus() {
		return SellStatus;
	}
	public void setSellStatus(int sellStatus) {
		SellStatus = sellStatus;
	}
	public int getTypeId() {
		return TypeId;
	}
	public void setTypeId(int typeId) {
		TypeId = typeId;
	}
}
