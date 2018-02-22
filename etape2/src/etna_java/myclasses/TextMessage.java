package etna_java.myclasses;

public class TextMessage extends Message {

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
