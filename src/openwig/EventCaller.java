package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.LuaTable;

public class EventCaller extends Thread {
	
	private LuaTable object;
	private String event;
	private Object param;
	
	public EventCaller (LuaTable object, String event, Object param) {
		this.object = object;
		this.event = event;
		this.param = param;
	}

	public void run() {
		EventTable.callEvent(object, event, param);
		Midlet.refresh();
	}

}
