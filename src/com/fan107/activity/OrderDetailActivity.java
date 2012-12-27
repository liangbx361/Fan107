package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderInfo;
import com.fan107.data.OrderItem;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderDetailActivity extends Activity implements ActivityTemplete, OnClickListener{
	private static final String[] ORDER_STATES = {"未处理", "处理中", "已完成", "作废"};
	
	private OrderInfo mOrderInfo;
	private Button mCommentBtn;
	private TextView mOrderNumberView;
	private TextView mShopNameView;
	private TextView mMoneyView;
	private TextView mPointView;
	private TextView mTimeView;
	private TextView mNameView;
	private TextView mTelView;
	private TextView mSendTimeView;
	private TextView mAddressView;
	private TextView mStatesView;
	
	private RelativeLayout dishDetailLayout;
	
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_order_detail);
		
		mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}
	
	public void findWidget() {
		mCommentBtn = (Button) findViewById(R.id.order_detail_comment_btn);
		mOrderNumberView = (TextView) findViewById(R.id.order_detail_order_number);
		mShopNameView = (TextView) findViewById(R.id.order_detail_shop_name);
		mMoneyView = (TextView) findViewById(R.id.order_detail_money);
		mPointView = (TextView) findViewById(R.id.order_detail_point);
		mTimeView = (TextView) findViewById(R.id.order_detail_time);
		mNameView = (TextView) findViewById(R.id.order_detail_name);
		mTelView = (TextView) findViewById(R.id.order_detail_tel);
		mSendTimeView = (TextView) findViewById(R.id.order_detail_send_time);
		mAddressView = (TextView) findViewById(R.id.order_detail_address);
		mStatesView = (TextView) findViewById(R.id.order_detail_states);		
		
		dishDetailLayout = (RelativeLayout) findViewById(R.id.dish_detail);
	}

	public void setWidgetListenter() {
		mCommentBtn.setOnClickListener(this);
		dishDetailLayout.setOnClickListener(this);
	}

	public void setWidgetPosition() {
		
	}
	
	public void setWidgetAttribute() {		
		String time = mOrderInfo.addTime.replace("T", " ");
		int end = time.indexOf(".");
		time = time.substring(0, end);
		
		mOrderNumberView.setText(mOrderInfo.orderNo);
		mShopNameView.setText(mOrderInfo.shopName);
		mMoneyView.setText(mOrderInfo.totalPrice + "");
		mPointView.setText(mOrderInfo.orderPoint + "");
		mTimeView.setText(time);
		mNameView.setText(mOrderInfo.username);
		mTelView.setText(mOrderInfo.userTel);
		mSendTimeView.setText(mOrderInfo.remark);
		mAddressView.setText(mOrderInfo.userAddress);
		mStatesView.setText(ORDER_STATES[mOrderInfo.orderStatus]);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.order_detail_comment_btn:
			Intent commentIntent = new Intent(this, UserCommentActivity.class);
			commentIntent.putExtra("orderInfo", mOrderInfo);
			startActivity(commentIntent);
			break;
			
		case R.id.dish_detail:
			Intent intent = new Intent(this, DishDetailActivity.class);
			intent.putExtra("orderInfo", mOrderInfo);
			startActivity(intent);
			break;
		}
	}
}
