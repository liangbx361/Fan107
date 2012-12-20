package com.fan107.common;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.fan107.config.WebServiceConfig;
import com.fan107.data.UpdateInfo;
import common.connection.net.WebServiceUtil;

public class UpdateState {

	public static UpdateInfo getUpdateSate() {
		UpdateInfo updateInfo = null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		String url = WebServiceConfig.url + WebServiceConfig.UPDATE_WEB_SERVICE;
		SoapObject result = WebServiceUtil.getWebServiceResult(url, WebServiceConfig.GET_VERSION_METHOD, params);
		
		if(result != null) {
			SoapObject soapChilds = (SoapObject) result.getProperty(0);
			updateInfo = new UpdateInfo();
			updateInfo.versionCode = WebServiceUtil.getSoapObjectInt(soapChilds, "VersionCode");
			updateInfo.versionName = WebServiceUtil.getSoapObjectString(soapChilds, "VersionName");
			updateInfo.src = WebServiceUtil.getSoapObjectString(soapChilds, "Src");
			updateInfo.introduction = WebServiceUtil.getSoapObjectString(soapChilds, "Introduction");
		}
		
		return updateInfo;
	}
	
	
	public static int getVersionCode(Context context) {
		
		int verCode = -1;
	    try
	    {
	        verCode = context.getPackageManager().getPackageInfo(
	            "com.fan107", 0).versionCode;
	    } catch (NameNotFoundException e) {
	        e.printStackTrace();
	    }
	    return verCode;
	}
}
