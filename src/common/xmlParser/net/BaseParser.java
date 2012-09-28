//package common.xmlParser.net;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import android.util.Log;
//
//import com.dict.WordStructure;
//
///**
// * 解析得到的结果
// * 
// * @author Administrator
// * 
// */
//public class BaseParser {
//
//	protected WordStructure wordStructure;
//	protected final String notFond = "Not Found";
//
//	protected final String keyNode = "key"; // 单词
//	protected final String psNode = "ps"; // 音标
//	protected final String pronNode = "pron"; // 语音
//	protected final String posNode = "pos"; // 词性
//	protected final String acceptationNode = "acceptation"; // 解释
//	protected final String englishSentence = "orig"; // 英文例句
//	protected final String chineseTranslation = "trans"; // 中文翻译
//
//	public BaseParser(Element root) {
//		wordStructure = new WordStructure();
//
//		NodeList wordList = root.getElementsByTagName(keyNode);
//		if (wordList.getLength() != 0) { // 如果没有keyNode则表示不存在此单词
//			Node key = wordList.item(0);
//			String word = key.getFirstChild().getNodeValue();
//			wordStructure.setWord(word);
//		}
//
//		NodeList proNounce = root.getElementsByTagName(psNode);
//		if (proNounce.getLength() != 0) {
//			Node proNode = proNounce.item(0);
//			NodeList mList = proNode.getChildNodes();
//			String pro = parserNodeList(mList);
//			wordStructure.setPronounce(pro);
//		}
//
//		NodeList audioList = root.getElementsByTagName(pronNode);
//		if (audioList.getLength() != 0) {
//			Node audio = audioList.item(0);
//			String voice = audio.getFirstChild().getNodeValue();
//			wordStructure.setVoice(voice);
//		}
//
//		NodeList posList = root.getElementsByTagName(posNode);
//		NodeList acceptationList = root.getElementsByTagName(acceptationNode);
//		if (posList.getLength() != 0 && acceptationList.getLength() != 0) {
//			String meanning = "";
//			for (int i = 0; i < posList.getLength(); i++) {
//				Node posNode = posList.item(i);
//				Node acceptationNode = acceptationList.item(i);
//				if (posNode.getFirstChild() != null) {
//					meanning += posNode.getFirstChild().getNodeValue() + " ";
//				}
//				if (acceptationNode.getFirstChild() != null) {
//					meanning += acceptationNode.getFirstChild().getNodeValue();
//				}
//				meanning += "\n";
//			}
//			wordStructure.setMeaning(meanning);
//		}
//
//		NodeList enList = root.getElementsByTagName(englishSentence);
//		NodeList chList = root.getElementsByTagName(chineseTranslation);
//		if (enList.getLength() != 0 && chList.getLength() != 0) {
//			processSentList(enList, chList);
//		}
//
//		// 过滤掉一些情况
//		if (wordStructure.getWord() == null) {
//			wordStructure = null;
//		} else if (wordStructure.getMeaning() == null
//				&& wordStructure.getListExample() == null
//				&& wordStructure.getVoice() == null
//				&& wordStructure.getPronounce() == null) {
//			wordStructure = null;
//		}
//	}
//
//	/**
//	 * 处理sent节点
//	 * 
//	 * @param nlist
//	 */
//	private void processSentList(NodeList enList, NodeList chList) {
//		if (enList == null || enList.getLength() == 0) {
//			return;
//		}
//
//		ArrayList<Example> exampleList = new ArrayList<Example>();
//		for (int i = 0; i < enList.getLength(); i++) {
//			Node enNode = enList.item(i);
//			Node chNode = chList.item(i);
//			String en = enNode.getFirstChild().getNodeValue();
//			String ch = chNode.getFirstChild().getNodeValue();
//			en = en.replace("\n", "");
//			ch = ch.replace("\n", "");
//			Example example = new Example(en, ch);
//
//			if (example.isValidExample()) {
//				exampleList.add(example);
//			}
//		}
//		wordStructure.setListExample(exampleList);
//	}
//
//	private Example processSigleSent(Node sentNode) {
//		Example example = new Example();
//
//		NodeList children = sentNode.getChildNodes();
//		for (int i = 0; i < children.getLength(); i++) {
//			Node child = children.item(i);
//			if (child instanceof Element) {
//				Element childElement = (Element) child;
//				String name = childElement.getNodeName();
//				NodeList mList = childElement.getChildNodes();
//				String value = parserNodeList(mList);
//
//				if (name.equalsIgnoreCase(englishSentence)) {
//					example.setEnglishSentence(value);
//				} else {
//					example.setTranslateSentence(value);
//				}
//			}
//		}
//		return example;
//	}
//
//	/**
//	 * 解析例句中的字符串, 删除不需要的部分
//	 * 
//	 * @param mList
//	 * @return
//	 */
//	private String parserNodeList(NodeList mList) {
//		String value = "";
//
//		for (int i = 0; i < mList.getLength(); i++) {
//			Node child = mList.item(i);
//			value += child.getNodeValue();
//		}
//
//		return value;
//	}
//
//	public WordStructure getWordStructure() {
//		return wordStructure;
//	}
//
//}
//	*/
