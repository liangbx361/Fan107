package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fan107.R;
import com.fan107.config.UrlConfig;

import common.connection.net.HttpClientUtil;
import common.connection.net.NetWorkCheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private static final int CHECK_STATE = 0;
	private static final int DIALOG_SHOW = 1;

	private Button registerButton;
	private LinearLayout loginButton;
	private EditText userNameText;
	private EditText passwordText;

	// private String userName;
	// private String password;

	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);

		findView();
		setListenter();
		initProgressDialog(this);
	}

	private void findView() {
		registerButton = (Button) findViewById(R.id.regbtn);
		loginButton = (LinearLayout) findViewById(R.id.loginbtn);
		userNameText = (EditText) findViewById(R.id.email);
		passwordText = (EditText) findViewById(R.id.password);
	}

	private void setListenter() {
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regbtn:
			Intent mIntent = new Intent();
			mIntent.setClass(this, RegisterActivity.class);
			startActivity(mIntent);
			break;

		case R.id.loginbtn:
			if (NetWorkCheck.checkNetWorkStatus(this)) {
				ConntectionThread mThread = new ConntectionThread(userNameText
						.getText().toString(), passwordText.getText()
						.toString());
				mThread.start();
			} else {
				showToast(this, "无网络连接, 请检查网络设置");
			}
			break;
		}
	}
	
	public class ConntectionThread extends Thread {
		private String userName;
		private String password;

		public ConntectionThread(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		@Override
		public void run() {
			mHandler.sendMessage(mHandler.obtainMessage(DIALOG_SHOW));
			boolean state = getLoginState(userName, password);
			Boolean mBoolean = Boolean.valueOf(state);
			
			if(state) savaAccount();	// 保存用户名和密码
				
			mHandler.sendMessage(mHandler.obtainMessage(CHECK_STATE, mBoolean));

			mProgressDialog.dismiss();
		}

		private boolean getLoginState(String userName, String password) {
			boolean state = false;

			Map<String, String> param = new HashMap<String, String>();
			param.put("username", userName);
			param.put("password", password);
			String result = HttpClientUtil.postRequest(UrlConfig.LOGIN_ACTION,
					param);
			
			if (result != null || !(result.equals(""))) {
				try {
					JSONObject jsonObjs = new JSONObject(result);
					state = jsonObjs.getBoolean("success");
					System.out.println(state);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return state;
		}
		
		/**
		 * 保存帐户密码
		 */
		private void savaAccount() {
			SharedPreferences initData = LoginActivity.this.getSharedPreferences("account", MODE_PRIVATE);
			Editor mEditor = initData.edit();
			mEditor.putString("userName", userName);
			mEditor.putString("password", password);
			mEditor.putBoolean("loginState", true); // 登录状态
			mEditor.commit();
		}

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_STATE:
				boolean state = ((Boolean) msg.obj).booleanValue();
				if (state) {
					showToast(LoginActivity.this, "登录成功");
					// 跳转到用户帐户页面
					Intent mIntent = new Intent(LoginActivity.this, UserAccountActivity.class);
					startActivity(mIntent);
				} else {
					showToast(LoginActivity.this, "登录失败");
				}
				Log.d(TAG, state + "");
				break;

			case DIALOG_SHOW:
				mProgressDialog.show();
				break;
			}
		}

	};

	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("正在登录");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
		// mProgressDialog.setOnShowListener(this);
		// mProgressDialog.setOnDismissListener((MainActivity)mContext);
	}

	private void showToast(Context mContext, String toast) {
		Toast mToast = Toast.makeText(mContext, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.show();
	}

}
