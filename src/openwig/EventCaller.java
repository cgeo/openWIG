package openwig;

import gui.Midlet;

public class EventCaller extends Thread {
	
	private EventTable target = null;
	private String event;
	private Object param;
	
	public EventCaller (EventTable target, String event, Object param) {
		this.target = target;
		this.event = event;
		this.param = param;
	}

	public void run() {
		try {
			target.callEvent(event, param);
			Midlet.refresh();
		} catch (Throwable t) {
			Engine.stacktrace(t);
		}
	}
}
