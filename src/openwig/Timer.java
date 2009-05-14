package openwig;

import se.krka.kahlua.vm.*;
import gui.Midlet;

public class Timer extends EventTable {
	
	private static java.util.Timer globalTimer;

	private static JavaFunction start = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Timer t = (Timer)callFrame.get(0);
			t.start();
			return 0;
		}
	};
	
	private static JavaFunction stop = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Timer t = (Timer)callFrame.get(0);
			t.stop();
			return 0;
		}
	};
	
	private static JavaFunction tick = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Timer t = (Timer)callFrame.get(0);
			//t.tick();
			t.callEvent("OnTick", null);
			return 0;
		}
	};
	
	private class TimerTask extends java.util.TimerTask {
		public void run() {
			tick();
			Midlet.refresh();
		}	
	}
	
	private TimerTask task = null;
	
	private static final int COUNTDOWN = 0;
	private static final int INTERVAL = 1;
	private int type = COUNTDOWN;
	
	private long duration = -1;
	private long lastTick = 0;
	
	public Timer () {
		if (globalTimer == null) globalTimer = new java.util.Timer();
		table.rawset("Start", start);
		table.rawset("Stop", stop);
		table.rawset("Tick", tick);
	}
	
	protected void setItem (String key, Object value) {
		if ("Type".equals(key) && value instanceof String) {
			String v = (String)value;
			if ("Countdown".equals(v)) {
				type = COUNTDOWN;
			} else if ("Interval".equals(v)) {
				type = INTERVAL;
			}
		} else if ("Duration".equals(key) && value instanceof Double) {
			long d = (long)LuaState.fromDouble(value);
			rawset("Remaining", value);
			duration = d * 1000;
		} else super.setItem(key, value);
	}
	
	public void start () {
		if (task != null) return;
		if (duration == 0) {
			callEvent("OnStart", null);
			callEvent("OnTick", null);
			return;
		}
		task = new TimerTask();
		lastTick = System.currentTimeMillis();
		updateRemaining();
		callEvent("OnStart", null);
		switch (type) {
			case COUNTDOWN:
				globalTimer.schedule(task, duration);
				break;
			case INTERVAL:
				globalTimer.scheduleAtFixedRate(task, duration, duration);
				break;
		}
	}
	
	public void stop () {
		if (task != null) {
			task.cancel();
			task = null;
			callEvent("OnStop", null);
		}
	}
	
	public void tick () {
		Engine.callEvent(this, "OnTick", null);
		lastTick = System.currentTimeMillis();
		updateRemaining();
		if (type == COUNTDOWN) task = null;
	}
	
	public void updateRemaining () {
		long stm = System.currentTimeMillis();
		long time = (duration/1000) - ((stm - lastTick)/1000);
		table.rawset("Remaining", LuaState.toDouble(time));
	}
	
	public static void kill() {
		if (globalTimer != null) globalTimer.cancel();
		globalTimer = null;
	}
}
