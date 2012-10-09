package com.fan107.activity;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fan107.R;
import com.fan107.config.UrlConfig;

import common.connection.net.HttpClientUtil;
import common.connection.net.HttpDownloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SearchActivity extends Activity implements OnClickListener {
	private Button orderDistanceButton;
	private Button orderPopularityButton;
	private Button orderPriceButton;
	
	private Button loginButton;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_commodity_list);

		findView();
		setListenter();
		
		getLogin();
	}

	private void findView() {
		orderDistanceButton = (Button) findViewById(R.id.class_1);
		orderPopularityButton = (Button) findViewById(R.id.class_2);
		orderPriceButton = (Button) findViewById(R.id.class_3);
		
		loginButton = (Button) findViewById(R.id.login);
		mImageView = (ImageView) findViewById(R.id.setAddress);
	}

	private void setListenter() {
		orderDistanceButton.setOnClickListener(this);
		orderPopularityButton.setOnClickListener(this);
		orderPriceButton.setOnClickListener(this);
		
		loginButton.setOnClickListener(this);
		mImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.class_1:
			cleanOrderButtonState();
			orderDistanceButton.setBackgroundResource(R.drawable.tab_left_b);			
			break;
			
		case R.id.class_2:
			cleanOrderButtonState();
			orderPopularityButton.setBackgroundResource(R.drawable.tab_middle_b);
			break;
			
		case R.id.class_3:
			cleanOrderButtonState();
			orderPriceButton.setBackgroundResource(R.drawable.tab_right_b);
			break;
			
		case R.id.login:
			Intent mIntent = new Intent();
			mIntent.setClass(this, LoginActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.setAddress:
			Intent mIntent2 = new Intent();
			mIntent2.setClass(this, RecevoirAddressActivity.class);
			startActivity(mIntent2);
			break;
		}
	}
	
	public void cleanOrderButtonState() {
		orderDistanceButton.setBackgroundResource(R.drawable.tab_left_a);
		orderPopularityButton.setBackgroundResource(R.drawable.tab_middle_a);
		orderPriceButton.setBackgroundResource(R.drawable.tab_right_a);
	}
	
	public void getLogin() {
//		HttpDownloader mDownloader = new HttpDownloader();
//		mDownloader.getHttpContent(UrlConfig.LOGIN_URL, "UTF-8");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("username", "admin");
		param.put("password", "admin");
		HttpClientUtil.postRequest(UrlConfig.LOGIN_ACTION, param);
	}
}
