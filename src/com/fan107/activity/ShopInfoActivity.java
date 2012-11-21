package com.fan107.activity;

import com.fan107.R;
import com.fan107.data.ShopInfo;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShopInfoActivity extends Activity implements ActivityTemplete{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_commodity_introduct);
		
		Intent mIntent  = getIntent();
		ShopInfo mInfo = (ShopInfo) mIntent.getSerializableExtra("shopinfo");
		
		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		
	}

	public void setWidgetListenter() {
		
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		
	}
	
	
}
