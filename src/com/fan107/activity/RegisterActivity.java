package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.common.helper.FormatCheck;
import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RegisterActivity extends Activity implements ActivityTemplete, OnClickListener{
		
	private EditText userNameView;
	private EditText phoneView;
	private EditText emailView;
	private EditText passwordView;
	private EditText passwordConfirmView;
	private LinearLayout regButton;
	
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register_activity);
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		userNameView = (EditText) findViewById(R.id.reg_username);
		phoneView = (EditText) findViewById(R.id.reg_phone);
		emailView = (EditText) findViewById(R.id.reg_email);
		passwordView = (EditText) findViewById(R.id.reg_password);
		passwordConfirmView = (EditText) findViewById(R.id.reg_password_confirm);
		regButton = (LinearLayout) findViewById(R.id.reg_confirm);
	}

	public void setWidgetListenter() {
		regButton.setOnClickListener(this);
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		initProgressDialog(this);
	}
	
	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.reg_confirm:
				String userName = userNameView.getText().toString();
				String phone = phoneView.getText().toString();
				String email = emailView.getText().toString();
				String password = passwordView.getText().toString();
				String passwordConfirm = passwordConfirmView.getText().toString();
				
				if(FormatCheck.checkPhoto(phone) == null) {
					mHandler.sendEmptyMessage(MessageCode.PHONE_FORMAT_ERROR);
					
				} else if(!FormatCheck.checkEmail(email)) {
					
					mHandler.sendEmptyMessage(MessageCode.EMAIL_FORMAT_ERROR);
				} else if(!password.equals(passwordConfirm)) {
					
					mHandler.sendEmptyMessage(MessageCode.TWICE_PASSWORD_DIFF);
				} else {
					
					new ConfirmThread(userName, phone, email, password).start();
				}
				break;
		}
		
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MessageCode.SHOW_DIALOT:
				mProgressDialog.show();
				break;
				
			case MessageCode.TWICE_PASSWORD_DIFF:
				ToastHelper.showToastInBottom(RegisterActivity.this, "两次密码不同", 0);
				break;
				
			case MessageCode.PHONE_FORMAT_ERROR:
				ToastHelper.showToastInBottom(RegisterActivity.this, "手机号错误", 0);
				break;
				
			case MessageCode.EMAIL_FORMAT_ERROR:
				ToastHelper.showToastInBottom(RegisterActivity.this, "邮箱格式错误", 0);
				break;
										
			case MessageCode.USER_NAME_USED:
				ToastHelper.showToastInBottom(RegisterActivity.this, "用户名已被使用", 0);
				break;
				
			case MessageCode.PHONE_USED:
				ToastHelper.showToastInBottom(RegisterActivity.this, "手机号已被使用", 0);
				break;
				
			case MessageCode.EMAIL_USED:
				ToastHelper.showToastInBottom(RegisterActivity.this, "邮箱已被使用", 0);
				break;
				
			case MessageCode.REGISTER_SUCCESS:
				ToastHelper.showToastInBottom(RegisterActivity.this, "注册成功", 0);
				onBackPressed();
				break;
				
			case MessageCode.UNKONW_ERROR:
				ToastHelper.showToastInBottom(RegisterActivity.this, "未知错误", 0);
				break;
			}
		}
	};
	
	private class ConfirmThread extends Thread {
		
		String username;
		String phone;
		String email;
		String password;
		
		public ConfirmThread(String username, String phone, String email, String password) {
			this.username = username;
			this.phone = phone;
			this.email = email;
			this.password = password;
		}
		
		@Override
		public void run() {
			mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOT);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", username);
			data.put("mobile", phone);
			data.put("email", email);
			data.put("password", password);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.REGISTER_ACCOUNT_METHOD, data);
			
			int code = Integer.valueOf(result.getPropertyAsString(0));
			switch(code) {
			case 0:
				mHandler.sendEmptyMessage(MessageCode.REGISTER_SUCCESS);
				break;
			case 1:
				mHandler.sendEmptyMessage(MessageCode.USER_NAME_USED);
				break;
			case 2:
				mHandler.sendEmptyMessage(MessageCode.PHONE_USED);
				break;
			case 3:
				mHandler.sendEmptyMessage(MessageCode.EMAIL_USED);
				break;
				
			default:
				mHandler.sendEmptyMessage(MessageCode.UNKONW_ERROR);
				break;
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
