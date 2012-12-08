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

	private Button addAddressButton;
	private ListView addressListView;
	private MyAdapter mAdapter;
	private List<Map<String, Object>> mData;
	private List<List<TextView>> mList;
	private List<LinearLayout> layoutView;
	
	private List<UserAddress> addressList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recevoir_address);

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();		
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		
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
						R.id.address });
		
		mList = new ArrayList<List<TextView>>();
		layoutView = new ArrayList<LinearLayout>();
		
		addressListView.setAdapter(mAdapter);
		addressListView.setOnItemClickListener(this);
		Log.d(TAG, addressListView.getChildCount()+"");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_adress_btn:
			Intent mIntent = new Intent();
			mIntent.setClass(this, AddressAddActivity.class);
			startActivity(mIntent);
			break;
		
		//�༭��ť	
		case R.id.edit_btn:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(0) ) ) {
					Log.d(TAG, "edit_btn: " + i);
					break;
				}
			}
			
			break;
		
		//ɾ����ť
		case R.id.del_btn:
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
		}
	}
	
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
			//�����������ɾ����¼��Ϣ
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
			WebServiceUtil.getWebServiceResult(url, WebServiceConfig.DELETE_ADDRESS_METHOD, params);
		}
	}
	
	/**
	 * ���õ�ַ����
	 */
	public void setAddressData() {		
		DBHelper dbHelper = new DBHelper(this);
		
		Cursor cursor = dbHelper.query(DBHelper.USER_ADDRESS_TABLE_NAME, new String[]{"id, username, mobile, address"}, null);
		addressList = new ArrayList<UserAddress>();
		cursor.moveToFirst();
		do{
			UserAddress userAddress = new UserAddress();
			userAddress.setId(cursor.getInt(cursor.getColumnIndex("id")));
			userAddress.setUserName(cursor.getString(cursor.getColumnIndex("username")));
			userAddress.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			userAddress.setAddress(cursor.getString(cursor.getColumnIndex("address")));
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
		LinearLayout v = (LinearLayout) view;
		TextView addressView = (TextView)v.findViewById(R.id.address);
		String address = addressView.getText().toString();
		
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
		
		Log.d(TAG, addressListView.getChildCount()+"");
		Log.d(TAG, view.toString());
		Log.d(TAG, addressView.getText().toString());
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
				TextView edit = (TextView)layout.findViewById(R.id.edit_btn);
				TextView delet = (TextView)layout.findViewById(R.id.del_btn);
			
				edit.setOnClickListener(RecevoirAddressActivity.this);
				delet.setOnClickListener(RecevoirAddressActivity.this);
			
				List<TextView> mData = new ArrayList<TextView>();
				mData.add(edit);
				mData.add(delet);
				mList.add(mData);
				
				layoutView.add(layout);
			}
			return view;
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
			}
		}
		
	};

}
