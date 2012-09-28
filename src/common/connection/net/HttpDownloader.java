package common.connection.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import common.file.util.FileUtils;

public class HttpDownloader {
	
	/**
	 * ͨ��URL���ӷ�����, �õ���������String���ͷ���
	 * �������ı�, XML, JOSN���ݸ�ʽ
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getHttpContent(String urlStr, String encoding) {
		String contentStr = null;

		try {
			// ת����URL
			URL url = new URL(urlStr);
			// ��������
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			
			// ������Ϣ��ͷ
//			urlConn.setRequestProperty("Authorization", "Basic " + encoding);
//			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			// ��ȡһ��Զ�̵�������
			InputStream content = (InputStream) urlConn.getInputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					content, encoding));
			StringBuffer buffer = new StringBuffer();
			while (in.ready()) {
				String inString = in.readLine().trim();
				if (inString.length() != 0) {
					buffer.append(inString);
				}
			}
			
			// �ر�������
			in.close();
			// ȡ������
			urlConn.disconnect();
			contentStr = buffer.toString();		
			
		} catch (Exception e) {
			e.printStackTrace();
			contentStr = null;
		}
		return contentStr;
	}

	/**
	 * ͨ��Http�����ļ�, ��������SD����
	 * ����������ͼƬ, MP3����Ҫ���������
	 * @param urlStr: ������Դ��ַ
	 * @param path: �洢��·��
	 * @param fileName: �ļ���
	 * @return:  -1 ���������ļ�����  0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 */
	public int downHttpFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();

			if (fileUtils.isFileExist(fileName, path)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,
						inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		URL url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
}
