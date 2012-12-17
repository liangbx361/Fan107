package com.fan107.activity;

import com.common.helper.MessageCode;
import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opinions_activity);
		
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
			new Thread() {

				@Override
				public void run() {
					mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOG);
					int messageCode = sendOpinions();
					mHandler.sendEmptyMessage(messageCode);
					mProgressDialog.dismiss();					
				}
				
			};
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {			
			switch(msg.what) {
			case MessageCode.SHOW_DIALOG:
				break;
				
			case MessageCode.SEND_OPINIONS:
				break;
			}
		}
		
	};
	
	private int sendOpinions() {
		
		String commentStr = commentView.getText().toString();
		String contectStr = contectView.getText().toString();
				
		return 0;
	}
	
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
