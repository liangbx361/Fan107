package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.data.OrderCar;
import com.fan107.data.OrderDish;
import com.fan107.data.ShopInfo;
import com.fan107.data.UserInfo;
import com.fan107.dialog.OrderAddressDialog;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

public class OrderCarActivity extends Activity implements ActivityTemplete, OnClickListener, OnDismissListener{
	
	private ListView orderList;
	private Button orderButton;
	private TextView totalOldPrice;
	private TextView totalNewPrice;	
	private List<LinearLayout> layoutView;
	private List<List<Button>> mList;
	List<Map<String, String>> listData;
	
	private ShopInfo mInfo;
	private OrderCar mCar;
	private UserInfo mUserInfo;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_car_layout);
		
		mInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
		mCar = (OrderCar) getIntent().getSerializableExtra("orderCar");
		mUserInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
		
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
		//����
		case R.id.order_list_dish_add_num:
			for(int i=0; i<mList.size(); i++) {
				List<Button> mData = mList.get(i);
				if(v.equals( mData.get(0) ) ) {
					mCar.addItemNum(i);
					mHandler.sendEmptyMessage(MessageCode.ADD_NUM);
				}
			}
			break;
		
		//ɾ��
		case R.id.order_list_dish_sub_num:
			for(int i=0; i<mList.size(); i++) {
				List<Button> mData = mList.get(i);
				if(v.equals( mData.get(1) ) ) {
					mCar.subItemNum(i);
					mHandler.sendEmptyMessage(MessageCode.SUB_NUM);
				}
			}
			break;
			
		case R.id.order_car_order_btn:
			if( checkOrder() ) {
				Dialog dialog = new OrderAddressDialog(this, R.style.myDialogTheme, mCar);
				dialog.setOnDismissListener(this);
				dialog.show();
			}
			break;
		}
	}
	
	public void onDismiss(DialogInterface dialog) {
		OrderAddressDialog orderDialog = (OrderAddressDialog)dialog;
		if(orderDialog.isConfirm()) {			
			ToastHelper.showToastInBottom(this, "�����ύ�ɹ�, ��ȴ��̶��Ͳ�", 0);
			Intent mIntent = new Intent(this, OrderListActivity.class);
			mIntent.putExtra("userInfo", mUserInfo);
			startActivity(mIntent);
		}
	}
	
	
	
	@Override
	public void onBackPressed() {
		Intent intent=new Intent();  
        intent.putExtra("orderCar", mCar);  
        setResult(MessageCode.RETURN_ORDER_CAR, intent);
        finish();
	}

	private boolean checkOrder() {
		if(mCar.getTotalOldPrice() >= mInfo.getLimitprice()) {				
			//�ж��Ƿ�ӵ���㹻�Ļ���			
			if(mCar.getNeedOrderPoint() <= mCar.userPoint) {
				return true;
			} else {
				String toast = "���Ļ��ֲ���, ���ֵ";
				ToastHelper.showToastInBottom(this, toast, 0);
				return false;
			}			
			
		} else {
			String toast = "��Ǹ,����" + mInfo.getLimitprice() + "Ԫ����";
			ToastHelper.showToastInBottom(this, toast, 0);
		}
		
		return false;
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what) {
			case MessageCode.ADD_NUM:
			case MessageCode.SUB_NUM:
				layoutView.clear();
				mList.clear();
				listData.clear();
				listData = getCarListData(mCar);
				
				orderList.setAdapter(new MyAdapter(OrderCarActivity.this, listData, 
						R.layout.order_car_list, 
						new String[]{"disName", "dishNum"}, 
						new int[]{R.id.order_list_dish_name, R.id.order_list_dish_num}));
				
				orderList.removeAllViewsInLayout();
				orderList.requestLayout();
				
				totalOldPrice.setText(mCar.getTotalOldPrice()+"");
				totalNewPrice.setText(mCar.getTotalNewPrice()+"");
				break;
			}
		}
		
	};
	
	public class MyAdapter extends SimpleAdapter {

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
