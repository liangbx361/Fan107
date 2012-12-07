package com.fan107.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.activity.RecevoirAddressActivity.MyAdapter;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderCar;
import com.fan107.data.OrderDish;
import com.fan107.data.ShopInfo;
import com.fan107.dialog.OrderAddressDialog;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderCarActivity extends Activity implements ActivityTemplete, OnClickListener, OnDismissListener{
	private ListView orderList;
	private Button orderButton;
	private TextView totalOldPrice;
	private TextView totalNewPrice;	
	private List<LinearLayout> layoutView;
	private List<List<Button>> mList;
	List<Map<String, String>> listData;
	
	private SimpleAdapter adapter;
	private ShopInfo mInfo;
	private OrderCar mCar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_car_layout);
		
		mInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
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
		orderButton.setOnClickListener(this);
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
			
		case R.id.order_car_order_btn:
			if( checkOrder() ) {
				Dialog dialog = new OrderAddressDialog(this, mCar);
				dialog.setOnDismissListener(this);
				dialog.show();
			}
			
			//生成订单, 向服务器发送订单
//			if( checkOrder() ) {
//				String carJson = mCar.getJsonString();
//				Map<String, Object> data = new HashMap<String, Object>();
//				data.put("orderMesage", carJson);
//				String url = WebServiceConfig.url + WebServiceConfig.ORDER_CHECK_WEB_SERVICE;
//				SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GENERATE_ORDER_METHOD, data);
//			}
			break;
		}
	}
	
	public void onDismiss(DialogInterface dialog) {
		OrderAddressDialog orderDialog = (OrderAddressDialog)dialog;
		if(orderDialog.isConfirm()) {
			ToastHelper.showToastInBottom(this, "订单提交成功, 请等待商定送餐", 0, 100);
		}
	}
	
	private boolean checkOrder() {
		if(mCar.getTotalOldPrice() >= mInfo.getLimitprice()) {				
			//判断是否拥有足够的积分			
			if(mCar.getNeedOrderPoint() <= mCar.userPoint) {
				return true;
			} else {
				String toast = "您的积分不足, 请充值";
				ToastHelper.showToastInBottom(this, toast, 0, 100);
				return false;
			}			
			
		} else {
			String toast = "抱歉,本店" + mInfo.getLimitprice() + "元起送";
			ToastHelper.showToastInBottom(this, toast, 0, 100);
		}
		
		return false;
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
