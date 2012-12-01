package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderCar;
import com.fan107.data.ShopInfo;
import com.fan107.data.Product;
import com.fan107.data.ProductType;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShopInfoActivity extends ActivityGroup implements ActivityTemplete, OnClickListener{
	private List<Map<String, List<Product>>> ProductList;
	private ShopInfo mInfo;
	private OrderCar mCar;
	
	private LinearLayout shopInfo;
	private LinearLayout shopOrder;
	private LinearLayout shopComment;
	private LinearLayout containerBody;
	private Button myOrder;
	private TextView shopName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_commodity_introduct);
		
		Intent mIntent  = getIntent();
		mInfo = (ShopInfo) mIntent.getSerializableExtra("shopInfo");
		mCar = new OrderCar();
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
		
		ProductList = new ArrayList<Map<String,List<Product>>>();
		
		LoadProductThread mThread = new LoadProductThread();
		mThread.start();
	}

	public void findWidget() {
		shopInfo = (LinearLayout) findViewById(R.id.shop_info);
		shopOrder = (LinearLayout) findViewById(R.id.shop_order);
		shopComment = (LinearLayout) findViewById(R.id.shop_comment);
		containerBody = (LinearLayout) findViewById(R.id.containerBody);
		myOrder = (Button) findViewById(R.id.my_order);
		shopName = (TextView) findViewById(R.id.shop_name_title);
	}

	public void setWidgetListenter() {
		shopInfo.setOnClickListener(this);
		shopOrder.setOnClickListener(this);
		shopComment.setOnClickListener(this);
		myOrder.setOnClickListener(this);
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		shopName.setText(mInfo.getShopname());
		
		// 默认进入商店简介页面
		containerBody.removeAllViews();
		containerBody.addView(getLocalActivityManager().startActivity(
                "简介",
                new Intent(ShopInfoActivity.this, ShopIntroductionActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("shopInfo", mInfo)                        )                        
                .getDecorView());
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.shop_info:
			containerBody.removeAllViews();
			containerBody.addView(getLocalActivityManager().startActivity(
                    "简介",
                    new Intent(ShopInfoActivity.this, ShopIntroductionActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("shopInfo", mInfo))
                    .getDecorView());
			break;
			
		case R.id.shop_order:
			containerBody.removeAllViews();
			containerBody.addView(getLocalActivityManager().startActivity(
                    "点餐",
                    new Intent(ShopInfoActivity.this, ShopOrderActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("shopInfo", mInfo)
                            .putExtra("orderCar", mCar))
                    .getDecorView());
			break;
			
		case R.id.shop_comment:
			containerBody.removeAllViews();
			containerBody.addView(getLocalActivityManager().startActivity(
                    "点评",
                    new Intent(ShopInfoActivity.this, ShopCommentActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("shopInfo", mInfo))
                    .getDecorView());
			break;
			
		case R.id.my_order:
			Intent orderCar = new Intent(ShopInfoActivity.this, OrderCarActivity.class);
			orderCar.putExtra("orderCar", mCar);
			startActivity(orderCar);
			break;
		}
	}
	
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
				
				Map<String, List<Product>> pMap = new HashMap<String, List<Product>>();
				pMap.put(pType.getTypeName(), product);
				ProductList.add(pMap);
			}
			
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
			}
			return pList;
		}
		
	}


}
