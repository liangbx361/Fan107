/**
 * 使用WebService进行通信示例
 */
package common.connection.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
	private static final String NAME_SPACE = "http://COE.org/";
	private static final String URL = "http://192.168.18.44/serverInsurance/Services.asmx?wsdl";
	private static SoapSerializationEnvelope envelope;

	// 调用WebService提供的方法
	public static String transferService(String METHOD_NAME,
			Map<String, Object> map) {
		String state = null;
		SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
		Iterator it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			request.addProperty(key, value);
		}

		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(URL);
		try {
			ht.call(NAME_SPACE + METHOD_NAME, envelope);
			if (envelope.getResponse() != null) {

				// .net中web service返回的数据是object类型,不能直接转成soapObject
				Object obj = envelope.getResponse();
				if (obj != null) {
					state = obj.toString();
				} else {
					state = null;
				}
			} else {
				state = null;
			}
		} catch (Exception e) {
			state = null;
		}
		return state;
	}

	public static void main(String[] args) {
		String userName = "admin";
		String pwd = "admin";

		String METHOD_NAME = "LoginCheckUser";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("pwd", pwd);
		String json = WebService.transferService(METHOD_NAME, map);
	}
}
