package common.connection.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	
	// ����HttpClient����
	public static HttpClient httpClient;

	/**
	 * 
	 * @param url 
	 * 			���������URL     
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static String getRequest(String url) throws Exception {
		httpClient = new DefaultHttpClient();

		try {
			// ����HttpGet����
			HttpGet get = new HttpGet(url);
			// ����GET����
			HttpResponse httpResponse = httpClient.execute(get);
			// ����������ɹ��ط�����Ӧ
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ��ȡ��������Ӧ�ַ���
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return "��ȡ����ʧ�ܣ�";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	/**
	 * ʹ��POST��������
	 * @param url
	 *            ���������URL
	 * @param params
	 *            �������
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static String postRequest(String url, Map<String, String> rawParams) {
		String result = null;
		httpClient = new DefaultHttpClient();
		
		try {
			// ����HttpPost����
			HttpPost post = new HttpPost(url);
			// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				// ��װ�������
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
			}
			// �����������
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// ����POST����
			HttpResponse httpResponse = httpClient.execute(post);
			// ����������ɹ��ط�����Ӧ
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ��ȡ��������Ӧ�ַ���
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
}
