package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.helper.MD5;
import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.db.DBHelper;
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

	private EditText oldPasswordView;
	private EditText newPasswordView;
	private EditText determineNewPasswordView;
	private Button changePassword;
	
	private String mUserName;
	private String mOldPassword;
	private String mNowPassword;
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_password);

		findWidget();
		setWidgetListenter();
		setWidgetAttribute();
		
		Intent mIntent = getIntent();
		mUserName = mIntent.getStringExtra("username");
		DBHelper dbHelper = new DBHelper(this);
		dbHelper.query(DBHelper.USER_LOGIN_TABLE_NAME, new String[]{"userpass"}, null);
		Cursor cursor = dbHelper.query("select userpass from " + DBHelper.USER_LOGIN_TABLE_NAME + " where username='" + mUserName + "'");
		cursor.moveToFirst();
		mOldPassword = cursor.getString(0);
		dbHelper.close();
	}

	public void findWidget() {
		oldPasswordView = (EditText) findViewById(R.id.et_old_password);
		newPasswordView = (EditText) findViewById(R.id.et_new_password);
		determineNewPasswordView = (EditText) findViewById(R.id.et_determine_new_password);

		changePassword = (Button) findViewById(R.id.btn_change_password);
	}

	public void setWidgetListenter() {
		changePassword.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		initProgressDialog(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_password:
			if(checkPassword()) {
				new ChangePasswordThread().start();
			}
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what) {
			case MessageCode.SHOW_DIALOG:
				mProgressDialog.show();
				break;			
				
			case MessageCode.PASSWORD_CHANGE_SUCCESS:
				ToastHelper.showToastInBottom(ChangePasswordActivity.this, MESSAGE_6, 0);
				onBackPressed();
				break;
				
			case MessageCode.PASSWORD_CHANGE_ERROR:
				ToastHelper.showToastInBottom(ChangePasswordActivity.this, MESSAGE_7, 0);
				break;
			}
		}		
	};

	private boolean checkPassword() {
		boolean state = false;
		
		String oldpassworddStr = oldPasswordView.getText().toString();
		String newPasswordStr = newPasswordView.getText().toString();
		String determineNewPasswordStr = determineNewPasswordView.getText().toString();

		if (!NetWorkCheck.checkNetWorkStatus(this)) {
			ToastHelper.showToastInBottom(this, MESSAGE_4);
		} else {
			oldpassworddStr = MD5.generateMD5(oldpassworddStr);
			if(!oldpassworddStr.equals(mOldPassword)) {
				ToastHelper.showToastInBottom(this, MESSAGE_2);
			} else if (oldpassworddStr == null || oldpassworddStr.length() < 6
					|| newPasswordStr == null || newPasswordStr.length() < 6
					|| determineNewPasswordStr == null
					|| determineNewPasswordStr.length() < 6) {
				ToastHelper.showToastInBottom(this, MESSAGE_1);

			} else if (!newPasswordStr.equals(determineNewPasswordStr)) {				
				ToastHelper.showToastInBottom(this, MESSAGE_3);	
				
			} else if (oldpassworddStr.equals(MD5.generateMD5(newPasswordStr))) {				
				ToastHelper.showToastInBottom(this, MESSAGE_5);
				
			} else {
				mNowPassword = MD5.generateMD5(newPasswordStr);
				state = true;
			}
		}
		
		return state;
	}
	
	private class ChangePasswordThread extends Thread {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOG);
			
			//修改服务器的密码 
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", mUserName);
			params.put("password", mNowPassword);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.CHANAGER_PASSWORD_METHOD, params);
			
			if(result.getPropertyAsString(0).equals("ok")) {
				//保存至本地数据库中
				String passwordMD5 = SoapObjectHelper.getSoapObjectResultString(result, WebServiceConfig.CHANAGER_PASSWORD_METHOD);
				ContentValues contentValues = new ContentValues();
				contentValues.put("userpass", passwordMD5);
				DBHelper mDbHelper = new DBHelper(ChangePasswordActivity.this);
				mDbHelper.updateTable(DBHelper.USER_LOGIN_TABLE_NAME, contentValues, "username = '" + mUserName + "'" , null);				
				mHandler.sendEmptyMessage(MessageCode.PASSWORD_CHANGE_SUCCESS);
				
			} else {
				mHandler.sendEmptyMessage(MessageCode.PASSWORD_CHANGE_ERROR);
			}
			
			mProgressDialog.dismiss();
		}
		
	}
		
	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("正在注册中");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
	}
}
