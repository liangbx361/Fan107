package com.fan107.activity;

import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity implements ActivityTemplete {

	private TextView versionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about_us);

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		versionView = (TextView) findViewById(R.id.version);
	}

	public void setWidgetListenter() {

	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		versionView.setText("����汾��:" + getVersionName());
	}

	private String getVersionName(){
		String version = "";
		
		try {
			// ��ȡpackage manager��ʵ��
			PackageManager packageManager = getPackageManager();
			// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return version;
	}
}
