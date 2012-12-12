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

public class RegisterActivity extends Activity implements ActivityTemplete, OnClickListener{
		
	private EditText userNameView;
	private EditText phoneView;
	private EditText emailView;
	private EditText passwordView;
	private EditText passwordConfirmView;
	private Button regButton;
	
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
		regButton = (Button) findViewById(R.id.reg_confirm);
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
				} else if(password.equals(passwordConfirm)) {
					
					mHandler.sendEmptyMessage(MessageCode.TWICE_PASSWORD_DIFF);
				} else {
					
				}
				break;
		}
		
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MessageCode.TWICE_PASSWORD_DIFF:
				ToastHelper.showToastInBottom(RegisterActivity.this, "两次密码不同", 0, 100);
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
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", username);
			data.put("mobile", phone);
			data.put("email", email);
			data.put("password", password);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.REGISTER_ACCOUNT_METHOD, data);
			
			int code = Integer.valueOf(result.getPropertyAsString(0));
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
