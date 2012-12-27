package com.fan107.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fan107.R;

public class LogoActivity extends Activity {
	
	private boolean flag = true;
	private int state;
	private int alpha = 0;
	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		setContentView(R.layout.logo_layout);
		imageView = (ImageView)findViewById(R.id.fan107_log);
		state = 0x123;
		alpha = 0;
		imageView.setAlpha(alpha);
		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(state);
				
			}
			
		}, 5);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == android.view.KeyEvent.KEYCODE_BACK)
    	{
    		System.exit(0);
    	}
		return super.onKeyDown(keyCode, event);
	}
	final Handler handler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		if(flag){
	    		if(msg.what == 0x124){
					flag = false;
					StartIntent();
				}else if(msg.what == 0x123){
					
					alpha += 5;
					if(alpha >= 255){
						alpha = 255;
						state = 0x124;
						new Timer().schedule(new TimerTask()
						{

							@Override
							public void run() {
								// TODO Auto-generated method stub
								handler.sendEmptyMessage(state);
								
							}
							
						}, 1000);
					}else{
						new Timer().schedule(new TimerTask()
						{

							@Override
							public void run() {
								// TODO Auto-generated method stub
								handler.sendEmptyMessage(state);
								
							}
							
						}, 2);
					}
					imageView.setAlpha(alpha);
				}
    		}
    		
    	}
    };
	public void StartIntent(){		
		finish();
		Intent intent = new Intent();
		intent.setClass(LogoActivity.this,SearchActivity.class);
		startActivity(intent);
		this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
}
