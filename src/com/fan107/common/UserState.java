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
import com.fan107.data.UserInfo;
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
	
	/**
	 * 设置登录用户的id
	 * @param context
	 */
	public static void setLoginUserId(Context context, int userId) {
		SharedPreferences initData = context.getSharedPreferences("account", Context.MODE_PRIVATE);
		Editor mEditor = initData.edit();
		mEditor.putInt("userId", userId); // 登录状态
		mEditor.commit();	
	}
	
	/**
	 * 获取登录用户的id
	 * @param context
	 * @return
	 */
	public static int getLoginUserId(Context context) {
		SharedPreferences initData = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
		return initData.getInt("userId", -1);
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
			mDbHelper.close();
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
		
		if(result != null && Boolean.valueOf(result.getPropertyAsString(0))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static UserInfo getUserInfo(String username, String passwordMD5) {
		UserInfo mUser = null;
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
	
	private static UserInfo getUser(SoapObject userInfo) {
		UserInfo mUser = new UserInfo();
		for(int i=0; i<userInfo.getPropertyCount(); i++) {
			SoapObject soapChilds = (SoapObject) userInfo.getProperty(i);
			
			int userId = WebServiceUtil.getSoapObjectInt(soapChilds, "Id");
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
			
			mUser.setUserid(userId);
			mUser.setUsergroup(userGroup);
			mUser.setUsername(userName);
			mUser.setNickname(nickName);
			mUser.setGender(gender);
			mUser.setBirthday(birthday);
			mUser.setTotalpoint(totalPoint);
			mUser.setCurrentpoint(currentPoint);
			mUser.setUserpoint(usePoint);
			mUser.setSpreadcount(spreadCount);
			mUser.setMobile(mobile);
			mUser.setEmail(email);
			mUser.setAddress(address);
			mUser.setUtype(utype);
			mUser.setShopid(shopId);
			mUser.setAddtime(addTime);													
		}	
		return mUser;
	}
	
	public static void saveUserInfoToDB(Context context, UserInfo userInfo) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put("userid", userInfo.getUserid());
		contentValues.put("usergroup", userInfo.getUsergroup());
		contentValues.put("username", userInfo.getUsername());
		contentValues.put("nickname", userInfo.getNickname());
		contentValues.put("gender", userInfo.getGender());
		contentValues.put("birthday", userInfo.getBirthday());
		contentValues.put("totalpoint", userInfo.getTotalpoint());
		contentValues.put("currentpoint", userInfo.getCurrentpoint());
		contentValues.put("usepoint", userInfo.getUserpoint());
		contentValues.put("spreadcount", userInfo.getSpreadcount());
		contentValues.put("mobile", userInfo.getMobile());
		contentValues.put("email", userInfo.getEmail());
		contentValues.put("address", userInfo.getAddress());
		contentValues.put("utype", userInfo.getUtype());
		contentValues.put("shopid", userInfo.getShopid());
		contentValues.put("addtime", userInfo.getAddtime());
		
		DBHelper dbHelper = new DBHelper(context);
		dbHelper.insertOrUpdate(DBHelper.USER_TABLE_NAME, contentValues, "username", "String");
		dbHelper.close();
	}
	
	public static UserInfo getUserInfoFromDB(Context context, int userid) {
		UserInfo userInfo = null;
		DBHelper dbHelper = new DBHelper(context);
		
		Cursor cursor = dbHelper.query("select * from " + DBHelper.USER_TABLE_NAME + " where userid=" + userid );
		if(cursor.moveToFirst()) {
			userInfo = new UserInfo();
			userInfo.setUserid(userid);
			userInfo.setUsergroup(cursor.getInt(cursor.getColumnIndex("usergroup")));
			userInfo.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			userInfo.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
			userInfo.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
			userInfo.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
			userInfo.setTotalpoint(cursor.getFloat(cursor.getColumnIndex("totalpoint")));
			userInfo.setCurrentpoint(cursor.getFloat(cursor.getColumnIndex("currentpoint")));
			userInfo.setUserpoint(cursor.getFloat(cursor.getColumnIndex("usepoint")));
			userInfo.setSpreadcount(cursor.getInt(cursor.getColumnIndex("spreadcount")));
			userInfo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			userInfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			userInfo.setUtype(cursor.getInt(cursor.getColumnIndex("utype")));
			userInfo.setShopid(cursor.getInt(cursor.getColumnIndex("shopid")));
			userInfo.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
		}
		
		dbHelper.close();
		return userInfo;
	}
	
	public static UserInfo upDateUserInfo(Context context, UserInfo userInfo) {
		DBHelper dbHelper = new DBHelper(context);
		
		Cursor cursor = dbHelper.query("select * from " + DBHelper.USER_TABLE_NAME + " where userid=" + userInfo.getUserid() );
		if(cursor.moveToFirst()) {
			userInfo.setUserid(userInfo.getUserid());
			userInfo.setUsergroup(cursor.getInt(cursor.getColumnIndex("usergroup")));
			userInfo.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			userInfo.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
			userInfo.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
			userInfo.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
			userInfo.setTotalpoint(cursor.getFloat(cursor.getColumnIndex("totalpoint")));
			userInfo.setCurrentpoint(cursor.getFloat(cursor.getColumnIndex("currentpoint")));
			userInfo.setUserpoint(cursor.getFloat(cursor.getColumnIndex("usepoint")));
			userInfo.setSpreadcount(cursor.getInt(cursor.getColumnIndex("spreadcount")));
			userInfo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			userInfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			userInfo.setUtype(cursor.getInt(cursor.getColumnIndex("utype")));
			userInfo.setShopid(cursor.getInt(cursor.getColumnIndex("shopid")));
			userInfo.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
		}
		
		dbHelper.close();
		return userInfo;
	}
}
