package com.fan107.activity;

import com.fan107.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RecevoirAddressActivity extends Activity implements
		OnClickListener {
	private Button addAddressButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recevoir_address);

		findView();
		setListenter();
	}

	private void findView() {
		addAddressButton = (Button) findViewById(R.id.add_adress_btn);
	}

	private void setListenter() {
		addAddressButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_adress_btn:
			Intent mIntent = new Intent();
			mIntent.setClass(this, AddressAddActivity.class);
			startActivity(mIntent);
			break;
		}
	}
}
