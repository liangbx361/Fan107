package com.fan107.config;

public interface WebServiceConfig {
	//服务器的IP地址 
	public static final String SERVER_IP = "192.168.0.105";
	
	public static final String RES_URL = "http://" + SERVER_IP + "/";
	
	//命名空间
	public static final String NAME_SPACE = "http://tempuri.org/";
	public static final String url = "http://" + SERVER_IP + "/WebService/";
	
	//登录
	public static final String LOGIN_WEB_SERVICE = "LoginCheck.asmx";
	public static final String LOGIN_METHOD = "login";
	public static final String AUTO_LOGIN_METHOD = "autoLoginCheck";
	public static final String GET_LOGIN_USER_INFO_METHOD = "getLoginUserInfo";
	
	//用户信息
	public static final String USER_ACCOUNT_WEB_SERVICE = "UserAccount.asmx";
	public static final String CHANAGER_PASSWORD_METHOD = "chanagePassword";
	public static final String CHANAGER_MEMBER_INFO_METHOD = "changeMemberInfo"; 
	
	//商品列表
	public static final String SHOP_LIST_WEB_SERVICE = "ShopList.asmx";
	public static final String GET_SHOP_LIST_BY_USER_ID_METHOD = "getShopListByUserId";
	public static final String GET_SHOP_LIST_BY_ADDRESS_ID_METHOD = "getShopListByAddressId";
	
	//外卖餐厅的信息
	public static final String SHOP_INFO_WEB_SERVICE = "ShopInfo.asmx";
	public static final String GET_PRODUCT_LIST_METHOD = "getProductList";
	public static final String GET_PRODUCT_METHOD = "getProduct";

	//提交定单信息
	public static final String ORDER_CHECK_WEB_SERVICE = "Ordercheck.asmx";
	public static final String GENERATE_ORDER_METHOD = "generateOrder";
	
	//用户地址信息
	public static final String USER_ADDRESS_WEB_SERVICE = "UserAddressInfo.asmx";
	public static final String GET_DEFAULT_ADDRESS_METHOD = "getDefaultAddress";
	public static final String GET_ADDRESS_LIST_METHOD = "getList";
	public static final String DELETE_ADDRESS_METHOD = "deleteAddress";
	public static final String GET_AREA_METHOD = "getArea";
	public static final String GET_STREET_METHOD = "getStreet";
	public static final String GET_DISTRICT_METHOD = "getDistrict";
	public static final String ADD_NEW_ADDRESS_METHOD = "addNewAddress";
	public static final String UPDATE_ADDRESS_METHOD = "updateAddress";
	public static final String UPDATE_DEFAULT_ADDRESS_METHOD = "updateDefaultAddress";
}
