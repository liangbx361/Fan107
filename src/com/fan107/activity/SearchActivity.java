package com.fan107.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
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
import com.fan107.common.UserState;
import com.fan107.config.UrlConfig;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.ShopInfo;
import com.fan107.data.UserAddress;
import com.fan107.data.UserInfo;
import com.fan107.data.UserLogin;
import com.fan107.db.DBHelper;
import com.lbx.cache.FileCache;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

import common.connection.net.HttpClientUtil;
import common.connection.net.HttpDownloader;
import common.connection.net.WebServiceUtil;
import common.file.util.FileUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener, OnItemClickListener, ActivityTemplete {
	private static final String TAG = "SearchActivity";
	
	private static final String NO_LOGIN_MESSAGE = "如需要订餐，请先登录";
	private static final String LOGIN_MESSAGE = "登录";
	private static final String ACCOUNT_MESSAGE = "帐户";
	
	private static final int SHOW_SHOP = 100;
	private static final int NO_SHOW_SHOP = 101;
	
	private Button orderDistanceButton;
	private Button orderPopularityButton;
	private Button orderPriceButton;
	
	private Button loginButton;
	private ImageView mImageView;
	private TextView mAddressTextView;
	private TextView noShopView;
	
	private ListView shopListView;
	private ProgressBar loadingBar;
	
	private MyAdapter adapter;
	private List<Map<String, String>> shopData;
	private List<ShopInfo> shopInfoList;
	
	private FileCache fileCache;
	private String orderType;
	private boolean isLogin;
	private boolean isFirst;
	
	private UserInfo mUserInfo;
	private UserAddress selectAddress;	
	private List<UserAddress> mAddressList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_commodity_list);

		findWidget();
		setWidgetListenter();
		setWidgetAttribute();
		
		fileCache = new FileCache(this); 
		orderType = "hit";
		isFirst = true;
		
		UserState.setLoginState(this, false);
		LoadShopListThread listThread = new LoadShopListThread(orderType, true);
		listThread.start();
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		
		if(UserState.getRefreshState(this)) {
			UserState.setRefreshState(this, false);
			SharedPreferences initData = this.getSharedPreferences("account", Activity.MODE_PRIVATE);
			String address = initData.getString("address", null);
			Editor mEditor = initData.edit();
			mEditor.putString("address", null);
			
			if(address == null) {
				mHandler.sendEmptyMessage(4);
			}
			else if(!selectAddress.getAddress().equals(address)) {
				//更新商户
				selectAddress.setAddress(address);
				selectAddress.setMobile(initData.getString("mobile", null));
				selectAddress.setUserName(initData.getString("username", null));	
				mHandler.sendEmptyMessage(1);
			}
			
			
		}
	}

	public void findWidget() {
		orderDistanceButton = (Button) findViewById(R.id.class_1);
		orderPopularityButton = (Button) findViewById(R.id.class_2);
		orderPriceButton = (Button) findViewById(R.id.class_3);
		
		loginButton = (Button) findViewById(R.id.userAccount);
		mImageView = (ImageView) findViewById(R.id.setAddress);
		mAddressTextView = (TextView) findViewById(R.id.search_address);
				
		shopListView = (ListView) findViewById(R.id.shopListView);
		loadingBar = (ProgressBar) findViewById(R.id.loading);		
		
		noShopView = (TextView) findViewById(R.id.no_shop_tv);
	}
	
	public void setWidgetListenter() {
		orderDistanceButton.setOnClickListener(this);
		orderPopularityButton.setOnClickListener(this);
		orderPriceButton.setOnClickListener(this);
		
		loginButton.setOnClickListener(this);
		mImageView.setOnClickListener(this);	
		
		shopListView.setOnItemClickListener(this);
	}

	public void setWidgetPosition() {
	
	}

	public void setWidgetAttribute() {
		shopData = new ArrayList<Map<String,String>>();
		shopInfoList = new ArrayList<ShopInfo>();
		orderPopularityButton.setBackgroundResource(R.drawable.tab_middle_b);
		
		adapter = new MyAdapter(SearchActivity.this, shopData, R.layout.shop_list_item, 
				new String[]{"shopname", "flavor", "environment", "server", "ordertime"}, 
				new int[]{R.id.shop_name, R.id.shop_flavor_point, R.id.shop_environment_point, 
					R.id.shop_server_point, R.id.scheduled_time_value});

		mAddressTextView.setText("");
		loginButton.setText(LOGIN_MESSAGE);
		
		noShopView.setVisibility(View.GONE);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		//按时间排序
		case R.id.class_1:
			if(!orderType.equals("time")) {
				cleanOrderButtonState();
				orderDistanceButton.setBackgroundResource(R.drawable.tab_left_b);				
				orderType = "time";
				mHandler.sendEmptyMessage(1);
			}
			break;
		
		//按人气排序
		case R.id.class_2:
			if(!orderType.equals("hit")) {
				cleanOrderButtonState();
				orderPopularityButton.setBackgroundResource(R.drawable.tab_middle_b);
				orderType = "hit";
				mHandler.sendEmptyMessage(1);
			}
			break;
			
		//按评分排序
		case R.id.class_3:
			if(!orderType.equals("point")) {
				cleanOrderButtonState();
				orderPriceButton.setBackgroundResource(R.drawable.tab_right_b);
				orderType = "point";
				mHandler.sendEmptyMessage(1);
			}
			break;
		
		//跳转到用户帐户, 或者登录界面
		case R.id.userAccount:
			Intent mIntent = new Intent();
			if(isLogin) {	
				UserState.saveUserInfoToDB(this, mUserInfo);
				mIntent.setClass(this, UserAccountActivity.class);
				mIntent.putExtra("userInfo", mUserInfo);				
			} else {
				mIntent.setClass(this, LoginActivity.class);
			}
			startActivity(mIntent);
			break;
		
		//更改收餐地址
		case R.id.setAddress:
			Intent mIntent2 = new Intent();
			if(isLogin) {
				mIntent2.setClass(this, RecevoirAddressActivity.class);	
				mIntent2.putExtra("activityName", "SearchActivity");
			} else {
				mIntent2.setClass(this, LoginActivity.class);
			}
			startActivity(mIntent2);
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 0:
				shopListView.setAdapter(adapter);
			
				shopListView.removeAllViewsInLayout();
				shopListView.requestLayout();
				loadingBar.setVisibility(View.GONE);
				loginButton.setText(ACCOUNT_MESSAGE);
				break;
			
			case 1:
				loadingBar.setVisibility(View.VISIBLE);
				shopListView.removeAllViewsInLayout();
				shopListView.requestLayout();
				shopListView.setAdapter(null);
				shopData.clear();
				
				new LoadShopListThread(orderType, false).start();				
				break;
				
			case 2:
				String addressName = msg.obj.toString();
				mAddressTextView.setText(addressName);
				break;
				
			case 3:
				shopListView.setAdapter(adapter);
				
				shopListView.removeAllViewsInLayout();
				shopListView.requestLayout();
				loadingBar.setVisibility(View.GONE);
				mAddressTextView.setText(NO_LOGIN_MESSAGE);
				loginButton.setText(LOGIN_MESSAGE);
				break;
				
			case 4:
				loadingBar.setVisibility(View.VISIBLE);
				shopListView.removeAllViewsInLayout();
				shopListView.requestLayout();
				shopListView.setAdapter(null);
				shopData.clear();
				
				new LoadShopListThread(orderType, true).start();
				break;
				
			case UserState.HANDLER_AUTO_LOGIN_SUCCESS:
				if(isFirst) {
					ToastHelper.showToastInBottom(SearchActivity.this, UserState.AUTO_LOGIN_SUCCESS);
					isFirst = false;
				}
				break;
				
			case UserState.HANDLER_LOGIN_FAIL:
				if(isFirst) {
					ToastHelper.showToastInBottom(SearchActivity.this, UserState.LOGIN_FAIL);
					isFirst = false;
				}
				break;
				
			case UserState.HANDLER_LOGIN_SUCCESS:
				if(isFirst) {
					ToastHelper.showToastInBottom(SearchActivity.this, UserState.LOGIN_SUCCESS);
					isFirst = false;
				}
				break;
				
			case UserState.HANDLER_NO_NETWORK:
				if(isFirst) {
					ToastHelper.showToastInBottom(SearchActivity.this, UserState.NO_NETWORK);
					isFirst = false;
				}
				break;
				
			case SHOW_SHOP:
				noShopView.setVisibility(View.VISIBLE);
				break;
				
			case NO_SHOW_SHOP:
				noShopView.setVisibility(View.GONE);
				break;
			}
		}
	};	
	
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
		String orderType;
		boolean isNeedLogin;
		
		public LoadShopListThread(int userId, boolean isNeedLogin) {
			this.userId = userId;
			this.isNeedLogin = isNeedLogin;
			searchType = 0;
		}
		
		public LoadShopListThread(String orderType, boolean isNeedLogin) {
			this.orderType = orderType;
			searchType = 1;
			this.isNeedLogin = isNeedLogin;
		}

		@Override
		public void run() {
			if(isNeedLogin) {
				UserLogin mLogin = new UserLogin();
				
				if(!UserState.getLoginState(SearchActivity.this)) {
					isLogin = UserState.autoLogin(SearchActivity.this, mLogin, mHandler); 
					if(isLogin) {
						mUserInfo = UserState.getUserInfo(mLogin.getUserName(), mLogin.getPasswdMD5());
						mAddressList = getAddressList(mUserInfo.getUserid());
						selectAddress = getDefaultAddress(mAddressList);
					}
				} else {
					isLogin = true;
					SharedPreferences initData = SearchActivity.this.getSharedPreferences("account", Activity.MODE_PRIVATE);
					String username = initData.getString("username", "");
					String password = initData.getString("password", "");
					mUserInfo = UserState.getUserInfo(username, password);
					mAddressList = getAddressList(mUserInfo.getUserid());
					selectAddress = getDefaultAddress(mAddressList);
				}				
			}
			
			if(isLogin) {
				try{ 
					String[] addressIdList = selectAddress.getAddress().split("\\|")[0].split(",");
					String addressName = selectAddress.getAddress().split("\\|")[1];	//用户的默认地址
					aid = Integer.parseInt(addressIdList[0]);
					sid = Integer.parseInt(addressIdList[1]);
					did = Integer.parseInt(addressIdList[2]);
					mHandler.sendMessage(mHandler.obtainMessage(2, addressName));
				}catch (Exception e) {
					aid = 0;
					sid = 0;
					did = 0;
				}
				searchType = 1;
				getShotList();
				mHandler.sendEmptyMessage(0);				
			} else {
				getShotList();
				mHandler.sendEmptyMessage(3);
			}
		}
		
		private void getShotList() {			
			String url = WebServiceConfig.url + WebServiceConfig.SHOP_LIST_WEB_SERVICE;
			SoapObject shopInfo;
			if(searchType == 0) {			
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				params.put("orderType",	orderType);
				shopInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_SHOP_LIST_BY_USER_ID_METHOD, params);
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("aid", aid);
				params.put("sid", sid);
				params.put("did", did);
				params.put("orderType", orderType);
				shopInfo = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_SHOP_LIST_BY_ADDRESS_ID_METHOD, params);
			}
			
			if(shopInfo != null) {
				parseShopInfo(shopInfo);
				mHandler.sendEmptyMessage(NO_SHOW_SHOP);
			} else mHandler.sendEmptyMessage(SHOW_SHOP);
		}
		
		private void parseShopInfo(SoapObject shopInfo) {
						
			int[] childList = {0, 1, 0};
			SoapObject child = WebServiceUtil.getChildSoapObject(shopInfo, childList);
					
			for(int i=0; i<child.getPropertyCount(); i++) {
				SoapObject shopChild = (SoapObject) child.getProperty(i);
								
				ShopInfo mInfo = SaveShopInfo(shopChild);
				
				Map<String, String> mData = new HashMap<String, String>();
				mData.put("shopname", mInfo.getShopname());
				mData.put("shoppic", mInfo.getShoppic());
				mData.put("flavor", String.valueOf(mInfo.getSumtastepoint()));
				mData.put("environment", String.valueOf(mInfo.getSummilieupoint()));
				mData.put("server", String.valueOf(mInfo.getSumservicepoint()));
				mData.put("ordertime", mInfo.getOrdertime());
				
				shopData.add(mData);
				shopInfoList.add(mInfo);
								
				if(mInfo.getShoppic() != null && !mInfo.getShoppic().equals("")) {
					try {					
						File picFile = fileCache.getFile(mInfo.getShoppic());
						if(!picFile.exists()) {
							InputStream inputStream = HttpDownloader.getInputStreamFromUrl(WebServiceConfig.RES_URL + mInfo.getShoppic());						
							FileUtils.write2SDFromInput(picFile, inputStream);
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private ShopInfo SaveShopInfo(SoapObject shopInfo) {
			ShopInfo mInfo = new ShopInfo();
			mInfo.setShopId(WebServiceUtil.getSoapObjectInt(shopInfo, "id"));
			mInfo.setShopname(WebServiceUtil.getSoapObjectString(shopInfo, "shopname"));
			mInfo.setAddress(WebServiceUtil.getSoapObjectString(shopInfo, "address"));
			mInfo.setAddtime(WebServiceUtil.getSoapObjectString(shopInfo, "addtime"));
			mInfo.setHit(WebServiceUtil.getSoapObjectInt(shopInfo, "hit"));
			mInfo.setIntro(WebServiceUtil.getSoapObjectString(shopInfo, "intro"));
			mInfo.setLimitprice(WebServiceUtil.getSoapObjectFloat(shopInfo, "limitprice"));
			mInfo.setOpentime(WebServiceUtil.getSoapObjectString(shopInfo, "opentime"));
			mInfo.setOrdertime(WebServiceUtil.getSoapObjectString(shopInfo, "ordertime"));
			mInfo.setPayment(WebServiceUtil.getSoapObjectString(shopInfo, "payment"));
			mInfo.setPhone(WebServiceUtil.getSoapObjectString(shopInfo, "phone"));
			mInfo.setSendprice(WebServiceUtil.getSoapObjectFloat(shopInfo, "sendprice"));
			mInfo.setSendtime(WebServiceUtil.getSoapObjectInt(shopInfo, "sendtime"));
			mInfo.setShoppic(WebServiceUtil.getSoapObjectString(shopInfo, "shoppic"));
			mInfo.setSummilieupoint(WebServiceUtil.getSoapObjectInt(shopInfo, "summilieupoint"));
			mInfo.setSumperson(WebServiceUtil.getSoapObjectInt(shopInfo, "sumperson"));
			mInfo.setSumpoint(WebServiceUtil.getSoapObjectInt(shopInfo, "sumpoint"));
			mInfo.setSumservicepoint(WebServiceUtil.getSoapObjectInt(shopInfo, "sumservicepoint"));
			mInfo.setSumtastepoint(WebServiceUtil.getSoapObjectInt(shopInfo, "sumtastepoint"));
			mInfo.setZk(WebServiceUtil.getSoapObjectInt(shopInfo, "zk"));			
			return mInfo;
		}
	}
		
	class MyAdapter extends SimpleAdapter {
		private List<? extends Map<String, ?>> mData;
		
		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			
			mData = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =  super.getView(position, convertView, parent);

			LinearLayout layout = (LinearLayout) view;
			ImageView pic = (ImageView) layout.findViewById(R.id.shop_pic);
			String picPath = (String) mData.get(position).get("shoppic");
			if(picPath != null && !picPath.equals("")) {
				File picFile = fileCache.getFile(picPath);
				if(picFile.exists()) {
					String pathStr = picFile.getAbsolutePath();
					pathStr = pathStr.replace("/mnt", "");
					Uri uri = Uri.parse("file://" + pathStr);
					pic.setImageURI(uri);
				}
			} else {
				pic.setImageResource(R.drawable.default_pic);
			}
							
//			Log.d(TAG, "picPath: " + picPath);
			return view;
		}
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShopInfo mInfo = shopInfoList.get(position);
		Intent mIntent = new Intent(this, ShopInfoActivity.class);
		mIntent.putExtra("shopInfo", mInfo);
		mIntent.putExtra("userInfo", mUserInfo);
		mIntent.putExtra("userAddress", selectAddress);
		startActivity(mIntent);
	}
	
	/**
	 * 获取服务器的默认地址
	 * @param userId
	 * @return
	 */
	public UserAddress getDefaultAddress(int userId) {
		UserAddress mAddress = new UserAddress();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject userAddressObject = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_DEFAULT_ADDRESS_METHOD, params);
		
		SoapObject chid = (SoapObject) userAddressObject.getProperty(0);
		String userName = WebServiceUtil.getSoapObjectString(chid, "UserName");
		String address = WebServiceUtil.getSoapObjectString(chid, "Address");
		String mobile = WebServiceUtil.getSoapObjectString(chid, "Mobile");
		
		mAddress.setUserId(userId);
		mAddress.setUserName(userName);
		mAddress.setAddress(address);
		mAddress.setMobile(mobile);
		
		return mAddress;
	}
	
	public UserAddress getDefaultAddress(List<UserAddress> addrssList) {
		UserAddress userAddress = null;
		
		for(int i=0; i<addrssList.size(); i++) {

			if(addrssList.get(i).getIsDefault() > 0) {
				userAddress = addrssList.get(i);
				break; 
			}
		}
		
		return userAddress;
	}
	
	/**
	 * 获取服务器的用户地址信息
	 * @param userId
	 * @return
	 */
	public List<UserAddress> getAddressList(int userId) {
		List<UserAddress> addrssList = new ArrayList<UserAddress>();
		DBHelper dbHelper = new DBHelper(this);
		dbHelper.deleteTableContent(DBHelper.USER_ADDRESS_TABLE_NAME, "userid="+userId);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject addressListObject = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_ADDRESS_LIST_METHOD, params);
		
		addressListObject = WebServiceUtil.getChildSoapObject(addressListObject, new int[]{0});
		for(int i=0; i<addressListObject.getPropertyCount(); i++) {
			SoapObject chid = (SoapObject) addressListObject.getProperty(i);
			int id = WebServiceUtil.getSoapObjectInt(chid, "Id");
			String userName = WebServiceUtil.getSoapObjectString(chid, "UserName");
			String address = WebServiceUtil.getSoapObjectString(chid, "Address");
			String mobile = WebServiceUtil.getSoapObjectString(chid, "Mobile");
			int isDefault = WebServiceUtil.getSoapObjectInt(chid, "IsDefault");
			
			UserAddress userAddress = new UserAddress();
			userAddress.setId(id);
			userAddress.setUserId(userId);
			userAddress.setUserName(userName);
			userAddress.setMobile(mobile);
			userAddress.setAddress(address);			
			userAddress.setIsDefault(isDefault);
			
			addrssList.add(userAddress);
			
			//保存到数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("id", id);
			contentValues.put("userid", userId);
			contentValues.put("username", userName);
			contentValues.put("mobile", mobile);
			contentValues.put("address", address);
			contentValues.put("isdefault", isDefault);			
			
			dbHelper.insert(DBHelper.USER_ADDRESS_TABLE_NAME, contentValues);			
		}
		
		dbHelper.close();
		return addrssList;
	}
}
