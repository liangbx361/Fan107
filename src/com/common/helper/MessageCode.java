package com.common.helper;

public interface MessageCode {
	
	//两次密码不相同
	final int TWICE_PASSWORD_DIFF = 0;
	//邮箱格式不对
	final int EMAIL_FORMAT_ERROR = 1;
	//电话格式不正确
	final int PHONE_FORMAT_ERROR = 2;
	//用户名已注册
	final int USER_NAME_USED = 3;
	//手机已经被注册
	final int PHONE_USED = 4;
	//邮箱已经被注册
	final int EMAIL_USED = 5;
	//注册成功
	final int REGISTER_SUCCESS = 6;
	//未知错误
	final int UNKONW_ERROR = 7;
	
	//显示对话框
	final int SHOW_DIALOG = 8;
	
	//显示更新对话框
	final int SHOW_UPDATE_DIALOG = 17;
	
	//密码修改成功
	final int PASSWORD_CHANGE_SUCCESS = 9;
	//密码修改失败, 请重试
	final int PASSWORD_CHANGE_ERROR = 10;
	
	//删除个数
	final int ADD_NUM = 11;
	//增加个娄
	final int SUB_NUM = 12;
	//回传的ordercar
	final int RETURN_ORDER_CAR = 13;
	
	//显示菜单
	final int DIS_ORDER_ITEM = 14;
	
	//意见发送成功
	final int SEND_OPINIONS_SUCCESS = 15;
	//意见发送失败
	final int SEND_OPINIONS_FAIL = 16;
	
	//安装apk
	final int INSTALL_APK = 18;
}
