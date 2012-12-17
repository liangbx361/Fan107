package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderInfo;
import com.fan107.data.UserInfo;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

public class OrderListActivity extends Activity implements ActivityTemplete, OnItemClickListener {
	
	private UserInfo mInfo;
	private List<OrderInfo> orderList;
	private List<Map<String, String>> mData;
	
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_layout);
		
		mInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		mListView = (ListView)findViewById(R.id.order_list);
	}

	public void setWidgetListenter() {
		mListView.setOnItemClickListener(this);
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		
		new Thread() {
			@Override
			public void run() {
				orderList = getOrderInfoList();
				mData = new ArrayList<Map<String, String>>();
				for(int i=0; i<orderList.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					OrderInfo orderInfo = orderList.get(i);
					String time = orderInfo.addTime.replace("T", " ");
					int end = time.indexOf(".");
					time = time.substring(0, end);
					
					map.put("orderNo", orderInfo.orderNo);
					map.put("shopName", orderInfo.shopName);
					map.put("totalPrice", String.valueOf(orderInfo.totalPrice));
					map.put("addTime", time);
					mData.add(map);
				}
				
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	
	Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			mListView.setAdapter( new SimpleAdapter(OrderListActivity.this, mData, R.layout.order_lsit, 
					new String[]{"orderNo", "shopName", "totalPrice", "addTime"}, 
					new int[]{R.id.order_list_orderno, R.id.order_list_shop_name, 
					R.id.order_list_totalprice, R.id.order_list_time})
					);
			
			mListView.requestLayout();
		}
		
	};
	
	private List<OrderInfo> getOrderInfoList() {
		List<OrderInfo> ordeList = new ArrayList<OrderInfo>();
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("userid", mInfo.getUserid());
		String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_ORDER_LIST_METHOD, data);
		
		result = WebServiceUtil.getChildSoapObject(result, new int[]{0});
		for(int i=0; i<result.getPropertyCount(); i++) {
			SoapObject childObject = (SoapObject) result.getProperty(i);
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.id = WebServiceUtil.getSoapObjectInt(childObject, "Id");
			orderInfo.orderNo = WebServiceUtil.getSoapObjectString(childObject, "OrderNo");
			orderInfo.shopId = WebServiceUtil.getSoapObjectInt(childObject, "ShopId");
			orderInfo.shopName = WebServiceUtil.getSoapObjectString(childObject, "ShopName");
			orderInfo.totalPrice = WebServiceUtil.getSoapObjectFloat(childObject, "TotalPrice");
			orderInfo.orderPoint = WebServiceUtil.getSoapObjectFloat(childObject, "OrderPoint");
			orderInfo.userId = WebServiceUtil.getSoapObjectInt(childObject, "UserId");
			orderInfo.username = WebServiceUtil.getSoapObjectString(childObject, "UserName");
			orderInfo.userTel = WebServiceUtil.getSoapObjectString(childObject, "UserTel");
			orderInfo.userAddress = WebServiceUtil.getSoapObjectString(childObject, "UserAddress");
			orderInfo.areaId = WebServiceUtil.getSoapObjectInt(childObject, "AreaId");
			orderInfo.remark = WebServiceUtil.getSoapObjectString(childObject, "Remark");
			orderInfo.orderStatus = WebServiceUtil.getSoapObjectInt(childObject, "OrderStatus");
			orderInfo.payStatus = WebServiceUtil.getSoapObjectInt(childObject, "PayStatus");
			orderInfo.addTime = WebServiceUtil.getSoapObjectString(childObject, "AddTime");
			
			ordeList.add(orderInfo);
		}
		
		return ordeList;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, OrderDetailActivity.class);
		intent.putExtra("orderInfo", orderList.get(position));
		startActivity(intent);
	}
}
