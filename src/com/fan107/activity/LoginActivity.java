package com.fan107.activity;

import com.fan107.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener{
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_activity);
		
		findView();
		setListenter();
	}
	
	private void findView() {
		registerButton = (Button) findViewById(R.id.regbtn);
	}
	
	private void setListenter() {
		registerButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.regbtn:
			Intent mIntent = new Intent();
			mIntent.setClass(this, RegisterActivity.class);
			startActivity(mIntent);
			break;
		}
	}
}
