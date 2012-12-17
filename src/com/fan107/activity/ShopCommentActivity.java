package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.ShopComment;
import com.fan107.data.ShopInfo;
import com.lbx.templete.ActivityTemplete;

import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;

public class ShopCommentActivity extends Activity implements ActivityTemplete {
	private static final int[] starId = {
		R.drawable.star0, R.drawable.star10, R.drawable.star20, R.drawable.star30, R.drawable.star40, R.drawable.star50,
	};
	
	private ShopInfo mInfo;
	private ListView mListView;
	private List<Map<String, String>> mData;
	private List<ShopComment> commentList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_comment_layout);
		
		mInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}
	
	public void findWidget() {
		mListView = (ListView) findViewById(R.id.shop_comment_list);
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		new Thread() {
			@Override
			public void run() {
				mData = new ArrayList<Map<String, String>>();
				List<ShopComment> commentList = getComment();
				for(int i=0; i<commentList.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					ShopComment shopComment = commentList.get(i);
					String time = shopComment.addTime.replace("T", " ");
					int end = time.indexOf(".");
					time = time.substring(0, end);
					map.put("username", shopComment.userName);
					map.put("flavor", String.valueOf(shopComment.tastePoint));
					map.put("environment", String.valueOf(shopComment.milieuPoint));
					map.put("server", String.valueOf(shopComment.servicePoint));
					map.put("comment", shopComment.commentContent);
					map.put("time", time);
					
					mData.add(map);
				}
				
				mHandler.sendEmptyMessage(0);
			}
		}.start();
		
	}

	private List<ShopComment> getComment() {
		commentList = new ArrayList<ShopComment>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shopId", mInfo.getShopId());
		String url = WebServiceConfig.url + WebServiceConfig.SHOP_INFO_WEB_SERVICE;
		SoapObject commentObject = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_SHOP_COMMENT_METHOD, params);
		
		commentObject = WebServiceUtil.getChildSoapObject(commentObject, new int[]{0});
		for(int i=0; i<commentObject.getPropertyCount(); i++) {
			SoapObject childObject = (SoapObject) commentObject.getProperty(i);
			ShopComment shopComment = new ShopComment();
			
			shopComment.id = WebServiceUtil.getSoapObjectInt(childObject, "Id");
			shopComment.shopId = WebServiceUtil.getSoapObjectInt(childObject, "ShopId");
			shopComment.shopName = WebServiceUtil.getSoapObjectString(childObject, "ShopName");
			shopComment.userId = WebServiceUtil.getSoapObjectInt(childObject, "UserId");
			shopComment.userName = WebServiceUtil.getSoapObjectString(childObject, "UserName");
			shopComment.totalPoint = WebServiceUtil.getSoapObjectInt(childObject, "TotalPoint");
			shopComment.tastePoint = WebServiceUtil.getSoapObjectInt(childObject, "TastePoint");
			shopComment.milieuPoint = WebServiceUtil.getSoapObjectInt(childObject, "MilieuPoint");
			shopComment.servicePoint = WebServiceUtil.getSoapObjectInt(childObject, "ServicePoint");
			shopComment.commentContent = WebServiceUtil.getSoapObjectString(childObject, "CommentContent");
			shopComment.checkStatus = WebServiceUtil.getSoapObjectInt(childObject, "CheckStatus");
			shopComment.addTime = WebServiceUtil.getSoapObjectString(childObject, "AddTime");
			
			commentList.add(shopComment);
		}
		
		return commentList;
	}
	
	private class CommentAdapter extends SimpleAdapter {

		public CommentAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			LinearLayout layout = (LinearLayout) v;
			ImageView ratingBar = (ImageView) layout.findViewById(R.id.shop_comment_lsit_point_ratingbar);	
			ratingBar.setImageDrawable(getResources().getDrawable(starId[commentList.get(position).totalPoint]));
			
			return v;
		}
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mListView.setAdapter( new CommentAdapter(ShopCommentActivity.this, mData, R.layout.shop_comment_list, 
					new String[]{"username", "flavor", "environment", "server", "comment", "time"},
					new int[]{R.id.shop_comment_list_username, R.id.shop_comment_lsit_flavor_point, 
					R.id.shop_comment_lsit_environment_point, R.id.shop_comment_lsit_server_point,
					R.id.shop_comment_list_comment, R.id.shop_comment_list_time})
					);
			
			mListView.requestLayout();
		}
		
	};
}
