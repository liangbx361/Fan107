package com.fan107.activity;

import java.util.ArrayList;
import java.util.List;

import com.fan107.R;
import com.lbx.templete.ActivityTemplete;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddressAddActivity extends Activity implements ActivityTemplete, OnClickListener {
	private String nameStr;
	private String areaStr;
	private String addressStr;
	private String telephoneStr;
	
	private EditText nameText;
	private EditText addressText;
	private EditText telephoneText;
	private Spinner areaSpinner;
	private Button checkButton;

	private final String area[] = { "鼓楼区", "台江区", "仓山区", "马尾区", "晋安区" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.address_add_activity);

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
	}

	public void findWidget() {
		areaSpinner = (Spinner) findViewById(R.id.area_spinner);
		nameText = (EditText) findViewById(R.id.name);
		addressText = (EditText) findViewById(R.id.address);
		telephoneText = (EditText) findViewById(R.id.phone);
		checkButton = (Button) findViewById(R.id.add);
	}

	public void setWidgetListenter() {
		checkButton.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		List<String> mList = new ArrayList<String>();
		
		for(int i=0; i<area.length; i++) {
			mList.add(area[i]);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mList);
		areaSpinner.setAdapter(adapter);
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.add:
			if(checkContent()) {
				// 添加送货地址
			} else {
				// 信息不完整,无法添加
				Toast mToast = Toast.makeText(this, "信息填写不完整!", 500);
				mToast.setGravity(Gravity.BOTTOM, 0, 20);
				mToast.show();
			}
			break;
		}
	}
	
	/**
	 * 检测内容填写的完整性
	 * @return
	 */
	public boolean checkContent() {
		
		nameStr = nameText.getText().toString();
		areaStr = (String)areaSpinner.getSelectedItem();
		addressStr = addressText.getText().toString();
		telephoneStr = telephoneText.getText().toString();
		
		if(nameStr == null || nameStr.equals("")) {
			return false;
		}
		
		if(areaStr == null || areaStr.equals("")) {
			return false;
		}
		
		if(addressStr == null || addressStr.equals("")) {
			return false;
		}
		
		if(telephoneStr == null || telephoneStr.equals("")) {
			return false;
		}
		
		
		return true;
	}
	

}
