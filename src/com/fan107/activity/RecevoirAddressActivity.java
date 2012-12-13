package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Text;

import com.fan107.R;
import com.fan107.common.UserState;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.UserAddress;
import com.fan107.db.DBHelper;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RecevoirAddressActivity extends Activity implements
		ActivityTemplete, OnClickListener, OnItemClickListener {
	
	private static final String TAG = "RecevoirAddressActivity";
	private static final int INVALID_VIE = 0;
	private static final int UPDATE_DEFALUT = 1;

	private Button addAddressButton;
	private ListView addressListView;
	private MyAdapter mAdapter;
	private List<Map<String, Object>> mData;
	private List<List<TextView>> mList;
	private List<LinearLayout> layoutView;
	
	private List<UserAddress> addressList;
	
	private String activityName;
	int userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recevoir_address);
		activityName = getIntent().getStringExtra("activityName");
		userId = getIntent().getIntExtra("userId", -1);
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setWidgetAttribute();		
		Log.d(TAG, "onResume: " + addressListView.getChildCount());
	}

	public void findWidget() {
		addAddressButton = (Button) findViewById(R.id.add_adress_btn);
		addressListView = (ListView) findViewById(R.id.addressListView);

	}

	public void setWidgetListenter() {
		addAddressButton.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		mData = new ArrayList<Map<String,Object>>();
		setAddressData();
		mAdapter = new MyAdapter(this, mData, R.layout.address_list_item,
				new String[] {"name", "phone", "address"}, new int[] { R.id.address_list_name, R.id.address_list_phone,
						R.id.address_list_address });
		
		mList = new ArrayList<List<TextView>>();
		layoutView = new ArrayList<LinearLayout>();
		
		addressListView.setAdapter(mAdapter);
		addressListView.setOnItemClickListener(this);
		Log.d(TAG, addressListView.getChildCount()+"");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_adress_btn:
			if(addressList.size() < 3) {
				Intent mIntent = new Intent();
				mIntent.setClass(this, AddressAddActivity.class);
				startActivity(mIntent);
			} else {
				ToastHelper.showToastInBottom(this, "添加地址不能超过3个");
			}
			break;
		
		//编辑按钮	
		case R.id.address_list_edit:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(0) ) ) {
					
					Intent mIntent2 = new Intent(this, AddressAddActivity.class);
					mIntent2.putExtra("useraddress", addressList.get(i));
					startActivity(mIntent2);
					Log.d(TAG, "edit_btn: " + i);
					break;
				}
			}
			
			break;
		
		//删除按钮
		case R.id.address_list_del:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(1) ) ) {
					
					deleteAddress(addressList.get(i));
					addressList.remove(i);
					mHandler.removeMessages(INVALID_VIE);
					mHandler.sendMessage(mHandler.obtainMessage(INVALID_VIE, i, 0));
					Log.d(TAG, "del_btn: " + i);
					break;
				}
			}
			
			break;
		
		//设置默认地址 
		case R.id.address_list_default:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(2) ) ) {
					
					setDefaultAddress(addressList.get(i));					
				}
			}
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case INVALID_VIE:
				try {
					mData.remove(msg.arg1);
					mList.clear();
					layoutView.clear();
				} catch (IndexOutOfBoundsException exception) {
					mData.clear();
					mList.clear();
				}
								
				addressListView.removeAllViewsInLayout();
				addressListView.requestLayout();
				break;
				
			case UPDATE_DEFALUT:
				mData.clear();
				setAddressData();
				mList.clear();
				layoutView.clear();
				
				addressListView.removeAllViewsInLayout();
				addressListView.requestLayout();
				break;
			}
		}
		
	};
	
	private void deleteAddress(UserAddress userAddress) {
		DBHelper dbHelper = new DBHelper(this);		
		dbHelper.deleteTableContent(DBHelper.USER_ADDRESS_TABLE_NAME, "id="+userAddress.getId());		
		dbHelper.close();
		
		new DeleteThread(userAddress.getId()).start(); 
	}
	
	private class DeleteThread extends Thread { 
		int id;
		
		public DeleteThread(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			//向服务器发送删除记录消息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
			WebServiceUtil.getWebServiceResult(url, WebServiceConfig.DELETE_ADDRESS_METHOD, params);
		}
	}
	
	private void setDefaultAddress(UserAddress userAddress) {
		DBHelper dbHelper = new DBHelper(this);
		dbHelper.updateTable(DBHelper.USER_ADDRESS_TABLE_NAME, "isdefault=0", "userid="+userAddress.getUserId());
		dbHelper.updateTable(DBHelper.USER_ADDRESS_TABLE_NAME, "isdefault=1", "id="+userAddress.getId());
		dbHelper.updateTable(DBHelper.USER_TABLE_NAME, "address='"+userAddress.getAddress()+"'", "userid="+userAddress.getUserId());
		dbHelper.close();
		
		new UpdateDefaultAddressThread(userAddress.getId()).start();
	}
	
	private class UpdateDefaultAddressThread extends Thread {
		int id;
		
		public UpdateDefaultAddressThread(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			//向服务器发送删除记录消息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
			WebServiceUtil.getWebServiceResult(url, WebServiceConfig.UPDATE_DEFAULT_ADDRESS_METHOD, params);
			
			mHandler.sendEmptyMessage(UPDATE_DEFALUT);
		}
		
	}
	
	/**
	 * 设置地址数据
	 */
	public void setAddressData() {		
		DBHelper dbHelper = new DBHelper(this);
		
		Cursor cursor = dbHelper.query(DBHelper.USER_ADDRESS_TABLE_NAME, new String[]{"id, userid, username, mobile, address, isdefault"}, 
				"userid="+userId, "isdefault desc");
		addressList = new ArrayList<UserAddress>();
		cursor.moveToFirst();
		do{
			UserAddress userAddress = new UserAddress();
			userAddress.setId(cursor.getInt(cursor.getColumnIndex("id")));
			userAddress.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
			userAddress.setUserName(cursor.getString(cursor.getColumnIndex("username")));
			userAddress.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			userAddress.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			userAddress.setIsDefault(cursor.getInt(cursor.getColumnIndex("isdefault")));
			addressList.add(userAddress);
		} while(cursor.moveToNext());
		
		dbHelper.close();
		
		for(int i=0; i<addressList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserAddress userAddress = addressList.get(i);
			
			map.put("name", userAddress.getUserName());
			map.put("phone", userAddress.getMobile());
			map.put("address", userAddress.getAddress().split("\\|")[1]);
			mData.add(map);
		}		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(activityName != null && activityName.equals("SearchActivity")) {
			LinearLayout v = (LinearLayout) view;
			TextView addressView = (TextView)v.findViewById(R.id.address_list_address);
			String address = addressView.getText().toString();
			
			if(address == null || address.equals("")) {
				
				Intent mIntent2 = new Intent(this, AddressAddActivity.class);
				mIntent2.putExtra("useraddress", addressList.get(0));
				startActivity(mIntent2);
				
				ToastHelper.showToastInBottom(this, "请先完善默认地址", 0);
			} else {
				UserState.setRefreshState(this, true);
		
				for(int i=0; i<addressList.size(); i++) {
					UserAddress userAddress = addressList.get(i);
					if(address.equals(userAddress.getAddress().split("\\|")[1])) {
						SharedPreferences initData = this.getSharedPreferences("account", Context.MODE_PRIVATE);
						Editor mEditor = initData.edit();
						mEditor.putString("mobile", userAddress.getMobile()); 
						mEditor.putString("username", userAddress.getUserName());
						mEditor.putString("address", userAddress.getAddress());
						mEditor.commit();
						break;
					}
				}
				
				onBackPressed();
			}
		
//			Log.d(TAG, addressListView.getChildCount()+"");
//			Log.d(TAG, view.toString());
//			Log.d(TAG, addressView.getText().toString());
		}
		
		
	}
	
	class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =  super.getView(position, convertView, parent);
			if( !layoutView.contains(view) ) {
				LinearLayout layout = (LinearLayout) view;
				TextView edit = (TextView)layout.findViewById(R.id.address_list_edit);
				TextView delet = (TextView)layout.findViewById(R.id.address_list_del);
				TextView defaultView = (TextView)layout.findViewById(R.id.address_list_default);
				
				if(position == 0) {
					delet.setVisibility(View.GONE);
					defaultView.setVisibility(View.GONE);
				}
				
				edit.setOnClickListener(RecevoirAddressActivity.this);
				delet.setOnClickListener(RecevoirAddressActivity.this);
				defaultView.setOnClickListener(RecevoirAddressActivity.this);
				
				List<TextView> mData = new ArrayList<TextView>();
				mData.add(edit);
				mData.add(delet);
				mData.add(defaultView);
				mList.add(mData);
				
				layoutView.add(layout);
			}
			return view;
		}	
	}
	


}
