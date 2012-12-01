package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fan107.R;
import com.fan107.activity.RecevoirAddressActivity.MyAdapter;
import com.fan107.data.OrderCar;
import com.fan107.data.OrderDish;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderCarActivity extends Activity implements ActivityTemplete, OnClickListener{
	private ListView orderList;
	private Button orderButton;
	private TextView totalOldPrice;
	private TextView totalNewPrice;	
	private List<LinearLayout> layoutView;
	private List<List<Button>> mList;
	List<Map<String, String>> listData;
	
	private SimpleAdapter adapter;
	private OrderCar mCar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_car_layout);
		
		mCar = (OrderCar) getIntent().getSerializableExtra("orderCar");
		
		findWidget();
		setWidgetListenter();
		setWidgetAttribute();		
	}
	
	public void findWidget() {
		orderList = (ListView) findViewById(R.id.order_car_list);
		orderButton = (Button) findViewById(R.id.order_car_order_btn);
		totalOldPrice = (TextView) findViewById(R.id.order_car_total_old_price);
		totalNewPrice = (TextView) findViewById(R.id.order_car_total_new_price);
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		
		layoutView = new ArrayList<LinearLayout>();
		mList = new ArrayList<List<Button>>();
		listData = getCarListData(mCar);
		
		orderList.setAdapter(new MyAdapter(this, listData, 
				R.layout.order_car_list, 
				new String[]{"disName", "dishNum"}, 
				new int[]{R.id.order_list_dish_name, R.id.order_list_dish_num}));
		
		totalOldPrice.setText(mCar.getTotalOldPrice()+"");
		totalNewPrice.setText(mCar.getTotalNewPrice()+"");
	}
	
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.order_list_dish_add_num:
			break;
			
		case R.id.order_list_dish_sub_num:
			break;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

		}
		
	};
	
	private class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			
			if( !layoutView.contains(view) ) {
				LinearLayout layout = (LinearLayout) view;
				
				Button addBtn = (Button) layout.findViewById(R.id.order_list_dish_add_num);
				Button subBtn = (Button) layout.findViewById(R.id.order_list_dish_sub_num);
				
				addBtn.setOnClickListener(OrderCarActivity.this);
				subBtn.setOnClickListener(OrderCarActivity.this);
				
				List<Button> mData = new ArrayList<Button>();
				mData.add(addBtn);
				mData.add(subBtn);
				mList.add(mData);
				
				layoutView.add(layout);
			}
			
			return view;
		}
	
	}
	
	public List<Map<String, String>> getCarListData(OrderCar orderCar) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				
		for(int i=0; i<mCar.getItemNum(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			OrderDish orderDish = mCar.getItem(i);
			map.put("disName", orderDish.getDishName());
			map.put("dishNum", orderDish.getOrderNum() + "");
			
			list.add(map);
		}
		
		return list;
	}

}