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
 * android����xml�� ��Ӧ�õĽǶȶ��ԣ�ֻ��Ҫ������ַURI���ɸ㶨
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
	 * ����ַ�����
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
	 * ����xml�ļ�
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
