package openwig;

import se.krka.kahlua.vm.*;
import gui.Midlet;

public class CallbackCaller extends Thread {
	
	private LuaClosure callback;
	private Object value;
	
	public CallbackCaller (LuaClosure callback, Object value) {
		this.callback = callback;
		this.value = value;
	}

	public void run() {
		try {
		synchronized (Engine.state) {
			try {
				Engine.state.call(callback, value, null, null);
			} catch (Exception e) {
				Engine.stacktrace(e);
			}
		}
		Midlet.refresh();
		} catch (Throwable t) {
			Engine.stacktrace(t);
		}
	}

}
