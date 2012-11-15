package common.connection.net;

import org.ksoap2.serialization.SoapObject;

import com.fan107.config.WebServiceConfig;

public class SoapObjectHelper {
	
	public static int getSoapObjectInt(SoapObject soapObject, String name) {
		return Integer.valueOf(soapObject.getProperty(name).toString()).intValue();
	}
	
	public static String getSoapObjectString(SoapObject soapObject, String name) {
		String result =  soapObject.getProperty(name).toString();
		
		if(result.equals("anyType{}")) {
			result = "";
		}
		
		return result;
	}
	
	public static float getSoapObjectFloat(SoapObject soapObject, String name) {
		return Float.valueOf(soapObject.getProperty(name).toString()).floatValue();
	}
	
	public static String getSoapObjectResultString(SoapObject soapObject, String method) {	
		return soapObject.getProperty(method + "Result").toString();
	}

}
