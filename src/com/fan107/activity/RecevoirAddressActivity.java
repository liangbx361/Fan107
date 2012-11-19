package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		setTestData();
		mAdapter = new MyAdapter(this, mData, R.layout.address_list_item,
				new String[] {"name", "phone", "address"}, new int[] { R.id.name, R.id.phone,
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
		
		//编辑按钮	
		case R.id.edit_btn:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(0) ) ) {
					Log.d(TAG, "edit_btn: " + i);
					break;
				}
			}
			
			break;
		
		//删除按钮
		case R.id.del_btn:
			for(int i=0; i<mList.size(); i++) {
				List<TextView> mData = mList.get(i);
				if(v.equals( mData.get(1) ) ) {
					mHandler.removeMessages(INVALID_VIE);
					mHandler.sendMessage(mHandler.obtainMessage(INVALID_VIE, i, 0));
					Log.d(TAG, "del_btn: " + i);
					break;
				}
			}
			
			break;
		}
	}
	
	/**
	 * 测试数据
	 */
	public void setTestData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "aaaa");
		map.put("phone", "18657436598");
		map.put("address", "金湖西路富民四巷26号");
		mData.add(map);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "sssss");
		map2.put("phone", "18657436598");
		map2.put("address", "金湖西路富民四巷26号");
		mData.add(map2);
		
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("name", "sssss");
//		map3.put("phone", "18657436598");
//		map3.put("address", "金湖西路富民四巷26号");
//		mData.add(map3);
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.d(TAG, addressListView.getChildCount()+"");
		Log.d(TAG, view.toString());
	}
	
	public class MyAdapter extends SimpleAdapter {

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
					mList.remove(msg.arg1);
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
