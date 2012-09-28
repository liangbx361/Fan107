/**
 * 采用DOM解析XML文件
 * 使用方式
 * AndroidXMLParser parser = new AndroidXMLParser(String url);
   Element rootElement = parser.getRoot();
 */
package common.xmlParser.net;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import common.connection.net.HttpDownloader;


public class DomXMLParser {
	private Element root;
	private Document document;

	public DomXMLParser(String uri) {
		DocumentBuilder docBuilder = null;
		try {
			// 创建一个DOM工厂实例
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(getConnectionStringData(uri,
					"UTF-8"));
			root = document.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取网络xml资源，并转化成InputSource类型
	 * @param uri
	 * @param encoding
	 * @return InputSource
	 */
	private InputSource getConnectionStringData(String uri, String encoding) {
		InputSource inputSource = null;
		try {
			// 通过http获取网络的xml资源
			HttpDownloader mHttpDownloader = new HttpDownloader();
			String urlSource = mHttpDownloader.getHttpContent(uri, encoding);
			
			// 将String转化成InputSource
			StringReader sReader = new StringReader(urlSource);
			inputSource = new InputSource(sReader);
			inputSource.setEncoding(encoding);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputSource;
	}
	
	/**
	 * 得到解析好的节点元素
	 * @return
	 */
	public Element getRoot() {
		return root;
	}
		
	/**
	 * 保存xml文件
	 * @param name
	 */
	public void saveToFile(String name)
	{
		
	}
			
}
