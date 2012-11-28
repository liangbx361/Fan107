package com.fan107.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.fan107.R;
import com.fan107.data.UserInfo;
import com.lbx.templete.ActivityTemplete;

public class MemberInformationActivity extends Activity implements ActivityTemplete, OnCheckedChangeListener{
	private TextView usernameText;
	private EditText emailText;
	private EditText nicknameText;
	private RadioGroup sexRadio;
	private EditText phoneText;
	private EditText birthdayText;
	
	private UserInfo mUserInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info_layout);
		
		findWidget();
		setWidgetListenter();
		setWidgetAttribute();
	}

	public void findWidget() {
		usernameText = (TextView) findViewById(R.id.username);
		emailText = (EditText) findViewById(R.id.email);
		nicknameText = (EditText) findViewById(R.id.nickname);
		sexRadio = (RadioGroup) findViewById(R.id.sex_radiogroup);
		phoneText = (EditText) findViewById(R.id.phone);
		birthdayText = (EditText) findViewById(R.id.birthday);
	}

	public void setWidgetListenter() {

	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		Intent mIntent = getIntent();
		mUserInfo = (UserInfo) mIntent.getSerializableExtra("userInfo");
		usernameText.setText(mUserInfo.getUsername());
		emailText.setText(mUserInfo.getEmail());
		nicknameText.setText(mUserInfo.getNickname());
		
		if(mUserInfo.getGender() == 0) {
		sexRadio.check(R.id.sex_male_Radio);
		} else {
			sexRadio.check(R.id.sex_female_radio);
		}
		
		phoneText.setText(mUserInfo.getMobile());
		birthdayText.setText(mUserInfo.getBirthday());
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
	}

}
