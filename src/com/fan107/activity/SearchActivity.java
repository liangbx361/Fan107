package com.fan107.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.UrlConfig;
import com.fan107.config.WebServiceConfig;
import com.lbx.templete.ActivityTemplete;

import common.connection.net.HttpClientUtil;
import common.connection.net.HttpDownloader;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener, ActivityTemplete {
	private static final String TAG = "SearchActivity";
	
	private Button orderDistanceButton;
	private Button orderPopularityButton;
	private Button orderPriceButton;
	
	private Button loginButton;
	private ImageView mImageView;
	private TextView mAddressTextView;
	
	private ListView shopListView;
	private ProgressBar loadingBar;
	
	private SimpleAdapter adapter;
	private List<Map<String, String>> shopData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_commodity_list);

		findView();
		setListenter();
		setWidgetAttribute();
				
		LoadShopListThread listThread = new LoadShopListThread(0, 0, 0);
		listThread.start();
	}

	private void findView() {
		orderDistanceButton = (Button) findViewById(R.id.class_1);
		orderPopularityButton = (Button) findViewById(R.id.class_2);
		orderPriceButton = (Button) findViewById(R.id.class_3);
		
		loginButton = (Button) findViewById(R.id.userAccount);
		mImageView = (ImageView) findViewById(R.id.setAddress);
		mAddressTextView = (TextView) findViewById(R.id.search_address);
		
		shopListView = (ListView) findViewById(R.id.shopListView);
		loadingBar = (ProgressBar) findViewById(R.id.loading);
	}

	private void setListenter() {
		orderDistanceButton.setOnClickListener(this);
		orderPopularityButton.setOnClickListener(this);
		orderPriceButton.setOnClickListener(this);
		
		loginButton.setOnClickListener(this);
		mImageView.setOnClickListener(this);
	}

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
			
		case R.id.userAccount:
			Intent mIntent = new Intent();
			mIntent.setClass(this, UserAccountActivity.class);
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
	
	class LoadShopListThread extends Thread {
		int userId;
		int aid;
		int sid;
		int did;
		int searchType;
		
		public LoadShopListThread(int userId) {
			this.userId = userId;
			searchType = 0;
		}
		
		public LoadShopListThread(int aid, int sid, int did) {
			this.aid = aid;
			this.sid = sid;
			this.did = did;
			searchType = 1;
		}

		@Override
		public void run() {
			getShotList(userId);
			mHandler.sendEmptyMessage(0);
		}
		
		private void getShotList(int userId) {
			
			String url = WebServiceConfig.url + WebServiceConfig.SHOP_LIST_WEB_SERVICE;
			SoapObject shopInfo;
			if(searchType == 0) {			
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				shopInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_SHOP_LIST_BY_USER_ID_METHOD, params);
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("aid", aid);
				params.put("sid", sid);
				params.put("did", did);
				shopInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_SHOP_LIST_BY_ADDRESS_ID_METHOD, params);
			}
			parseShopInfo(shopInfo);
		}
		
		private void parseShopInfo(SoapObject shopInfo) {
						
			int[] childList = {0, 1, 0};
			SoapObject child = WebServiceUtil.getChildSoapObject(shopInfo, childList);
					
			for(int i=0; i<child.getPropertyCount(); i++) {
				SoapObject shopChild = (SoapObject) child.getProperty(i);
				
				String shopname = WebServiceUtil.getSoapObjectString(shopChild, "shopname");
				String shoppic = WebServiceUtil.getSoapObjectString(shopChild, "shoppic");
				
				Map<String, String> mData = new HashMap<String, String>();
				mData.put("shopname", shopname);
				mData.put("shoppic", shoppic);
				
				shopData.add(mData);
			}
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			shopListView.removeAllViewsInLayout();
			shopListView.requestLayout();
			loadingBar.setVisibility(View.GONE);
		}
	};

	public void findWidget() {
		// TODO Auto-generated method stub
		
	}

	public void setWidgetListenter() {
		// TODO Auto-generated method stub
		
	}

	public void setWidgetPosition() {
		// TODO Auto-generated method stub
		
	}

	public void setWidgetAttribute() {
		shopData = new ArrayList<Map<String,String>>();		
		adapter = new SimpleAdapter(this, shopData, R.layout.shop_list_item, 
				new String[]{"shopname"}, 
				new int[]{R.id.shop_name});
		shopListView.setAdapter(adapter);
	}
}
