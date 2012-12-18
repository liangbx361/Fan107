package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderInfo;
import com.fan107.data.UserInfo;
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

public class OpinionActivity extends Activity implements ActivityTemplete, OnClickListener{
	private Button sendButton;
	private EditText commentView;
	private EditText contectView;
	
	ProgressDialog mProgressDialog;
	
	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opinions_activity);
		
		mUserInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		sendButton = (Button) findViewById(R.id.opinions_submit);
		commentView = (EditText) findViewById(R.id.opinions_comment);
		contectView = (EditText) findViewById(R.id.opinions_contect);
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		initProgressDialog(this);
	}

	public void setWidgetAttribute() {
		sendButton.setOnClickListener(this);
	}

	public void onClick(View v) {		
		switch(v.getId()) {
		case R.id.opinions_submit:
			sendOpinions();
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {			
			switch(msg.what) {
			case MessageCode.SHOW_DIALOG:
				mProgressDialog.show();
				break;
				
			case MessageCode.SEND_OPINIONS_SUCCESS:
				ToastHelper.showToastInBottom(OpinionActivity.this, "提交成功", 0);
				onBackPressed();
				break;
				
			case MessageCode.SEND_OPINIONS_FAIL:
				ToastHelper.showToastInBottom(OpinionActivity.this, "提交失败", 0);
				break;
			}
		}
		
	};
	
	private int sendOpinions() {
		
		String commentStr = commentView.getText().toString();
		String contectStr = contectView.getText().toString();
		
		if(commentStr != null && !commentStr.equals("")) {
			new OpinionThread(commentStr, contectStr).start();
			
		} else {
			ToastHelper.showToastInBottom(this, "提交内容不能为空", 0);
		}
		
		return 0;
	}
	
	private class OpinionThread extends Thread {
		private String comment;
		private String contect;
		
		public OpinionThread(String comment, String contect) {
			this.comment = comment;
			this.contect = contect;
		}
		
		@Override
		public void run() {
			mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOG);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", mUserInfo.getUsername());
			data.put("subject", comment);
			data.put("content", contect);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.FEED_BACK_METHOD, data);
			
			if(result.getPropertyAsString(0).equals("ok")) {
				mHandler.sendEmptyMessage(MessageCode.SEND_OPINIONS_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(MessageCode.SEND_OPINIONS_FAIL);
			}
			
			mProgressDialog.dismiss();
		}
	};
	
	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("正在提交中");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
		// mProgressDialog.setOnShowListener(this);
		// mProgressDialog.setOnDismissListener((MainActivity)mContext);
	}	
}
