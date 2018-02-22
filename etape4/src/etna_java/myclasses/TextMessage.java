package etna_java.myclasses;

import java.io.Serializable;

public class TextMessage extends Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8553252553387624034L;
	private String  object;
	private String content;
	
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public TextMessage( String content) {
		this.content = content;
	}
	
	
	
	
}
