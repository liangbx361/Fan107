/**
 * ����DOM����XML�ļ�
 * ʹ�÷�ʽ
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
			// ����һ��DOM����ʵ��
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
	 * ��ȡ����xml��Դ����ת����InputSource����
	 * @param uri
	 * @param encoding
	 * @return InputSource
	 */
	private InputSource getConnectionStringData(String uri, String encoding) {
		InputSource inputSource = null;
		try {
			// ͨ��http��ȡ�����xml��Դ
			HttpDownloader mHttpDownloader = new HttpDownloader();
			String urlSource = mHttpDownloader.getHttpContent(uri, encoding);
			
			// ��Stringת����InputSource
			StringReader sReader = new StringReader(urlSource);
			inputSource = new InputSource(sReader);
			inputSource.setEncoding(encoding);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputSource;
	}
	
	/**
	 * �õ������õĽڵ�Ԫ��
	 * @return
	 */
	public Element getRoot() {
		return root;
	}
		
	/**
	 * ����xml�ļ�
	 * @param name
	 */
	public void saveToFile(String name)
	{
		
	}
			
}
