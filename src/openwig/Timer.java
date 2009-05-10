package openwig;

import se.krka.kahlua.vm.*;

public class Timer extends EventTable {

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
			t.start();
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
	
	private static final int COUNTDOWN = 0;
	private static final int INTERVAL = 1;
	private int type = COUNTDOWN;
	
	private long duration = -1;
	
	private long lastTick = 0;
	
	private boolean running = false;
	
	public Timer () {
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
			duration = d * 1000;
		} else super.setItem(key, value);
	}
	
	public void start () {
		if (running) return;
		table.rawset("Remaining", LuaState.toDouble(duration / 1000));
		callEvent("OnStart", null);
		running = true;
		lastTick = System.currentTimeMillis();
	}
	
	public void stop () {
		if (running) {
			callEvent("OnStop", null);
		}
		running = false;
	}
	
	public void tick () {
		if (!running) return;
		long ctm = System.currentTimeMillis();
		long time = ctm - lastTick;
		long remaining = duration - time;
		while (remaining < 0) remaining += duration;
		table.rawset("Remaining", LuaState.toDouble(remaining / 1000));
		while (time >= duration) {
			callEvent("OnTick", null);
			time -= duration;
			lastTick = ctm;
			if (type == COUNTDOWN) {
				running = false;
				break;
			}
		}
	}	
}
