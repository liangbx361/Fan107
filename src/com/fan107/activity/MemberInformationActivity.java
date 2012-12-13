package com.fan107.activity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.fan107.config.WebServiceConfig;
import com.fan107.data.UserInfo;
import com.fan107.db.DBHelper;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;
import common.connection.net.WebServiceUtil;

public class MemberInformationActivity extends Activity implements
		ActivityTemplete, OnCheckedChangeListener, OnClickListener {
	
	private static final int DIALOG_SHOW = 100;
	
	private TextView usernameText;
	private EditText emailText;
	private EditText nicknameText;
	private RadioGroup sexRadio;
	private EditText phoneText;
	private TextView birthdayText;
	private Button confirmBtn;

	private UserInfo mUserInfo;

	private int gender;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	public ProgressDialog mProgressDialog;
	MyHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info_layout);
		
		mHandler = new MyHandler(this);

		findWidget();
		setWidgetListenter();
		setWidgetAttribute();
	}

	public void findWidget() {
		usernameText = (TextView) findViewById(R.id.username);
		emailText = (EditText) findViewById(R.id.email);
		nicknameText = (EditText) findViewById(R.id.nickname);
		sexRadio = (RadioGroup) findViewById(R.id.sex_radiogroup);
		phoneText = (EditText) findViewById(R.id.member_phone);
		birthdayText = (TextView) findViewById(R.id.birthday);
		confirmBtn = (Button) findViewById(R.id.member_info_confirm_btn);
	}

	public void setWidgetListenter() {
		birthdayText.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		sexRadio.setOnCheckedChangeListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		Intent mIntent = getIntent();
		mUserInfo = (UserInfo) mIntent.getSerializableExtra("userInfo");
		usernameText.setText(mUserInfo.getUsername());
		emailText.setText(mUserInfo.getEmail());
		nicknameText.setText(mUserInfo.getNickname());
		gender = mUserInfo.getGender();
		
		if (mUserInfo.getGender() == 0) {
			sexRadio.check(R.id.sex_male_Radio);			
		} else {
			sexRadio.check(R.id.sex_female_radio);
		}

		phoneText.setText(mUserInfo.getMobile());
		birthdayText.setText(mUserInfo.getBirthday());
		
		initProgressDialog(this);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId == R.id.sex_male_Radio) gender = 0;
		else gender = 1;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.birthday:
			getBirthdayValue(birthdayText.getText().toString());
			showDialog(0);
			break;

		case R.id.member_info_confirm_btn:
			if (check()) {
				new Thread() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(DIALOG_SHOW);
						if(sendMemberInfo())
							onBackPressed();
						mProgressDialog.dismiss();
					}
				}.start();
			}
			break;
		}
	}
	
	static class MyHandler extends Handler {
		
		WeakReference<MemberInformationActivity> mActivity;
		
		MyHandler(MemberInformationActivity activity) {  
            mActivity = new WeakReference<MemberInformationActivity>(activity);  
		}  

		@Override
		public void handleMessage(Message msg) {
			MemberInformationActivity activity = mActivity.get();
			switch(msg.what) {
			case DIALOG_SHOW:
				activity.mProgressDialog.show();
				break;
			}
		}
	};
	
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
		if (!FormatCheck.checkEmail(emailText.getText().toString())) {
			ToastHelper.showToastInBottom(this, "邮箱格式不正确", 0);
			return false;
				
		} else if(FormatCheck.checkPhoto(phoneText.getText().toString()) == null) {
			ToastHelper.showToastInBottom(this, "电话号码格式不正确", 0);
			return false;
		}
		
		return true;
	}
	
	private boolean sendMemberInfo() {
		boolean isSuccess = false;
		
		String phone;
		String nickName;
		String email;
		String birthday;
		
		JSONObject orderObject = new JSONObject();
		phone = phoneText.getText().toString();
		nickName = nicknameText.getText().toString();
		email = emailText.getText().toString();
		birthday = birthdayText.getText().toString();
		
		try {
			orderObject.put("id", mUserInfo.getUserid());
			orderObject.put("usergroup", mUserInfo.getUsergroup());
			orderObject.put("email", emailText.getText().toString());
			orderObject.put("nickName", nicknameText.getText().toString());
			orderObject.put("phone", phoneText.getText().toString());
			orderObject.put("gender", gender);
			orderObject.put("birthday", birthdayText.getText().toString());
			orderObject.put("address", mUserInfo.getAddress());
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("values", orderObject.toString());
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.CHANAGER_MEMBER_INFO_METHOD, data);
			
			if(result.getPropertyAsString(0).equals("ok")) {
				
				mUserInfo.setGender(gender);
				mUserInfo.setMobile(phone);
				mUserInfo.setNickname(nickName);
				mUserInfo.setEmail(email);
				mUserInfo.setBirthday(birthday);

				//更新本地数据库
				ContentValues content = new ContentValues();
				content.put("gender", gender);
				content.put("nickname", nickName);
				content.put("mobile", phone);
				content.put("email", email);
				content.put("birthday", birthday);
				DBHelper mDbHelper = new DBHelper(this);
				mDbHelper.updateTable(DBHelper.USER_TABLE_NAME, content, "userid="+mUserInfo.getUserid(), null);
				mDbHelper.close();
				
				isSuccess = true;	
			} else {
				ToastHelper.showToastInBottom(this, "信息提交失败, 请重试", 0);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("数据保存中");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
	}
	
}
