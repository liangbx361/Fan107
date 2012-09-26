package common.xmlParser.net;

import java.io.Serializable;

public class Explanations implements Serializable {
	private static final long serialVersionUID = 3508869041676037556L;

	private String wordType; // ¥ –‘
	private String wordMeaning; // ∫¨“Â
	
	public Explanations() {
		
	}
	
	public Explanations(String wordType, String wordMeaning) {
		this.wordType = wordType;
		this.wordMeaning = wordMeaning;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
	}

	public String getWordMeaning() {
		return wordMeaning;
	}

	public void setWordMeaning(String wordMeaning) {
		this.wordMeaning = wordMeaning;
	}

}
