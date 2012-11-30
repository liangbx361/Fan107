package com.fan107.config;

public interface WebServiceConfig {
	//��������IP��ַ 
	public static final String SERVER_IP = "192.168.0.23";
	
	public static final String RES_URL = "http://" + SERVER_IP + "/";
	
	//�����ռ�
	public static final String NAME_SPACE = "http://tempuri.org/";
	public static final String url = "http://" + SERVER_IP + "/WebService/";
	
	//��¼
	public static final String LOGIN_WEB_SERVICE = "LoginCheck.asmx";
	public static final String LOGIN_METHOD = "login";
	public static final String AUTO_LOGIN_METHOD = "autoLoginCheck";
	public static final String GET_LOGIN_USER_INFO_METHOD = "getLoginUserInfo";
	
	//�û���Ϣ
	public static final String USER_ACCOUNT_WEB_SERVICE = "UserAccount.asmx";
	public static final String CHANAGER_PASSWORD_METHOD = "chanagePassword";
	
	//��Ʒ�б�
	public static final String SHOP_LIST_WEB_SERVICE = "ShopList.asmx";
	public static final String GET_SHOP_LIST_BY_USER_ID_METHOD = "getShopListByUserId";
	public static final String GET_SHOP_LIST_BY_ADDRESS_ID_METHOD = "getShopListByAddressId";
	
	//������������Ϣ
	public static final String SHOP_INFO_WEB_SERVICE = "ShopInfo.asmx";
	public static final String GET_PRODUCT_LIST_METHOD = "getProductList";
	public static final String GET_PRODUCT_METHOD = "getProduct";

}
