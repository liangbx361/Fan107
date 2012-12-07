package com.fan107.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.common.helper.FormatCheck;
import com.dominicsayers.isemail.IsEMail;
import com.dominicsayers.isemail.IsEMailResult;
import com.dominicsayers.isemail.dns.DNSLookupException;
import com.fan107.R;
import com.fan107.data.UserInfo;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

public class MemberInformationActivity extends Activity implements
		ActivityTemplete, OnCheckedChangeListener, OnClickListener {
	private TextView usernameText;
	private EditText emailText;
	private EditText nicknameText;
	private RadioGroup sexRadio;
	private EditText phoneText;
	private TextView birthdayText;
	private Button confirmBtn;

	private UserInfo mUserInfo;

	private int mYear;
	private int mMonth;
	private int mDay;

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
		birthdayText = (TextView) findViewById(R.id.birthday);
		confirmBtn = (Button) findViewById(R.id.member_info_confirm_btn);
	}

	public void setWidgetListenter() {
		birthdayText.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		Intent mIntent = getIntent();
		mUserInfo = (UserInfo) mIntent.getSerializableExtra("userInfo");
		usernameText.setText(mUserInfo.getUsername());
		emailText.setText(mUserInfo.getEmail());
		nicknameText.setText(mUserInfo.getNickname());

		if (mUserInfo.getGender() == 0) {
			sexRadio.check(R.id.sex_male_Radio);
		} else {
			sexRadio.check(R.id.sex_female_radio);
		}

		phoneText.setText(mUserInfo.getMobile());
		birthdayText.setText(mUserInfo.getBirthday());
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.birthday:
			getBirthdayValue(birthdayText.getText().toString());
			showDialog(0);
			break;

		case R.id.member_info_confirm_btn:
			if (check()) {
				
			}
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		return new DatePickerDialog(this, mDateSetListener, mYear, mMonth - 1,
				mDay);
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		((DatePickerDialog) dialog).updateDate(mYear, mMonth - 1, mDay);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		StringBuilder birthdayBuilder = new StringBuilder();
		birthdayBuilder.append(mYear).append("-");
		if (mMonth < 10)
			birthdayBuilder.append("0");
		birthdayBuilder.append(mMonth).append("-");
		if (mDay < 10)
			birthdayBuilder.append("0");
		birthdayBuilder.append(mDay);

		birthdayText.setText(birthdayBuilder);
	}

	public void getBirthdayValue(String birthday) {

		if (!birthday.equals("")) {
			String[] value = birthday.split("-");
			mYear = Integer.valueOf(value[0]);
			mMonth = Integer.valueOf(value[1]);
			mDay = Integer.valueOf(value[2]);
		} else {
			mYear = 1980;
			mMonth = 1;
			mDay = 1;
		}
	}

	private boolean check() {
		FormatCheck formatCheck = new FormatCheck();
		try {
			if (!formatCheck.checkEmail(emailText.getText().toString())) {
				ToastHelper.showToastInBottom(this, "邮箱格式不正确", 0, 100);
				return false;
				
			} else if(formatCheck.checkPhoto(phoneText.getText().toString()) != null) {
				ToastHelper.showToastInBottom(this, "电话号码格式不正确", 0, 100);
				return false;
			}
			
		} catch (DNSLookupException e) {
			e.printStackTrace();
		}

		return true;
	}

}
