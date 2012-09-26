package common.xmlParser.net;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


import com.dict.net.DictRemoteConnection;
import com.ostrichmyself.api.util.URLProcesser;
import common.connection.net.IRemoteConnection;

/**
 * android解析xml； 从应用的角度而言，只需要传入网址URI即可搞定
 * 
 * @author Administrator
 * 
 */
public class AndroidXMLParser {

	public AndroidXMLParser(String uri) {
		DocumentBuilder docBuilder = null;
		try {
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
	 * 获得字符数据
	 * @param uri
	 * @param encoding
	 * @return
	 */
	private InputSource getConnectionStringData(String uri, String encoding) {
		InputSource inputSource = null;
		try {
			String sssString = URLProcesser.getStringFromURL(uri, "UTF-8");
			StringReader sReader = new StringReader(sssString);
			inputSource = new InputSource(sReader);
			inputSource.setEncoding(encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputSource;
	}
	
	
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
	
	private Element root;
	private Document document;
	
	public static void main(String[] args) {
		IRemoteConnection connection = new DictRemoteConnection();
		AndroidXMLParser parser = new AndroidXMLParser(connection
				.getConnectionURI("split"));
		Element rootElement = parser.getRoot();
		BaseParser contentParser = new BaseParser(rootElement);
		System.out.println(contentParser.getWordStructure().getMeaning());
	}
	
}
