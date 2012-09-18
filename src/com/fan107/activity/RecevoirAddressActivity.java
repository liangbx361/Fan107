package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RecevoirAddressActivity extends Activity implements
		ActivityTemplete, OnClickListener {

	private Button addAddressButton;
	private ListView addressListView;
	private SimpleAdapter mAdapter;
	private List<Map<String, Object>> mData;

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
	public void findWidget() {
		addAddressButton = (Button) findViewById(R.id.add_adress_btn);
		addressListView = (ListView) findViewById(R.id.addressListView);

	}

	@Override
	public void setWidgetListenter() {
		addAddressButton.setOnClickListener(this);
	}

	@Override
	public void setWidgetPosition() {

	}

	@Override
	public void setWidgetAttribute() {
		mData = new ArrayList<Map<String,Object>>();
		setTestData();
		mAdapter = new SimpleAdapter(this, mData, R.layout.address_list_item,
				new String[] {"name", "phone", "address"}, new int[] { R.id.name, R.id.phone,
						R.id.address });
		
		addressListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_adress_btn:
			Intent mIntent = new Intent();
			mIntent.setClass(this, AddressAddActivity.class);
			startActivity(mIntent);
			break;
		}
	}
	
	/**
	 * 测试数据
	 */
	public void setTestData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "梁宝贤");
		map.put("phone", "18657436598");
		map.put("address", "金湖西路富民四巷26号");
		mData.add(map);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "梁宝贤");
		map2.put("phone", "18657436598");
		map2.put("address", "金湖西路富民四巷26号");
		mData.add(map2);
	}

}
