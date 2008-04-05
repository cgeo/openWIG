package openwig;

public class EventCaller extends Thread {
	
	private EventTable object;
	private String event;
	private Object param;
	
	public EventCaller (EventTable object, String event, Object param) {
		this.object = object;
		this.event = event;
		this.param = param;
	}

	public void run() {
		object.callEvent(event, param);
	}

}
