package com.fan107.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.OrderCar;
import com.fan107.data.OrderDish;
import com.lbx.templete.ActivityTemplete;
import com.widget.helper.ToastHelper;

import common.connection.net.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderAddressDialog extends Dialog implements ActivityTemplete, android.view.View.OnClickListener{
	
	private final String[] hour = {
			"10��", "11��", "12��", "13��", "14��", "15��", "16��", "17��", 
			"18��", "19��", "20��", "21��", 
	};
	
	private final String[] minute = {
		"00��", "15��", "30��", "45��",  	
	};
	
	private TextView address1View;
	private EditText address2View;
	private TextView nameView;
	private TextView mobileView;
	private Spinner hourSpinner;
	private Spinner minuteSpinner;
	private Button confirmButton;
	private Button cancleButton;
	
	private OrderCar mCar;
	private Context context;
	private String address1;
	
	private boolean isConfirm = false;
	
	public OrderAddressDialog(Context context, OrderCar orderCar) {
		super(context);
		mCar = orderCar;
		this.context = context;
	}
	
	public OrderAddressDialog(Context context, int theme, OrderCar orderCar) {
		super(context, theme);
		mCar = orderCar;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_address_layout);
		
		findWidget();
		setWidgetListenter();
		setWidgetAttribute();
	}

	public void findWidget() {
		address1View = (TextView)findViewById(R.id.order_address_1);
		address2View = (EditText)findViewById(R.id.order_address_2);
		nameView = (TextView)findViewById(R.id.order_address_name);
		mobileView = (TextView)findViewById(R.id.order_address_mobile);
		hourSpinner = (Spinner)findViewById(R.id.order_address_time_hour);
		minuteSpinner = (Spinner)findViewById(R.id.order_address_time_minute);
		confirmButton = (Button)findViewById(R.id.order_address_confirm);
		cancleButton = (Button)findViewById(R.id.order_address_cancle);	
	}

	public void setWidgetListenter() {
		confirmButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);
	}

	public void setWidgetPosition() {
		
	}

	public void setWidgetAttribute() {
		address1 = mCar.userAddress.split("\\|")[1];
		address1View.setText(address1);
		nameView.setText(mCar.userName);
		mobileView.setText(mCar.userTel);
				
		List<String> hourList = new ArrayList<String>();
		for(int i=0; i<hour.length; i++) {
			hourList.add(hour[i]);
		}
		hourSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hourList));
		
		List<String> miuteList = new ArrayList<String>();
		for(int i=0; i<minute.length; i++) {
			miuteList.add(minute[i]);
		}
		minuteSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, miuteList));		
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.order_address_confirm:
			sendOrder();			
			break;
			
		case R.id.order_address_cancle:
			dismiss();
			break;
		}
	}
	
	private void sendOrder() {
		String delayAddr = address2View.getText().toString();
		if(delayAddr!= null && !delayAddr.equals("")) {
			mCar.remark = (String)hourSpinner.getSelectedItem() + (String)minuteSpinner.getSelectedItem();
			mCar.remark = mCar.remark.replace("��", ":");
			mCar.remark = mCar.remark.replace("��", "");
			mCar.userAddress = address1 + address2View.getText().toString();
		
			mThread.start();
		} else {
			ToastHelper.showToastInBottom(context, "����д��ϸ��ַ", 0);
		}
	}
	
	Thread mThread = new Thread() {

		@Override
		public void run() {
			//���ɶ���, ����������Ͷ���
			String carJson = mCar.getJsonString();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("orderMesage", carJson);
			String url = WebServiceConfig.url + WebServiceConfig.ORDER_CHECK_WEB_SERVICE;
			SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GENERATE_ORDER_METHOD, data);
			
			if(result.getPropertyAsString(0).equals("ok")) {
				isConfirm = true;
				dismiss();				
			} else {
				ToastHelper.showToastInBottom(context, "�����ύʧ��, ������", 0);
			}
		}
		
	};

	public boolean isConfirm() {
		return isConfirm;
	}
}
