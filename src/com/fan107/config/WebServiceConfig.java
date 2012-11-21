package com.fan107.config;

public interface WebServiceConfig {
	//服务器的IP地址 
	public static final String SERVER_IP = "192.168.0.23";
	
	public static final String RES_URL = "http://" + SERVER_IP + "/";
	
	//命名空间
	public static final String NAME_SPACE = "http://tempuri.org/";
	public static final String url = "http://" + SERVER_IP + "/WebService/";
	
	//登录
	public static final String LOGIN_WEB_SERVICE = "LoginCheck.asmx";
	public static final String LOGIN_METHOD = "login";
	public static final String GET_LOGIN_USER_INFO_METHOD = "getLoginUserInfo";
	
	//商品列表
	public static final String SHOP_LIST_WEB_SERVICE = "ShopList.asmx";
	public static final String GET_SHOP_LIST_BY_USER_ID_METHOD = "getShopListByUserId";
	public static final String GET_SHOP_LIST_BY_ADDRESS_ID_METHOD = "getShopListByAddressId";
	
	//用户信息
	public static final String USER_ACCOUNT_WEB_SERVICE = "UserAccount.asmx";
	public static final String CHANAGER_PASSWORD_METHOD = "chanagePassword";
	

}
