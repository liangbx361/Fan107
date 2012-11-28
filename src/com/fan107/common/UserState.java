package com.fan107.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fan107.activity.LoginActivity;

public class UserState {
	
	/**
	 * ���õ�¼״̬
	 * @param context
	 * @param isLogin
	 */
	public static void setLoginState(Context context, boolean isLogin) {
		SharedPreferences initData = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
		Editor mEditor = initData.edit();
		mEditor.putBoolean("loginState", isLogin); // ��¼״̬
		mEditor.commit();
	}
	
	/**
	 * ��ȡ�û��ĵ�¼״̬
	 * @param context
	 * @return
	 */
	public static boolean getLoginState(Context context) {
		SharedPreferences initData = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
		return initData.getBoolean("loginState", false);
	}
}
