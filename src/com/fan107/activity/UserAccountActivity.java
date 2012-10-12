package com.fan107.activity;

import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserAccountActivity extends Activity implements ActivityTemplete,
		OnClickListener {
	
	Button login;
	RelativeLayout logout_layout;
	RelativeLayout login_layout;
	TextView uname;
	
	boolean loginState;
	SharedPreferences userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_user_account);
		getLoginState();
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	@Override
	public void findWidget() {
		login = (Button) findViewById(R.id.login);
		logout_layout = (RelativeLayout) findViewById(R.id.logout_layout);
		login_layout = (RelativeLayout) findViewById(R.id.login_layout);
		uname = (TextView) findViewById(R.id.uname);
	}

	@Override
	public void setWidgetListenter() {
		login.setOnClickListener(this);
	}

	@Override
	public void setWidgetPosition() {

	}

	@Override
	public void setWidgetAttribute() {
		if(loginState) {
			logout_layout.setVisibility(View.GONE);
			login.setText(R.string.user_loginout);
			uname.setText(getUserName());
		} else {
			login_layout.setVisibility(View.GONE);
			login.setText(R.string.user_login);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login:
			if(loginState) {
				Editor mEditor = userData.edit();
				mEditor.putBoolean("loginState", false);
				mEditor.commit();
			} else {
				Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
			}
			break;
		}

	}
	
	private void getLoginState() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		loginState =  userData.getBoolean("loginState", false);
	}
	
	private String getUserName() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		return userData.getString("userName", "defalut");
	}

}
