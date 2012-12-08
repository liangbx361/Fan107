package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.Area;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddressAddActivity extends Activity implements ActivityTemplete, OnClickListener {
	private static final int UPDATE_AREA = 0;
	private static final int UPDATE_STREET = 1;
	private static final int UPDATE_DISTRICT = 2;
	
	private String nameStr;
	private String areaStr;
	private String streetStr;
	private String districtStr;
	private String telephoneStr;
	
	private EditText nameText;
	private EditText telephoneText;
	private Spinner areaSpinner;
	private Spinner streetSpinner;
	private Spinner districtSpinner;
	private Button checkButton;
	
	private List<Area> areaList;
	private List<Area> streetList;
	private List<Area> districtList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.address_add_activity);

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
		
		new AreaThread().start();
	}

	public void findWidget() {
		areaSpinner = (Spinner) findViewById(R.id.address_add_area_spinner);
		streetSpinner = (Spinner) findViewById(R.id.address_add_street_spinner);
		districtSpinner = (Spinner) findViewById(R.id.address_add_district_spinner);
		nameText = (EditText) findViewById(R.id.address_add_name);
		telephoneText = (EditText) findViewById(R.id.address_add_phone);
		checkButton = (Button) findViewById(R.id.address_add_confirm);
	}

	public void setWidgetListenter() {
		areaSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
		
		streetSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
		
		districtSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
		
		checkButton.setOnClickListener(this);		
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.address_add_confirm:
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
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what) {
			case UPDATE_AREA:
				String[] areaStr = new String[areaList.size()];
				for(int i=0; i<areaList.size(); i++) {
					areaStr[i] = areaList.get(i).areaName;
				}
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddressAddActivity.this, android.R.layout.simple_spinner_item, areaStr);
				areaSpinner.setAdapter(adapter1);
				break;
				
			case UPDATE_STREET:
				String[] streetStr = new String[areaList.size()];
				for(int i=0; i<areaList.size(); i++) {
					streetStr[i] = streetList.get(i).areaName;
				}
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddressAddActivity.this, android.R.layout.simple_spinner_item, streetStr);
				streetSpinner.setAdapter(adapter2);
				break;
				
			case UPDATE_DISTRICT:
				String[] districtStr = new String[areaList.size()];
				for(int i=0; i<areaList.size(); i++) {
					districtStr[i] = districtList.get(i).areaName;
				}
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(AddressAddActivity.this, android.R.layout.simple_spinner_item, districtStr);
				districtSpinner.setAdapter(adapter3);
				break;
			}
		}
		
	};
	
	/**
	 * 检测内容填写的完整性
	 * @return
	 */
	public boolean checkContent() {
		
		nameStr = nameText.getText().toString();
		areaStr = (String)areaSpinner.getSelectedItem();
		streetStr = (String)streetSpinner.getSelectedItem();
		districtStr = (String)districtSpinner.getSelectedItem();
		telephoneStr = telephoneText.getText().toString();
		
		if(nameStr == null || nameStr.equals("")) {
			return false;
		}
		
		if(telephoneStr == null || telephoneStr.equals("")) {
			return false;
		}
		
		if(areaStr == null || areaStr.equals("")) {
			return false;
		}
		
		if(streetStr == null || streetStr.equals("")) {
			return false;
		}
		
		if(districtStr == null || districtStr.equals("")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取区域
	 * @return
	 */
	private List<Area> getArea() {
		List<Area> list = new ArrayList<Area>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_AREA_METHOD, params);
		result = WebServiceUtil.getChildSoapObject(result, new int[]{0});
		
		for(int i=0; i<result.getPropertyCount(); i++) {
			SoapObject child = (SoapObject) result.getProperty(i);
			Area area = new Area();
			area.id = WebServiceUtil.getSoapObjectInt(child, "Id");
			area.areaName = WebServiceUtil.getSoapObjectString(child, "AreaName");
			area.parentId = WebServiceUtil.getSoapObjectInt(child, "Pid");
			list.add(area);
		}
		
		return list;
	}
	
	private class AreaThread extends Thread {
		
		@Override
		public void run() {
			areaList = getArea();
			areaStr = areaList.get(0).areaName;
			new StreetThread(areaList.get(0).id).start();
			mHandler.sendEmptyMessage(UPDATE_AREA);
		}
	}

	/**
	 * 获取街道
	 * @return
	 */
	private List<Area> getStreet(int aid) {
		List<Area> list = new ArrayList<Area>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("aid", aid);
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_STREET_METHOD, params);
		result = WebServiceUtil.getChildSoapObject(result, new int[]{0});
		
		for(int i=0; i<result.getPropertyCount(); i++) {
			SoapObject child = (SoapObject) result.getProperty(i);
			Area area = new Area();
			area.id = WebServiceUtil.getSoapObjectInt(child, "Id");
			area.areaName = WebServiceUtil.getSoapObjectString(child, "AreaName");
			area.parentId = WebServiceUtil.getSoapObjectInt(child, "Pid");
			list.add(area);
		}
		
		return list;
	}
	
	private class StreetThread extends Thread {
		private int aid;
		
		public StreetThread(int aid) {
			this.aid = aid;
		}
		
		@Override
		public void run() {
			streetList = getStreet(aid);		
			streetStr = streetList.get(0).areaName;
			new DistrictThread(streetList.get(0).id).start();
			mHandler.sendEmptyMessage(UPDATE_STREET);
		}
	}
	
	/**
	 * 获取楼宇
	 * @return
	 */
	private List<Area> getDistrict(int sid) {
		List<Area> list = new ArrayList<Area>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_DISTRICT_METHOD, params);
		result = WebServiceUtil.getChildSoapObject(result, new int[]{0});
		
		for(int i=0; i<result.getPropertyCount(); i++) {
			SoapObject child = (SoapObject) result.getProperty(i);
			Area area = new Area();
			area.id = WebServiceUtil.getSoapObjectInt(child, "Id");
			area.areaName = WebServiceUtil.getSoapObjectString(child, "AreaName");
			area.parentId = WebServiceUtil.getSoapObjectInt(child, "Pid");
			list.add(area);
		}
		
		return list;
	}
	
	private class DistrictThread extends Thread {
		int sid;
		
		public DistrictThread(int sid) {
			this.sid = sid;
		}
		
		@Override
		public void run() {
			districtList = getDistrict(sid);
			districtStr = districtList.get(0).areaName;
			mHandler.sendEmptyMessage(UPDATE_DISTRICT);
		}
	}
}
