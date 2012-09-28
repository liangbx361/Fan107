package common.xmlParser.net;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomXmlParserDemo {
	
	private final String keyNode = "key"; // 单词
	
	public DomXmlParserDemo(Element element) {
		
		// 根据节点名称得到节点数据
		NodeList nodeList = element.getElementsByTagName(keyNode);
		for(int i=0; i<nodeList.getLength(); i++) {
			// 获得每个节点
			Node node = nodeList.item(i);
		}
		
		// 
		
		// 
		
	}

}
