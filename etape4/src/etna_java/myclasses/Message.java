package etna_java.myclasses;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6177192546488945497L;
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss:SSS";
	private long id;
	private long time;
	private String from;
	private String to;
	
	public Message(String from, String to) {
		this.from = from;
		this.to = to;
		try {
			this.time=convertToTimeStamp(to)-convertToTimeStamp(from);
		} catch (ParseException e) {
			System.out.println("Probleme de conversion de cette date : "+to+" ou cette date : "+from);
		}
	}
	
	public Message() {
		//default constructor
	}

	public void readMessage() {
		
	}

	public String infoMessage() {
		return "id=" + id + "\ntime=" + time + "\nfrom=" + from + "\nto=" + to + "\n";
	}
	
	
	public long convertToTimeStamp(String date) throws ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	            DATE_FORMAT);
	    Date parsedTimeStamp = dateFormat.parse(date);
	    Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
	    return timestamp.getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	
}
