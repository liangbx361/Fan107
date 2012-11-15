package com.fan107.config;

public interface WebServiceConfig {
	//��������IP��ַ 
	public static final String SERVER_IP = "192.168.0.105";
	//�����ռ�
	public static final String NAME_SPACE = "http://tempuri.org/";
	public static final String url = "http://" + SERVER_IP + "/WebService/";
	
	//��¼
	public static final String LOGIN_WEB_SERVICE = "LoginCheck.asmx";
	public static final String LOGIN_METHOD = "login";
	public static final String GET_LOGIN_USER_INFO_METHOD = "getLoginUserInfo";
	
	//��Ʒ�б�
	public static final String SHOP_LIST_WEB_SERVICE = "ShopList.asmx";
	
	//�û���Ϣ
	public static final String USER_ACCOUNT_WEB_SERVICE = "UserAccount.asmx";
	public static final String CHANAGER_PASSWORD_METHOD = "chanagePassword";

}
