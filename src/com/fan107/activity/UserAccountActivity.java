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
	RelativeLayout order_layout; // �ҵĶ���
	RelativeLayout change_my_password; // �޸�����
	RelativeLayout my_send_address; // �Ͳ͵�ַ
	RelativeLayout share_config_layout; // ϵͳ����
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
		order_layout = (RelativeLayout) findViewById(R.id.order_layout);
		change_my_password = (RelativeLayout) findViewById(R.id.change_my_password);
		my_send_address = (RelativeLayout) findViewById(R.id.my_send_address);
		share_config_layout = (RelativeLayout) findViewById(R.id.share_config_layout);
		uname = (TextView) findViewById(R.id.uname);
	}

	@Override
	public void setWidgetListenter() {
		login.setOnClickListener(this);
		order_layout.setOnClickListener(this);
		change_my_password.setOnClickListener(this);
		my_send_address.setOnClickListener(this);
		share_config_layout.setOnClickListener(this);
	}

	@Override
	public void setWidgetPosition() {

	}

	@Override
	public void setWidgetAttribute() {
		if (loginState) {
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
			if (loginState) {
				Editor mEditor = userData.edit();
				mEditor.putBoolean("loginState", false);
				mEditor.commit();
			} else {
				Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
			}
			break;

		// �ҵĶ���
		case R.id.order_layout:
			break;

		// �޸�����
		case R.id.change_my_password:
			break;

		// �Ͳ͵�ַ
		case R.id.my_send_address:
			break;

		// ϵͳ����
		case R.id.share_config_layout:
			break;
		}

	}

	private void getLoginState() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		loginState = userData.getBoolean("loginState", false);
	}

	private String getUserName() {
		userData = this.getSharedPreferences("account", MODE_PRIVATE);
		return userData.getString("userName", "defalut");
	}

}
