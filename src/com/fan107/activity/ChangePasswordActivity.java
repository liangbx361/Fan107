package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.lbx.db.DBHelper;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

import common.connection.net.NetWorkCheck;
import common.connection.net.SoapObjectHelper;
import common.connection.net.WebServiceUtil;

public class ChangePasswordActivity extends Activity implements
		ActivityTemplete, OnClickListener {
	private static final String MESSAGE_1 = "密码长度不够6位";
	private static final String MESSAGE_2 = "原密码输入错误";
	private static final String MESSAGE_3 = "两次密码输入不同";
	private static final String MESSAGE_4 = "无网络连接, 无法修改密码";
	private static final String MESSAGE_5 = "与原密码相同, 无须修改";
	private static final String MESSAGE_6 = "密码修改成功";
	private static final String MESSAGE_7 = "密码修改失败, 请重试";

	private EditText oldPassword;
	private EditText newPassword;
	private EditText determineNewPassword;
	private Button changePassword;
	
	private String userName;
	private String nowPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_password);

		findWidget();
		setWidgetListenter();
		
		Intent mIntent = getIntent();
		userName = mIntent.getStringExtra("userName");
	}

	public void findWidget() {
		oldPassword = (EditText) findViewById(R.id.et_old_password);
		newPassword = (EditText) findViewById(R.id.et_new_password);
		determineNewPassword = (EditText) findViewById(R.id.et_determine_new_password);

		changePassword = (Button) findViewById(R.id.btn_change_password);
	}

	public void setWidgetListenter() {
		changePassword.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_password:
			if(checkPassword()) {
				//修改服务器的密码 
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("username", userName);
				params.put("password", nowPassword);
				String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
				SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.CHANAGER_PASSWORD_METHOD, params);
				
				if(result != null) {
					//保存至本地数据库中
					String passwordMD5 = SoapObjectHelper.getSoapObjectResultString(result, WebServiceConfig.CHANAGER_PASSWORD_METHOD);
					ContentValues contentValues = new ContentValues();
					contentValues.put("userpass", passwordMD5);
					DBHelper mDbHelper = new DBHelper(this);
					mDbHelper.updateTable(DBHelper.USER_LOGIN_TABLE_NAME, contentValues, "username = '" + userName + "'" , null);
					ToastHelper.showToastInBottom(this, MESSAGE_6);
					
					Intent mIntent = new Intent(this, UserAccountActivity.class);
					startActivity(mIntent);
				} else {
					ToastHelper.showToastInBottom(this, MESSAGE_7);
				}
			}
			break;
		}
	}

	private boolean checkPassword() {
		boolean state = false;
		
		String oldpassworddStr = oldPassword.getText().toString();
		String newPasswordStr = newPassword.getText().toString();
		String determineNewPasswordStr = determineNewPassword.getText()
				.toString();

		if (!NetWorkCheck.checkNetWorkStatus(this)) {
			ToastHelper.showToastInBottom(this, MESSAGE_4);
			
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", userName);
			params.put("password", oldpassworddStr);
			String url = WebServiceConfig.url + WebServiceConfig.LOGIN_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.LOGIN_METHOD, params);
			
			if(result == null) {
				ToastHelper.showToastInBottom(this, MESSAGE_2);
				
			} else if (oldpassworddStr == null || oldpassworddStr.length() < 6
					|| newPasswordStr == null || newPasswordStr.length() < 6
					|| determineNewPasswordStr == null
					|| determineNewPasswordStr.length() < 6) {
				ToastHelper.showToastInBottom(this, MESSAGE_1);

			} else if (!newPasswordStr.equals(determineNewPasswordStr)) {				
				ToastHelper.showToastInBottom(this, MESSAGE_3);				
			} else if (oldpassworddStr.equals(newPasswordStr)) {				
				ToastHelper.showToastInBottom(this, MESSAGE_5);
			} else {
				nowPassword = newPasswordStr;
				state = true;
			}
		}
		
		return state;
	}
}
