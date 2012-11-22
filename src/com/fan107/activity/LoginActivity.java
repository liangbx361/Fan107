package com.fan107.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.UrlConfig;
import com.fan107.config.WebServiceConfig;
import com.fan107.db.DBHelper;

import common.connection.net.HttpClientUtil;
import common.connection.net.NetWorkCheck;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.ConditionVariable;
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
	
	private DBHelper dbHelper;

	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);

		findView();
		setListenter();
		initProgressDialog(this);
		
		dbHelper = new DBHelper(this);
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
				showToast(this, "����������, ������������");
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
//			boolean state = getLoginState(userName, password);
			boolean state = getLoginStateByWebService(userName, password);
			Boolean mBoolean = Boolean.valueOf(state);				
			mHandler.sendMessage(mHandler.obtainMessage(CHECK_STATE, mBoolean));

			mProgressDialog.dismiss();
		}
		
		/**
		 * ʹ��Post��ȡ��¼״̬
		 * @param userName
		 * @param password
		 * @return
		 */
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
		 * ͨ��webservice��֤��¼
		 * @param userName
		 * @param password
		 * @return
		 */
		private boolean getLoginStateByWebService(String userName, String password) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", userName);
			params.put("password", password);
			String url = WebServiceConfig.url + WebServiceConfig.LOGIN_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.LOGIN_METHOD, params);
			
			if(result != null) {
				String passwordMD5 = parseSoapObject(result); //���MD5����
				saveLoginUser(userName, passwordMD5);
				saveUserInfo(userName, passwordMD5);
				return true;
			} else {
				return false;
			}
			
		}
		
		/**
		 * ���� SoapObject ��ȡ�û���Ϣ
		 * @param soapObject
		 */
		public String parseSoapObject(SoapObject soapObject) {
		
			return soapObject.getProperty(WebServiceConfig.LOGIN_METHOD + "Result").toString();
		}
		
		/**
		 * ��ʱû���ж�����û����Ѿ����������ݿ⣬��ν�����⣬ ˼·���ж�ͬ���û��Ƿ���ڣ��������������û����ݣ�����Ų�����Ϣ
		 * @param username
		 * @param soapObject
		 */
		private void saveLoginUser(String username, String passwordMD5) {
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d=new Date();
			String currentTime = s.format(d.getTime());
			
			//��ɾ�������Զ���¼��־
			dbHelper.updateTable("update " + DBHelper.USER_LOGIN_TABLE_NAME + " set auto_login=0 where auto_login=1");
			
			ContentValues contentValues = new ContentValues();
			contentValues.put("username", username);
			contentValues.put("userpass", passwordMD5);
			contentValues.put("login_time", currentTime);
			contentValues.put("auto_login", 1);
			
			dbHelper.insertOrUpdate(DBHelper.USER_LOGIN_TABLE_NAME, contentValues, "username", "String");
		}
		
		private void saveUserInfo(String username, String passwordMD5) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", username);
			params.put("password", passwordMD5);
			String url = WebServiceConfig.url + WebServiceConfig.LOGIN_WEB_SERVICE;
			SoapObject userInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_LOGIN_USER_INFO_METHOD, params);
			
			if(userInfo != null) {
				saveUserInfoToDB(userInfo);
			} else {
				Log.d(TAG, "�û���Ϣ��ȡʧ��!");
			}
		}
		
		/**
		 * �����û���Ϣ������
		 * @param userInfo
		 */
		private void saveUserInfoToDB(SoapObject userInfo) {
			for(int i=0; i<userInfo.getPropertyCount(); i++) {
				SoapObject soapChilds = (SoapObject) userInfo.getProperty(i);
				
				int userGroup = WebServiceUtil.getSoapObjectInt(soapChilds, "UserGroup");
				String userName = WebServiceUtil.getSoapObjectString(soapChilds, "UserName");
				String nickName = WebServiceUtil.getSoapObjectString(soapChilds, "NickName");
				int gender =  WebServiceUtil.getSoapObjectInt(soapChilds, "Gender");
				String birthday = WebServiceUtil.getSoapObjectString(soapChilds, "Birthday");
				float totalPoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "TotalPoint");
				float currentPoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "CurrentPoint");
				float usePoint = WebServiceUtil.getSoapObjectFloat(soapChilds, "UsePoint");
				int spreadCount = WebServiceUtil.getSoapObjectInt(soapChilds, "SpreadCount");
				String mobile = WebServiceUtil.getSoapObjectString(soapChilds, "Mobile");
				String email = WebServiceUtil.getSoapObjectString(soapChilds, "Email");
				String address = WebServiceUtil.getSoapObjectString(soapChilds, "Address");
				int utype = WebServiceUtil.getSoapObjectInt(soapChilds, "Utype");
				int shopId = WebServiceUtil.getSoapObjectInt(soapChilds, "ShopId");
				String addTime = WebServiceUtil.getSoapObjectString(soapChilds, "AddTime");
								
				ContentValues contentValues = new ContentValues();
				contentValues.put("usergroup", userGroup);
				contentValues.put("username", userName);
				contentValues.put("nickname", nickName);
				contentValues.put("gender", gender);
				contentValues.put("birthday", birthday);
				contentValues.put("totalpoint", totalPoint);
				contentValues.put("currentpoint", currentPoint);
				contentValues.put("usepoint", usePoint);
				contentValues.put("spreadcount", spreadCount);
				contentValues.put("mobile", mobile);
				contentValues.put("email", email);
				contentValues.put("address", address);
				contentValues.put("utype", utype);
				contentValues.put("shopid", shopId);
				contentValues.put("addtime", addTime);
				
				dbHelper.insertOrUpdate(DBHelper.USER_TABLE_NAME, contentValues, "username", "String");
			}
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_STATE:
				boolean state = ((Boolean) msg.obj).booleanValue();
				if (state) {
					showToast(LoginActivity.this, "��¼�ɹ�");
					// ��ת���û��ʻ�ҳ��
					Intent mIntent = new Intent(LoginActivity.this, UserAccountActivity.class);
					startActivity(mIntent);
				} else {
					showToast(LoginActivity.this, "��¼ʧ��");
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
		mProgressDialog.setTitle("���ڵ�¼");
		mProgressDialog.setMessage("���Ե�...");
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
