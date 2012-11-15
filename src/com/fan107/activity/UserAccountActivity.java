package com.fan107.activity;

import com.fan107.R;
import com.lbx.db.DBHelper;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserAccountActivity extends Activity implements ActivityTemplete,
		OnClickListener {
	
	Button login;
	Button returnList;
	RelativeLayout logout_layout;
	RelativeLayout login_layout;
	TextView uname;
	TextView point;
	
	RelativeLayout userInfoLayout;
	RelativeLayout chanagePasswordLayout;
	RelativeLayout sendAddressLayout;
	
	private String userName;
	private String nickName;
	private String passwordMD5;
	private float currentPointValue;
	
	boolean loginState;
	SharedPreferences userData;
	
	DBHelper mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_user_account);
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		
		mDbHelper = new DBHelper(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getLoginState();
		setWidgetAttribute();
	}


	public void findWidget() {
		login = (Button) findViewById(R.id.login);
		returnList = (Button) findViewById(R.id.returnList);
		logout_layout = (RelativeLayout) findViewById(R.id.logout_layout);
		login_layout = (RelativeLayout) findViewById(R.id.login_layout);
		uname = (TextView) findViewById(R.id.uname);
		point = (TextView) findViewById(R.id.money);
		
		chanagePasswordLayout = (RelativeLayout) findViewById(R.id.change_password);
	}

	public void setWidgetListenter() {
		login.setOnClickListener(this);
		returnList.setOnClickListener(this);
		chanagePasswordLayout.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		if(loginState) {
			//显示为登出按钮
			login_layout.setVisibility(View.VISIBLE);
			logout_layout.setVisibility(View.GONE);
			login.setText(R.string.user_loginout);
			uname.setText(nickName);
			point.setText(currentPointValue + "");
		} else {
			login_layout.setVisibility(View.GONE);
			logout_layout.setVisibility(View.VISIBLE);
			login.setText(R.string.user_login);
		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login:
			if(loginState) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("auto_login", 0);
				mDbHelper.updateTable(DBHelper.USER_LOGIN_TABLE_NAME, contentValues, "auto_login=1", null);
			} 
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			break;
			
		case R.id.returnList:
			Intent returnIntent = new Intent(this, SearchActivity.class);
			startActivity(returnIntent);
			break;
		
		case R.id.change_password:
			if(loginState) {
				Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
				changePasswordIntent.putExtra("userName", userName);
				startActivity(changePasswordIntent);		
			}
			break;
		}

	}
	
	private void getLoginState() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		loginState =  userData.getBoolean("loginState", false);
		
		//取出数据库中的用户名和密码数据，进行登录验证
		Cursor cursor = mDbHelper.query("select * from " + DBHelper.USER_LOGIN_TABLE_NAME + " where auto_login=1");
		if(cursor.moveToFirst()) {
			loginState = true;
			userName = cursor.getString(2);
			passwordMD5 = cursor.getString(3);
			getUserInfo();
		} else {
			loginState = false;
		}
	}
	
	private void getUserInfo() {
		Cursor cursor = mDbHelper.query("select * from " + DBHelper.USER_TABLE_NAME + " where username ='" + userName +"'");
		if(cursor.moveToFirst()) {
			nickName  = cursor.getString(3);
			currentPointValue = cursor.getFloat(7);
		}
	}
	
	private String getUserName() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		return userData.getString("userName", "defalut");
	}

}
