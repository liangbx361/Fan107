package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.activity.ShopInfoActivity.LoadProductThread;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.Product;
import com.fan107.data.ProductType;
import com.fan107.data.ShopInfo;

import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SimpleExpandableListAdapter;

public class ShopOrderActivity extends ExpandableListActivity {	
	private static final int SET_ADAPTER = 1;
	
	private List<Map<String, String>> dishsNameList;
	private List<List<Map<String, String>>> dishList;
	private List<Map<String, List<Product>>> ProductList;
	private SimpleExpandableListAdapter mAdapter;
	private ShopInfo mInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.shop_order_layout);
		
		Intent mIntent  = getIntent();
		mInfo = (ShopInfo) mIntent.getSerializableExtra("shopInfo");
				
		dishsNameList = new ArrayList<Map<String,String>>();
		dishList = new ArrayList<List<Map<String, String>>>();
		ProductList = new ArrayList<Map<String,List<Product>>>();
		LoadProductThread mThread = new LoadProductThread();
		mThread.start();
		
		mAdapter = new SimpleExpandableListAdapter(this, 
				dishsNameList, R.layout.dishes_list, 
				new String[]{"dishName"}, new int[]{R.id.dishs_name}, 
				
				dishList,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{"dishName", "price"},
                new int[] { android.R.id.text1, android.R.id.text2 });
				
//				dishList, R.layout.dish_list, 
//				new String[]{"dishName", "price"}, new int[]{R.id.dish_name, R.id.dish_price});
								
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what) {
			case SET_ADAPTER:
				ShopOrderActivity.this.setListAdapter(mAdapter);
				onContentChanged();
				break;
			}
		}
		
	};
	
	class LoadProductThread extends Thread {

		@Override
		public void run() {
			
			String url = WebServiceConfig.url + WebServiceConfig.SHOP_INFO_WEB_SERVICE;
			int shopId = mInfo.getShopId();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("shopId", shopId);
			SoapObject proudctType = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_PRODUCT_LIST_METHOD, params);
			List<ProductType> pList = saveProudctType(proudctType);
			
			for(int i=0; i<pList.size(); i++) {
				ProductType pType = pList.get(i);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("shopId", shopId);
				params1.put("productId", pType.getId());
				SoapObject proudctSoObject = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_PRODUCT_METHOD, params1);
				List<Product> product = saveProudct(proudctSoObject);
				
				//获得菜品名称
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
		 * @param proudctType
		 * @return
		 */
		private List<ProductType> saveProudctType(SoapObject proudctType) {
			List<ProductType> pList = new ArrayList<ProductType>();
			int[] childList = {0};
			SoapObject childs = WebServiceUtil.getChildSoapObject(proudctType, childList);
			for(int i=0; i<childs.getPropertyCount(); i++) {
				SoapObject child = (SoapObject) childs.getProperty(i);
				ProductType pType = new ProductType();
				pType.setId( WebServiceUtil.getSoapObjectInt(child, "Id") );
				pType.setTypeName( WebServiceUtil.getSoapObjectString(child, "TypeName") );
				pType.setShopId( WebServiceUtil.getSoapObjectInt(child, "ShopId") );
				
				pList.add(pType);
			}
			
			return pList;
		}
		
		/**
		 * 存储具体的菜品
		 * @param pSoObject
		 * @return
		 */
		private List<Product> saveProudct(SoapObject pSoObject) {
			List<Product> pList = new ArrayList<Product>();
			List<Map<String, String>> dishChildList = new ArrayList<Map<String,String>>();
			int[] childList = {0};
			SoapObject childs = WebServiceUtil.getChildSoapObject(pSoObject, childList);
			for(int i=0; i<childs.getPropertyCount(); i++) {
				SoapObject child = (SoapObject) childs.getProperty(i);
				Product product = new Product();
				product.setId(WebServiceUtil.getSoapObjectInt(child, "Id"));
				product.setImgSrc(WebServiceUtil.getSoapObjectString(child, "ImgSrc"));
				product.setPrice(WebServiceUtil.getSoapObjectFloat(child, "Price"));
				product.setPrice2(WebServiceUtil.getSoapObjectFloat(child, "Price2"));
				product.setProductName(WebServiceUtil.getSoapObjectString(child, "ProductName"));
				product.setSellStatus(WebServiceUtil.getSoapObjectInt(child, "SellStatus"));
				product.setShopId(WebServiceUtil.getSoapObjectInt(child, "ShopId"));
				product.setSortId(WebServiceUtil.getSoapObjectInt(child, "SortId"));
				product.setTypeId(WebServiceUtil.getSoapObjectInt(child, "TypeId"));
				
				pList.add(product);
				
				//获得单个菜名和菜价				
				Map<String, String> child1 = new HashMap<String, String>();
				child1.put("dishName", product.getProductName());
				dishChildList.add(child1);
				Map<String, String> child2 = new HashMap<String, String>();
				child2.put("price", String.valueOf(product.getPrice()));
				dishChildList.add(child2);
			}
			
			dishList.add(dishChildList);
			return pList;
		}
		
	}

}
