package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.common.helper.MessageCode;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderInfo;
import com.fan107.data.OrderItem;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DishDetailActivity extends Activity implements ActivityTemplete {
	private ListView dishList;	
	private List<Map<String, String>> mData;
	private OrderInfo mOrderInfo;
	private List<OrderItem> mOrderItemList;
	
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detail_layout);
		
		mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}
	
	public void findWidget() {
		dishList = (ListView) findViewById(R.id.dish_detail_dish_list);		
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		mData = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("prouctName", "菜名");
		map.put("buyNum", "数量");
		map.put("price", "金额");
		mData.add(map);
		
		initProgressDialog(this);
		
		new Thread() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(MessageCode.SHOW_DIALOG);
				mOrderItemList = getOrderItemList();
				for(int i=0; i<mOrderItemList.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					OrderItem orderItem = mOrderItemList.get(i);
					
					map.put("prouctName", orderItem.productName);
					map.put("buyNum", orderItem.buyNum + "");
					map.put("price", orderItem.price * orderItem.buyNum + "");
					
					mData.add(map);
				}
				
				mHandler.sendEmptyMessage(MessageCode.DIS_ORDER_ITEM);
			}
		}.start();
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg){
			
			switch(msg.what) {
			case MessageCode.SHOW_DIALOG:
				mProgressDialog.show();
				break;
				
			case MessageCode.DIS_ORDER_ITEM:
				mProgressDialog.dismiss();
				dishList.setAdapter(new SimpleAdapter(DishDetailActivity.this, mData, R.layout.order_item_list, 
						new String[]{"prouctName", "buyNum", "price"}, 
						new int[]{R.id.order_item_prouct_name, R.id.order_item_buy_num, R.id.order_item_price}));
				
				dishList.requestLayout();
				break;
			}
		}
		
	};
	
	private List<OrderItem> getOrderItemList() {
		List<OrderItem> ordeItemList = new ArrayList<OrderItem>();
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderNo", mOrderInfo.orderNo);
		String url = WebServiceConfig.url + WebServiceConfig.USER_ACCOUNT_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_ORDER_ITEM_LIST_METHOD, data);
		
		result = WebServiceUtil.getChildSoapObject(result, new int[]{0});
		for(int i=0; i<result.getPropertyCount(); i++) {
			SoapObject childObject = (SoapObject) result.getProperty(i);
			OrderItem orderItem = new OrderItem();
			orderItem.id = WebServiceUtil.getSoapObjectInt(childObject, "Id");
			orderItem.productId = WebServiceUtil.getSoapObjectInt(childObject, "ProductId");
			orderItem.productName = WebServiceUtil.getSoapObjectString(childObject, "ProductName");
			orderItem.buyNum = WebServiceUtil.getSoapObjectInt(childObject, "BuyNum");
			orderItem.price = WebServiceUtil.getSoapObjectFloat(childObject, "Price");
			
			ordeItemList.add(orderItem);
		}
		
		return ordeItemList;
	}
	
	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("数据加载中");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
		// mProgressDialog.setOnShowListener(this);
		// mProgressDialog.setOnDismissListener((MainActivity)mContext);
	}
}
