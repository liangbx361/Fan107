package common.xmlParser.net;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomXmlParserDemo {
	
	private final String keyNode = "key"; // ����
	
	public DomXmlParserDemo(Element element) {
		
		// ���ݽڵ����Ƶõ��ڵ�����
		NodeList nodeList = element.getElementsByTagName(keyNode);
		for(int i=0; i<nodeList.getLength(); i++) {
			// ���ÿ���ڵ�
			Node node = nodeList.item(i);
		}
		
		// 
		
		// 
		
	}

}
