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
	 * 通过URL连接服务器, 得到的数据以String类型返回
	 * 适用于文本, XML, JOSN数据格式
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getHttpContent(String urlStr, String encoding) {
		String contentStr = null;

		try {
			// 转换成URL
			URL url = new URL(urlStr);
			// 建立连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			
			// 设置消息报头
//			urlConn.setRequestProperty("Authorization", "Basic " + encoding);
//			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			// 获取一个远程的输入流
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
			
			// 关闭输入流
			in.close();
			// 取消连接
			urlConn.disconnect();
			contentStr = buffer.toString();		
			
		} catch (Exception e) {
			e.printStackTrace();
			contentStr = null;
		}
		return contentStr;
	}

	/**
	 * 通过Http下载文件, 并保存在SD卡上
	 * 适用于下载图片, MP3等需要保存的数据
	 * @param urlStr: 下载资源地址
	 * @param path: 存储的路径
	 * @param fileName: 文件名
	 * @return:  -1 代表下载文件出错  0：代表下载文件成功 1：代表文件已经存在
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
	 * 根据URL得到输入流
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
