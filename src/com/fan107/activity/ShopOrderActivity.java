package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.activity.ShopInfoActivity.LoadProductThread;
import com.fan107.common.OrderState;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderCar;
import com.fan107.data.OrderDish;
import com.fan107.data.Product;
import com.fan107.data.ProductType;
import com.fan107.data.ShopInfo;
import com.fan107.dialog.OrderDishDialog;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class ShopOrderActivity extends ExpandableListActivity implements
		ActivityTemplete, ExpandableListView.OnChildClickListener,
		OnDismissListener {
	private static final String TAG = "ShopOrderActivity";
	private static final int SET_ADAPTER = 1;

	private List<Map<String, String>> dishsNameList;
	private List<List<Map<String, String>>> dishList;
	private List<Map<String, List<Product>>> ProductList;
	private SimpleExpandableListAdapter mAdapter;
	private ExpandableListView mListView;
	private ShopInfo mInfo;
	private OrderCar mCar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shop_order_layout);

		Intent mIntent = getIntent();
		mInfo = (ShopInfo) mIntent.getSerializableExtra("shopInfo");
		mCar = (OrderCar) mIntent.getSerializableExtra("orderCar");

		// findWidget();
		// setWidgetListenter();
		// setWidgetAttribute();

		dishsNameList = new ArrayList<Map<String, String>>();
		dishList = new ArrayList<List<Map<String, String>>>();
		ProductList = new ArrayList<Map<String, List<Product>>>();
		LoadProductThread mThread = new LoadProductThread();
		mThread.start();

		// mAdapter = new SimpleExpandableListAdapter(this,
		// dishsNameList, R.layout.dishes_list,
		// new String[]{"dishName"}, new int[]{R.id.dishs_name},
		//
		// dishList, R.layout.dish_list,
		// new String[]{"dishName", }, new int[]{R.id.dish_name});

		setWidgetAttribute();
		
		IntentFilter intentFilter = new IntentFilter("fan107.orderCar");
		registerReceiver(mReceiver, intentFilter);  

	}
	
	public void findWidget() {
		mListView = getExpandableListView();
	}

	public void setWidgetListenter() {
		mListView.setOnChildClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		mAdapter = new SimpleExpandableListAdapter(this, dishsNameList,
				R.layout.dishes_list, new String[] { "dishName" },
				new int[] { R.id.dishs_name },

				dishList, R.layout.dish_list, new String[] { "dishName",
						"price", },
				new int[] { R.id.dish_name, R.id.dish_price });
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SET_ADAPTER:
				ShopOrderActivity.this.setListAdapter(mAdapter);
				onContentChanged();
				break;
			}
		}

	};

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		if(mCar == null) {
			ToastHelper.showToastInBottom(this, "请先登录, 再进行订餐", 0);
			
		} else if (OrderState.checkTime(mInfo.getOrdertime())) {
			OrderDish mOrderDish = new OrderDish();
			String disTypeName = dishsNameList.get(groupPosition).get(
					"dishName");
			Product mProduct = (Product) ProductList.get(groupPosition)
					.get(disTypeName).get(childPosition);
			mOrderDish.setDishName(mProduct.getProductName());
			mOrderDish.setOrderNum(1);
			mOrderDish.setOldPrice(mProduct.getPrice());
			mOrderDish.setNewPrice(mProduct.getPrice2());
			mOrderDish.setProductId(mProduct.getId());

			OrderDishDialog mDialog = new OrderDishDialog(this, R.style.myDialogTheme, mOrderDish);
			mDialog.setOnDismissListener(this);
			mDialog.show();

			Log.d(TAG, "groupPosition: " + groupPosition + " childPosition:"
					+ childPosition + " id:" + id);
			Log.d(TAG,
					dishList.get(groupPosition).get(childPosition)
							.get("dishName")
							+ " 一份");
		} else {
			ToastHelper.showToastInBottom(this, "非订餐时段,请自行电话定餐", 0);
		}
		return false;
	}

	class LoadProductThread extends Thread {

		@Override
		public void run() {

			String url = WebServiceConfig.url
					+ WebServiceConfig.SHOP_INFO_WEB_SERVICE;
			int shopId = mInfo.getShopId();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("shopId", shopId);
			SoapObject proudctType = WebServiceUtil.getWebServiceResult(url,
					WebServiceConfig.GET_PRODUCT_LIST_METHOD, params);
			List<ProductType> pList = saveProudctType(proudctType);

			for (int i = 0; i < pList.size(); i++) {
				ProductType pType = pList.get(i);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("shopId", shopId);
				params1.put("productId", pType.getId());
				SoapObject proudctSoObject = WebServiceUtil
						.getWebServiceResult(url,
								WebServiceConfig.GET_PRODUCT_METHOD, params1);
				List<Product> product = saveProudct(proudctSoObject);

				// 获得菜品名称
				Map<String, String> name = new HashMap<String, String>();
				name.put("dishName", pType.getTypeName());
				dishsNameList.add(name);

				Map<String, List<Product>> pMap = new HashMap<String, List<Product>>();
				pMap.put(pType.getTypeName(), product);
				ProductList.add(pMap);

			}

			mHandler.sendEmptyMessage(SET_ADAPTER);

		}

		/**
		 * 存储菜的类型
		 * 
		 * @param proudctType
		 * @return
		 */
		private List<ProductType> saveProudctType(SoapObject proudctType) {
			List<ProductType> pList = new ArrayList<ProductType>();
			int[] childList = { 0 };
			SoapObject childs = WebServiceUtil.getChildSoapObject(proudctType,
					childList);
			for (int i = 0; i < childs.getPropertyCount(); i++) {
				SoapObject child = (SoapObject) childs.getProperty(i);
				ProductType pType = new ProductType();
				pType.setId(WebServiceUtil.getSoapObjectInt(child, "Id"));
				pType.setTypeName(WebServiceUtil.getSoapObjectString(child,
						"TypeName"));
				pType.setShopId(WebServiceUtil
						.getSoapObjectInt(child, "ShopId"));

				pList.add(pType);
			}

			return pList;
		}

		/**
		 * 存储具体的菜品
		 * 
		 * @param pSoObject
		 * @return
		 */
		private List<Product> saveProudct(SoapObject pSoObject) {
			List<Product> pList = new ArrayList<Product>();
			List<Map<String, String>> dishChildList = new ArrayList<Map<String, String>>();
			int[] childList = { 0 };
			SoapObject childs = WebServiceUtil.getChildSoapObject(pSoObject,
					childList);
			for (int i = 0; i < childs.getPropertyCount(); i++) {
				SoapObject child = (SoapObject) childs.getProperty(i);
				Product product = new Product();
				product.setId(WebServiceUtil.getSoapObjectInt(child, "Id"));
				product.setImgSrc(WebServiceUtil.getSoapObjectString(child,
						"ImgSrc"));
				product.setPrice(WebServiceUtil.getSoapObjectFloat(child,
						"Price"));
				product.setPrice2(WebServiceUtil.getSoapObjectFloat(child,
						"Price2"));
				product.setProductName(WebServiceUtil.getSoapObjectString(
						child, "ProductName"));
				product.setSellStatus(WebServiceUtil.getSoapObjectInt(child,
						"SellStatus"));
				product.setShopId(WebServiceUtil.getSoapObjectInt(child,
						"ShopId"));
				product.setSortId(WebServiceUtil.getSoapObjectInt(child,
						"SortId"));
				product.setTypeId(WebServiceUtil.getSoapObjectInt(child,
						"TypeId"));

				pList.add(product);

				// 获得单个菜名和菜价
				Map<String, String> childMap = new HashMap<String, String>();
				childMap.put("dishName", product.getProductName());
				childMap.put("price", String.valueOf(product.getPrice()));
				dishChildList.add(childMap);
			}

			dishList.add(dishChildList);
			return pList;
		}

	}

	public void onDismiss(DialogInterface dialog) {
		OrderDishDialog mDialog = (OrderDishDialog) dialog;
		OrderDish mOrderDish = mDialog.getOrderDish();
		if (mDialog.isConfirm()) {
			mCar.addItem(mOrderDish);
			String message = "已点" + mOrderDish.getDishName() + " "
					+ mOrderDish.getOrderNum() + "份";
			ToastHelper.showToastInBottom(this, message, 0);
			
			//发送一个广播更新餐车信息
			Intent intent = new Intent();
			intent.putExtra("orderCar", mCar);
			intent.setAction("fan107.orderCar_1");
			sendBroadcast(intent);
		}
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			mCar = (OrderCar) intent.getSerializableExtra("orderCar");
		}
	};
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
