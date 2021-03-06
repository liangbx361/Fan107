package com.fan107.activity;

import com.fan107.R;
import com.fan107.common.UserState;
import com.fan107.data.UserInfo;
import com.fan107.db.DBHelper;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserAccountActivity extends Activity implements ActivityTemplete,
		OnClickListener {
	
	private static final String TAG = "UserAccountActivity";
	private static final String MESSAGE_1 = "请先登录";

	Button login;
	Button returnList;
	RelativeLayout logout_layout;
	RelativeLayout login_layout;
	LinearLayout order_layout; // 我的订单
	RelativeLayout my_send_address; // 送餐地址
	RelativeLayout share_config_layout; // 系统设置
	TextView uname;
	TextView point;
	
	RelativeLayout userInfoLayout;
	RelativeLayout chanagePasswordLayout;
	RelativeLayout sendAddressLayout;
	
	RelativeLayout opinionsLayout;
	RelativeLayout aboutLayout;
	
	private String userName;
	private String nickName;
	private String passwordMD5;
	private float currentPointValue;
	
	boolean loginState;
	SharedPreferences userData;
	
	DBHelper mDbHelper;
	
	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_user_account);

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		
		mDbHelper = new DBHelper(this);
		
		loginState = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setWidgetAttribute();
		mUserInfo = UserState.upDateUserInfo(this, mUserInfo);
	}


	public void findWidget() {
		login = (Button) findViewById(R.id.login);
		returnList = (Button) findViewById(R.id.returnList);
		logout_layout = (RelativeLayout) findViewById(R.id.logout_layout);
		login_layout = (RelativeLayout) findViewById(R.id.login_layout);
		order_layout = (LinearLayout) findViewById(R.id.order_layout);
		
		share_config_layout = (RelativeLayout) findViewById(R.id.share_config_layout);
		uname = (TextView) findViewById(R.id.uname);
		point = (TextView) findViewById(R.id.money);
		
		userInfoLayout = (RelativeLayout) findViewById(R.id.user_information);
		chanagePasswordLayout = (RelativeLayout) findViewById(R.id.change_password);
		my_send_address = (RelativeLayout) findViewById(R.id.my_send_address);
		
		opinionsLayout = (RelativeLayout) findViewById(R.id.user_account_opinion);
		aboutLayout = (RelativeLayout) findViewById(R.id.user_account_about);
		
	}

	public void setWidgetListenter() {
		login.setOnClickListener(this);
		returnList.setOnClickListener(this);
		chanagePasswordLayout.setOnClickListener(this);
		userInfoLayout.setOnClickListener(this);
		my_send_address.setOnClickListener(this);
		order_layout.setOnClickListener(this);
		
		opinionsLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		Intent mIntent = getIntent();
		mUserInfo = (UserInfo) mIntent.getSerializableExtra("userInfo");
		
		if(loginState) {
			//显示为登出按钮
			login_layout.setVisibility(View.VISIBLE);
			logout_layout.setVisibility(View.GONE);
			login.setText(R.string.user_loginout);
			uname.setText(mUserInfo.getUsername());
			point.setText(mUserInfo.getCurrentpoint() + "");
		} else {
			login_layout.setVisibility(View.GONE);
			logout_layout.setVisibility(View.VISIBLE);
			login.setText(R.string.user_login);
		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		//登出
		case R.id.login:
			if(loginState) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("auto_login", 0);
				mDbHelper.updateTable(DBHelper.USER_LOGIN_TABLE_NAME, contentValues, "auto_login=1", null);
			} 
			UserState.setLoginState(this, false);	//设置为登出
			UserState.setRefreshState(this, true); 	//首页需要重新刷新
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
			break;
			
		case R.id.returnList:
			onBackPressed();
			break;
			
		//个人资料
		case R.id.user_information:
			if(loginState) {
				Intent userInfoIntent = new Intent(this, MemberInformationActivity.class);
				userInfoIntent.putExtra("userInfo", mUserInfo);
				startActivity(userInfoIntent);		
			} else {
				ToastHelper.showToastInBottom(this, MESSAGE_1);
			}
			break;
		
		//修改密码
		case R.id.change_password:
			if(loginState) {
				Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
				changePasswordIntent.putExtra("username", mUserInfo.getUsername());
				startActivity(changePasswordIntent);		
			} else {
				ToastHelper.showToastInBottom(this, MESSAGE_1);
			}
			break;

		// 我的订单
		case R.id.order_layout:
			Intent orderIntent = new Intent(this, OrderListActivity.class);
			orderIntent.putExtra("userInfo", mUserInfo);
			startActivity(orderIntent);
			break;

		//送餐地址
		case R.id.my_send_address:
			Intent addressIntent = new Intent(this, RecevoirAddressActivity.class);
			addressIntent.putExtra("userId", mUserInfo.getUserid());
			startActivity(addressIntent);
			break;

		// 系统设置
		case R.id.share_config_layout:
			break;
			
		//意见反馈
		case R.id.user_account_opinion:
			Intent opinionIntent = new Intent(this, OpinionActivity.class);
			opinionIntent.putExtra("userInfo", mUserInfo);
			startActivity(opinionIntent);
			break;
			
		//关于我们
		case R.id.user_account_about:
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
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
			ToastHelper.showToastInBottom(this, MESSAGE_1);
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
