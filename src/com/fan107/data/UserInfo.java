package com.fan107.data;

import java.io.Serializable;

public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1454337815540147840L;
	
	private int userid;
	private int usergroup;
	private String username;
	private String userpass;
	private String nickname;
	private int gender;	//性别
	private String birthday;
	private float totalpoint;
	private float currentpoint;
	private float userpoint;
	private int spreadcount; //推广个数
	private String mobile;
	private String email;
	private String address;
	private int utype;
	private int shopid;
	private String addtime;
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getUsergroup() {
		return usergroup;
	}
	public void setUsergroup(int usergroup) {
		this.usergroup = usergroup;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public float getTotalpoint() {
		return totalpoint;
	}
	public void setTotalpoint(float totalpoint) {
		this.totalpoint = totalpoint;
	}
	public float getCurrentpoint() {
		return currentpoint;
	}
	public void setCurrentpoint(float currentpoint) {
		this.currentpoint = currentpoint;
	}
	public float getUserpoint() {
		return userpoint;
	}
	public void setUserpoint(float userpoint) {
		this.userpoint = userpoint;
	}
	public int getSpreadcount() {
		return spreadcount;
	}
	public void setSpreadcount(int spreadcount) {
		this.spreadcount = spreadcount;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUtype() {
		return utype;
	}
	public void setUtype(int utype) {
		this.utype = utype;
	}
	public int getShopid() {
		return shopid;
	}
	public void setShopid(int shopid) {
		this.shopid = shopid;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
	
}
