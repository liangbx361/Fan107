package com.fan107.common;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.fan107.activity.LoginActivity;
import com.fan107.activity.SearchActivity;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.User;
import com.fan107.data.UserLogin;
import com.fan107.db.DBHelper;
import com.widget.helper.ToastHelper;

import common.connection.net.NetWorkCheck;
import common.connection.net.WebServiceUtil;

public class UserState {

	private static final String TAG = "UserState";
	
	public static final int HANDLER_LOGIN_SUCCESS = 10;
	public static final int HANDLER_AUTO_LOGIN_SUCCESS = 11;
	public static final int HANDLER_LOGIN_FAIL = 12;
	public static final int HANDLER_NO_NETWORK = 13;
	
	public static final String LOGIN_SUCCESS = "登录成功";
	public static final String AUTO_LOGIN_SUCCESS = "自动登录成功";
	public static final String LOGIN_FAIL = "登录失败";
	public static final String NO_NETWORK = "无网络连接, 请检查网络";
		
	/**
	 * 设置登录状态
	 * @param context
	 * @param isLogin
	 */
	public static void setLoginState(Context context, boolean isLogin) {
		SharedPreferences initData = context.getSharedPreferences("account", Context.MODE_PRIVATE);
		Editor mEditor = initData.edit();
		mEditor.putBoolean("loginState", isLogin); // 登录状态
		mEditor.commit();
	}
	
	/**
	 * 获取用户的登录状态
	 * @param context
	 * @return
	 */
	public static boolean getLoginState(Context context) {
		SharedPreferences initData = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
		return initData.getBoolean("loginState", false);
	}
	
	public static boolean autoLogin(Context context, UserLogin mLogin, Handler mHandler) {
		boolean isAutoLogin = false;
		if(NetWorkCheck.checkNetWorkStatus(context)) {
			DBHelper mDbHelper = new DBHelper(context);
			Cursor cursor = mDbHelper.query("select username, userpass from " + DBHelper.USER_LOGIN_TABLE_NAME + " where auto_login=1");
			if(cursor.moveToFirst()) {
				String username = cursor.getString(0);
				String userpass = cursor.getString(1);
				
				mLogin.setUserName(username);
				mLogin.setPasswdMD5(userpass);
				
				if(getLoginStateByWebService(username, userpass)) {
					isAutoLogin = true;
					setLoginState(context, true);
					mHandler.sendMessage(mHandler.obtainMessage(HANDLER_AUTO_LOGIN_SUCCESS, AUTO_LOGIN_SUCCESS));
				} else {
					mHandler.sendMessage(mHandler.obtainMessage(HANDLER_LOGIN_FAIL, LOGIN_FAIL));
				}				
			} 
		} else {
			mHandler.sendMessage(mHandler.obtainMessage(HANDLER_NO_NETWORK, NO_NETWORK));;
		}
		
		return isAutoLogin;
	}
	
	private static boolean getLoginStateByWebService(String userName, String password) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		params.put("password", password);
		String url = WebServiceConfig.url + WebServiceConfig.LOGIN_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.AUTO_LOGIN_METHOD, params);
		
		if(Boolean.valueOf(result.getPropertyAsString(0))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static User getUserInfo(String username, String passwordMD5) {
		User mUser = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", passwordMD5);
		String url = WebServiceConfig.url + WebServiceConfig.LOGIN_WEB_SERVICE;
		SoapObject userInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_LOGIN_USER_INFO_METHOD, params);
		
		if(userInfo != null) {
			mUser = getUser(userInfo);
		} else {
			Log.d(TAG, "用户信息获取失败!");
		}
		
		return mUser;
		
	}
	
	private static User getUser(SoapObject userInfo) {
		User mUser = new User();
		for(int i=0; i<userInfo.getPropertyCount(); i++) {
			SoapObject soapChilds = (SoapObject) userInfo.getProperty(i);
			
			int userGroup = WebServiceUtil.getSoapObjectInt(soapChilds, "UserGroup");
			String userName = WebServiceUtil.getSoapObjectString(soapChilds, "UserName");
			String nickName = WebServiceUtil.getSoapObjectString(soapChilds, "NickName");
			int gender =  WebServiceUtil.getSoapObjectInt(soapChilds, "Gender");
			String birthday = WebServiceUtil.getSoapObjectString(soapChilds, "Birthday");
			float totalPoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "TotalPoint");
			float currentPoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "CurrentPoint");
			float usePoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "UsePoint");
			int spreadCount = WebServiceUtil.getSoapObjectInt(soapChilds, "SpreadCount");
			String mobile = WebServiceUtil.getSoapObjectString(soapChilds, "Mobile");
			String email = WebServiceUtil.getSoapObjectString(soapChilds, "Email");
			String address = WebServiceUtil.getSoapObjectString(soapChilds, "Address");
			int utype = WebServiceUtil.getSoapObjectInt(soapChilds, "Utype");
			int shopId = WebServiceUtil.getSoapObjectInt(soapChilds, "ShopId");
			String addTime = WebServiceUtil.getSoapObjectString(soapChilds, "AddTime");
			
			mUser.setUsergroup(userGroup);
			mUser.setUsername(userName);
			mUser.setNickname(nickName);
			mUser.setGender(gender);
			mUser.setBirthday(birthday);
			mUser.setTotalpoint(totalPoint);
			mUser.setCurrentpoint(currentPoint);
			mUser.setUsepoint(usePoint);
			mUser.setSpreadcount(spreadCount);
			mUser.setMobile(mobile);
			mUser.setEmail(email);
			mUser.setAddress(address);
			mUser.setUtype(utype);
			mUser.setShopid(shopId);
			mUser.setAddtime(addTime);
										
//			ContentValues contentValues = new ContentValues();
//			contentValues.put("usergroup", userGroup);
//			contentValues.put("username", userName);
//			contentValues.put("nickname", nickName);
//			contentValues.put("gender", gender);
//			contentValues.put("birthday", birthday);
//			contentValues.put("totalpoint", totalPoint);
//			contentValues.put("currentpoint", currentPoint);
//			contentValues.put("usepoint", usePoint);
//			contentValues.put("spreadcount", spreadCount);
//			contentValues.put("mobile", mobile);
//			contentValues.put("email", email);
//			contentValues.put("address", address);
//			contentValues.put("utype", utype);
//			contentValues.put("shopid", shopId);
//			contentValues.put("addtime", addTime);
		}	
		return mUser;
	}
}
