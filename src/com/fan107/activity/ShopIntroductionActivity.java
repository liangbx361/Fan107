package com.fan107.activity;

import java.io.File;

import com.fan107.R;
import com.fan107.data.ShopInfo;
import com.lbx.cache.FileCache;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShopIntroductionActivity extends Activity implements ActivityTemplete{
	private ShopInfo mInfo;
	private ImageView shopPic;
	private TextView flavorPoint;
	private TextView environmentPoint;
	private TextView serverPoint;
	private ImageView mStarView;
	
	private TextView limitPrice;
	private TextView sendPrice;
	private TextView sendTime;
	private TextView payment;
	
	private TextView openTime;
	private TextView scheduledTime;
	
	private TextView phone;
	private TextView addressView;
	
	private static final int[] starId = {
		R.drawable.star0, R.drawable.star10,
		R.drawable.star15, R.drawable.star20,
		R.drawable.star25, R.drawable.star30,
		R.drawable.star35, R.drawable.star40,
		R.drawable.star45, R.drawable.star50,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.shop_introduct_layout);
		
		Intent mIntent  = getIntent();
		mInfo = (ShopInfo) mIntent.getSerializableExtra("shopInfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		shopPic = (ImageView) findViewById(R.id.shop_detail_pic);
		
		flavorPoint = (TextView) findViewById(R.id.shop_detail_flavor_point);
		environmentPoint = (TextView) findViewById(R.id.shop_detail_environment_point);
		serverPoint = (TextView) findViewById(R.id.shop_detail_server_point);
		mStarView = (ImageView) findViewById(R.id.shop_detail_star);
		
		limitPrice = (TextView) findViewById(R.id.shop_detail_limitPrice);
		sendPrice = (TextView) findViewById(R.id.shop_detail_sendPrice);
		sendTime = (TextView) findViewById(R.id.shop_detail_send_time);
		payment = (TextView) findViewById(R.id.shop_detail_payment);
		
		openTime = (TextView) findViewById(R.id.shop_detail_open_time);
		scheduledTime = (TextView) findViewById(R.id.shop_detail_scheduled_time);
		
		phone = (TextView) findViewById(R.id.shop_detail_phone);
		addressView = (TextView) findViewById(R.id.shop_detail_address);
		
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		FileCache fileCache = new FileCache(this);
		File picFile = fileCache.getFile(mInfo.getShoppic());
		if(picFile.exists()) {
			String pathStr = picFile.getAbsolutePath();
			pathStr = pathStr.replace("/mnt", "");
			Uri uri = Uri.parse("file://" + pathStr);
			shopPic.setImageURI(uri);
		}
		
		flavorPoint.setText(mInfo.getSumtastepoint() + " ");
		environmentPoint.setText(mInfo.getSummilieupoint() + " ");
		serverPoint.setText(mInfo.getSumservicepoint() + " ");		
		int rate = (mInfo.getSumtastepoint() + mInfo.getSummilieupoint() + mInfo.getSumservicepoint())/3;
		if(rate > 9) rate = 9;
		mStarView.setImageDrawable(getResources().getDrawable(starId[rate]));
		
		limitPrice.setText(mInfo.getLimitprice() + "");
		sendPrice.setText(mInfo.getSendprice() + "");
		sendTime.setText(mInfo.getSendtime() + "");
		payment.setText(mInfo.getPayment());
		
		openTime.setText(mInfo.getOpentime());
		scheduledTime.setText(mInfo.getOrdertime());		
		
		phone.setText(mInfo.getPhone());
		addressView.setText(mInfo.getAddress());
	}
	
}
