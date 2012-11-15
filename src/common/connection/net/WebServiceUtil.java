package common.connection.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.fan107.config.WebServiceConfig;

public class WebServiceUtil {
	private static final String TAG = "WebServiceUtil";

	public static SoapObject getWebServiceResult(String url, String methodName,
		Map<String, Object> params) {
		SoapObject result = null;
		
		try {
			// step1 ָ��WebService�������ռ�͵��õķ�����
			SoapObject request = new SoapObject(WebServiceConfig.NAME_SPACE,
					methodName);

			// step2 ���õ��÷����Ĳ���ֵ,����Ĳ���������ú�WebServiceһ��
			for (String key : params.keySet()) {
				request.addProperty(key, params.get(key));
			}

			// step3 ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER10);

			// �����Ƿ���õ���dotNet�µ�WebService
			envelope.dotNet = true;
			// ���룬�ȼ���envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);

			// step4 ����HttpTransportSE����
			HttpTransportSE ht = new HttpTransportSE(url);

			// step5 ����WebService
			ht.call(WebServiceConfig.NAME_SPACE + methodName, envelope);

			// step6 ʹ��getResponse�������WebService�����ķ��ؽ��
			if (envelope.getResponse() != null) {
				 result = (SoapObject) envelope.bodyIn;				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<String> parseTransMessage(SoapObject detail) {
		List<String> messageList = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++) {
//			messageList.add(detail.getProperty(i).toString());
			SoapObject soapChilds =(SoapObject)detail.getProperty(i);
			Log.d(TAG, soapChilds.getProperty("Id").toString());
			Log.d(TAG, soapChilds.getProperty("UserGroup").toString());
			Log.d(TAG, soapChilds.getProperty("UserName").toString());
			
		}
		return messageList;
	}
	
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
}
