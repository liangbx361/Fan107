package com.fan107.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

public class UserCommentActivity extends Activity implements ActivityTemplete, OnClickListener{
	
	private final String[] SpinnerValues = {"请选择", "差(1)", "一般(2)", "好(3)", "很好(4)", "非常好(5)" };
	
	private Button submitBtn;
	private RatingBar totalRatingBar;
	private Spinner tasteSpinner;
	private Spinner milieuSpinner;
	private Spinner serviceSpinner;
	private EditText commetEditText;
	
	private ProgressDialog mProgressDialog;
	private OrderInfo mOrderInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_user_comment);
		
		mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		submitBtn = (Button) findViewById(R.id.user_comment_submit);
		totalRatingBar = (RatingBar) findViewById(R.id.user_comment_ratingBar);
		tasteSpinner = (Spinner) findViewById(R.id.user_comment_tastepoin_spinner);
		milieuSpinner = (Spinner) findViewById(R.id.user_comment_milieupoint_spinner);
		serviceSpinner = (Spinner) findViewById(R.id.user_comment_servicepoint_spinner);
		commetEditText = (EditText) findViewById(R.id.user_comment_comment);
	}

	public void setWidgetListenter() {
		submitBtn.setOnClickListener(this);
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		tasteSpinner.setAdapter(new ArrayAdapter<String>(UserCommentActivity.this, R.layout.fan107_spinner_item, SpinnerValues));
		milieuSpinner.setAdapter(new ArrayAdapter<String>(UserCommentActivity.this, R.layout.fan107_spinner_item, SpinnerValues));
		serviceSpinner.setAdapter(new ArrayAdapter<String>(UserCommentActivity.this, R.layout.fan107_spinner_item, SpinnerValues));
		
		initProgressDialog(this);
	}
	
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.user_comment_submit:
			submitComment();
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
				ToastHelper.showToastInBottom(UserCommentActivity.this, "提交成功", 0);
				onBackPressed();
				break;
				
			case MessageCode.SEND_OPINIONS_FAIL:
				ToastHelper.showToastInBottom(UserCommentActivity.this, "提交失败", 0);
				break;
			}
		}
		
	};
	
	private void submitComment() {
		int totalPoint = (int) totalRatingBar.getRating();
		String taste = (String) tasteSpinner.getSelectedItem();
		String milieu = (String) milieuSpinner.getSelectedItem();
		String service = (String) serviceSpinner.getSelectedItem();
		String content = commetEditText.getText().toString();
		
		if(taste == null || taste.equals("请选择")) {		
			
			ToastHelper.showToastInBottom(this, "口味暂未评价", 0);
			return;
			
		} else if(milieu == null || milieu.equals("请选择")) {
			
			ToastHelper.showToastInBottom(this, "环境暂未评价", 0);
			return;
			
		} else if(service == null || service.equals("请选择")) {
			
			ToastHelper.showToastInBottom(this, "服务暂未评价", 0);
			return;
			
		} 
		
		JSONObject commentObject = new JSONObject();
		try {
			commentObject.put("totalPoint", totalPoint);
			commentObject.put("tastePoint", getIndex(taste));
			commentObject.put("milieuPoint", getIndex(milieu));
			commentObject.put("servicePoint", getIndex(service));
			commentObject.put("comment", content);
			commentObject.put("shopId", mOrderInfo.shopId);
			commentObject.put("userId", mOrderInfo.userId);
			commentObject.put("userName", mOrderInfo.username);
			
		} catch (JSONException e) {
			commentObject = null;
			e.printStackTrace();
		}
		
		if(commentObject != null) {
			new SubmitCommentThread(commentObject.toString()).start();
		}
		
	}
	
	private int getIndex(String value) {
		for(int i=0; i<SpinnerValues.length; i++) {
			if(SpinnerValues[i].equals(value)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private class SubmitCommentThread extends Thread {
		private String message;
		
		public SubmitCommentThread(String message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOG);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("commentMessage", message);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.USER_COMMENT_METHOD, data);
			
			if(result != null && result.getPropertyAsString(0).equals("ok")) {
				mHandler.sendEmptyMessage(MessageCode.SEND_OPINIONS_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(MessageCode.SEND_OPINIONS_FAIL);
			}
			
			mProgressDialog.dismiss();
		}
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
