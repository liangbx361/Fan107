package common.xmlParser.net;

import java.io.Serializable;

/**
 * µ¥´ÊÀý¾ä
 * @author Administrator
 *
 */
public class Example implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2625664635400366603L;
	
	public Example()
	{
		
	}
	
	public Example(String en, String chs)
	{
		this.englishSentence = en;
		this.translateSentence = chs;
	}
	
	public boolean isValidExample()
	{
		if (englishSentence == null)
		{
			return false;
		}
		
		if (translateSentence == null)
		{
			return false;
		}
			
		return true;
	}
	
	
	
	public String getEnglishSentence() {
		return englishSentence;
	}
	public void setEnglishSentence(String englishSentence) {
		this.englishSentence = englishSentence;
	}
	public String getTranslateSentence() {
		return translateSentence;
	}
	public void setTranslateSentence(String translateSentence) {
		this.translateSentence = translateSentence;
	}



	String englishSentence;
	String translateSentence;
}