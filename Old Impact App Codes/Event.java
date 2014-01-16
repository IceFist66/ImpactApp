package ewbcalpoly.impact.ewbimpact;


public class Event implements Comparable<Event>{
	private String eventName;
	private int eventId;
	private String start;
	private String stop;
	private String eunix;
	
	public Event(String nName, int nId, String nStart, String nStop, String nunix){
		eventName = nName;
		eventId = nId;
		start = nStart;
		stop = nStop;
		eunix = nunix;
	}
	public int compareTo(Event ename){
		return eunix.compareToIgnoreCase(ename.eunix);
	}
	public String getName(){
		return eventName;
	}
	public int getId(){
		return eventId;
	}
	public String getStart(){
		return start;
	}
	public String getStop(){
		return stop;
	}
}
