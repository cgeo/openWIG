package openwig;

import se.krka.kahlua.vm.*;
import gui.Midlet;

public class Timer extends EventTable {
	
	private static java.util.Timer globalTimer = new java.util.Timer();
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Timer.class, metatable);
	}
	
	private static class Method implements JavaFunction {
		private int index;
		
		public static final int START = 0;
		public static final int STOP = 1;
		
		public Method(int index) {
			this.index = index;
		}

		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case START: return start(callFrame, nArguments);
				case STOP: return stop(callFrame, nArguments);
				default: return 0;
			}
		}
		
		private int start (LuaCallFrame frame, int n) {
			Timer t = (Timer)frame.get(0);
			t.start();
			return 0;
		}
		
		private int stop (LuaCallFrame frame, int n) {
			Timer t = (Timer)frame.get(0);
			t.stop();
			return 0;
		}
	}
	
	private static Method startMethod = new Method(Method.START);
	private static Method stopMethod = new Method(Method.STOP);
	
	private static class TimerTask extends java.util.TimerTask {
		
		private Timer parent;
		public TimerTask (Timer parent) {
			this.parent = parent;
		}

		public void run() {
			parent.callEvent("OnTick", null);
			Midlet.refresh();
		}	
	}
	
	private TimerTask task = null;
	
	private static final int COUNTDOWN = 0;
	private static final int INTERVAL = 1;
	private int type = COUNTDOWN;
	
	private long duration = -1;
	
	private boolean running = false;
	
	public Timer () {
		this.table.rawset("Start", startMethod);
		this.table.rawset("Stop", stopMethod);
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Type" && value instanceof String) {
			String v = (String)value;
			if (v == "Countdown") {
				type = COUNTDOWN;
			} else if (v == "Interval") {
				type = INTERVAL;
			}
		} else if (key == "Duration" && value instanceof Double) {
			long d = (long)LuaState.fromDouble(value);
			duration = d * 1000;
		} else super.setItem(key, value);
	}
	
	synchronized public void start () {
		stop();
		running = true;
		callEvent("OnStart", null);
		task = new TimerTask(this);
		switch (type) {
			case COUNTDOWN:
				globalTimer.schedule(task, duration);
				break;
			case INTERVAL:
				globalTimer.scheduleAtFixedRate(task, duration, duration);
				break;
		}
	}
	
	synchronized public void stop () {
		if (running) {
			callEvent("OnStop", null);
			task.cancel();
			task = null;
		}
		running = false;
	}
}
