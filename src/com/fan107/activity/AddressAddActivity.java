package com.fan107.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.common.helper.FormatCheck;
import com.fan107.R;
import com.fan107.config.WebServiceConfig;
import com.fan107.data.Area;
import com.fan107.data.UserAddress;
import com.fan107.db.DBHelper;
import com.lbx.templete.ActivityTemplete;
import common.connection.net.WebServiceUtil;

public class AddressAddActivity extends Activity implements ActivityTemplete, OnClickListener {
	private static final String TAG = "AddressAddActivity";
	
	private static final int UPDATE_AREA = 0;
	private static final int UPDATE_STREET = 1;
	private static final int UPDATE_DISTRICT = 2;
	
	private static final int DIALOG_SHOW = 100;
	
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
	
	private UserAddress mAddress;
	private boolean isInitSate;
	private int aid;
	private int sid;
	private int did;
	private int userid;
	
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.address_add_activity);
		
		mAddress = (UserAddress) getIntent().getSerializableExtra("useraddress");
		userid = getIntent().getIntExtra("userid", -1);
		if(mAddress != null) isInitSate = true;

		findWidget();
		setWidgetListenter();
		setWidgetPosition();
		setWidgetAttribute();
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
                    		if(!isInitSate)
                    			new StreetThread(areaList.get(position).id).start();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
		
		streetSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    	if(!isInitSate)
                    		new DistrictThread(streetList.get(position).id).start();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
		
		districtSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {               	
                    	if(isInitSate)
                    		isInitSate = false;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
		
		checkButton.setOnClickListener(this);		
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		
		if(mAddress != null) {
			try {
				String[] id = mAddress.getAddress().split("\\|")[0].split(",");
				aid = Integer.valueOf(id[0]);
				sid = Integer.valueOf(id[1]);
				did = Integer.valueOf(id[2]);			
			} catch (Exception e) {
				aid = 0;
				sid = 0;
				did = 0;
				isInitSate = false;
			}
			nameText.setText(mAddress.getUserName());
			telephoneText.setText(mAddress.getMobile());
		}
		
		new AreaThread().start();
		
		initProgressDialog(this);
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.address_add_confirm:
			if(checkContent()) {
				// 添加送货地址
				if(mAddress == null) {
					new Thread() {
						@Override
						public void run() {
							mHandler.sendEmptyMessage(DIALOG_SHOW);
							saveNewAddress();
							onBackPressed();
							mProgressDialog.dismiss();
						}
					}.start();
					
				} else {
					new Thread() {
						@Override
						public void run() {
							mHandler.sendEmptyMessage(DIALOG_SHOW);
							updateAddress();
							onBackPressed();
							mProgressDialog.dismiss();
						}
					}.start();					
				}				
//				Log.d(TAG, nameStr + "--" + telephoneStr + "--" + areaStr + "--" + streetStr + "--" + districtStr);
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
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddressAddActivity.this, R.layout.fan107_spinner_item, areaStr);
				areaSpinner.setAdapter(adapter1);
				if(isInitSate) {
					areaSpinner.setSelection(getSelectionIndex(areaList, aid));
				}
				break;
				
			case UPDATE_STREET:
				String[] streetStr = new String[streetList.size()];
				for(int i=0; i<streetList.size(); i++) {
					streetStr[i] = streetList.get(i).areaName;
				}
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddressAddActivity.this, R.layout.fan107_spinner_item, streetStr);
				streetSpinner.setAdapter(adapter2);
				if(isInitSate) {
					streetSpinner.setSelection(getSelectionIndex(streetList, sid));
				}
				break;
				
			case UPDATE_DISTRICT:
				String[] districtStr = new String[districtList.size()];
				for(int i=0; i<districtList.size(); i++) {
					districtStr[i] = districtList.get(i).areaName;
				}
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(AddressAddActivity.this, R.layout.fan107_spinner_item, districtStr);
				districtSpinner.setAdapter(adapter3);
				if(isInitSate) {
					districtSpinner.setSelection(getSelectionIndex(districtList, did));
				}
				break;
				
			case DIALOG_SHOW:
				mProgressDialog.show();
				break;
				
			}
		}
		
	};
	
	/**
	 * 检测内容填写的完整性
	 * @return
	 */
	public boolean checkContent() {
		FormatCheck formatCheck = new FormatCheck();
		
		nameStr = nameText.getText().toString();
		telephoneStr = telephoneText.getText().toString();
		areaStr = (String)areaSpinner.getSelectedItem();
		streetStr = (String)streetSpinner.getSelectedItem();
		districtStr = (String)districtSpinner.getSelectedItem();		
		
		if(nameStr == null || nameStr.equals("")) {
			return false;
		}
		
		if(formatCheck.checkPhoto(telephoneStr) == null){
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
//			areaStr = areaList.get(0).areaName;
			if(isInitSate) new StreetThread(aid).start();
			else new StreetThread(areaList.get(0).id).start();
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
//			streetStr = streetList.get(0).areaName;
			if(isInitSate) new DistrictThread(sid).start();
			else new DistrictThread(streetList.get(0).id).start();
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
//			districtStr = districtList.get(0).areaName;
			mHandler.sendEmptyMessage(UPDATE_DISTRICT);
		}
	}
	
	/**
	 * 保存新地址 
	 */
	private void saveNewAddress() {		
		DBHelper dbHelper = new DBHelper(this);
		StringBuilder addressBuilder = new StringBuilder();
		addressBuilder.append(getAreaId(areaList, areaStr)).append(",");
		addressBuilder.append(getAreaId(streetList, streetStr)).append(",");
		addressBuilder.append(getAreaId(districtList, districtStr)).append("|");
		addressBuilder.append(areaStr+streetStr+districtStr);
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userid", userid);
			jObject.put("username", nameStr);
			jObject.put("mobile", telephoneStr);
			jObject.put("address", addressBuilder.toString());
			jObject.put("isdefault", 0);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("addressMessage", jObject.toString());
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.ADD_NEW_ADDRESS_METHOD, params);
		
		if(result.getPropertyAsString(0).equals("ok")) {
			//存储到本地数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("userid", userid);
			contentValues.put("username", nameStr);
			contentValues.put("mobile", telephoneStr);
			contentValues.put("address", addressBuilder.toString());
			contentValues.put("isdefault", 0);
			dbHelper.insert(DBHelper.USER_ADDRESS_TABLE_NAME, contentValues);
		}
		
		dbHelper.close();
	}
	
	/**
	 * 更新地址 
	 */
	private void updateAddress() {		
		DBHelper dbHelper = new DBHelper(this);
		StringBuilder addressBuilder = new StringBuilder();
		addressBuilder.append(getAreaId(areaList, areaStr)).append(",");
		addressBuilder.append(getAreaId(streetList, streetStr)).append(",");
		addressBuilder.append(getAreaId(districtList, districtStr)).append("|");
		addressBuilder.append(areaStr+streetStr+districtStr);
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", mAddress.getId());
			jObject.put("userid", userid);
			jObject.put("username", nameStr);
			jObject.put("mobile", telephoneStr);
			jObject.put("address", addressBuilder.toString());
			jObject.put("isdefault", mAddress.getIsDefault());
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("addressMessage", jObject.toString());
		String url = WebServiceConfig.url + WebServiceConfig.USER_ADDRESS_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.UPDATE_ADDRESS_METHOD, params);
		
		if(result.getPropertyAsString(0).equals("ok")) {
			//存储到本地数据库中
			ContentValues contentValues = new ContentValues();
			contentValues.put("username", nameStr);
			contentValues.put("mobile", telephoneStr);
			contentValues.put("address", addressBuilder.toString());
			dbHelper.updateTable(DBHelper.USER_ADDRESS_TABLE_NAME, contentValues, "id="+mAddress.getId(), null);
		}
		
		dbHelper.close();
	}
	
	/**
	 * 获取区域的id号
	 * @param areaStr
	 * @return
	 */
	private int getAreaId(List<Area> list, String areaStr) {
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).areaName.equals(areaStr)) {
				return list.get(i).id;
			}
		}
		
		return -1;
	}
	
	/**
	 * 获取区域在控件的索引
	 * @param list
	 * @param id
	 * @return
	 */
	private int getSelectionIndex(List<Area> list, int id) {
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).id == id) {
				return i;
			}
		}
		
		return -1;
	}
	
	private void initProgressDialog(Context mContext) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("数据保存中");
		mProgressDialog.setMessage("请稍等...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
	}
}
