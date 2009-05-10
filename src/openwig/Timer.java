package openwig;

import se.krka.kahlua.vm.*;
import gui.Midlet;

public class Timer extends EventTable {
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Timer.class, metatable);
	}
	
	private static class Method implements JavaFunction {
		private int index;
		
		public static final int START = 0;
		public static final int STOP = 1;
		public static final int TICK = 2;
		
		public Method(int index) {
			this.index = index;
		}

		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case START: return start(callFrame, nArguments);
				case STOP: return stop(callFrame, nArguments);
				case TICK: return tick(callFrame, nArguments);
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
		
		private int tick (LuaCallFrame frame, int n) {
			Timer t = (Timer)frame.get(0);
			//t.tick();
			t.callEvent("OnTick", null);
			return 0;
		}
	}
	
	private static Method startMethod = new Method(Method.START);
	private static Method stopMethod = new Method(Method.STOP);
	private static Method tickMethod = new Method(Method.TICK);
	
	private static final int COUNTDOWN = 0;
	private static final int INTERVAL = 1;
	private int type = COUNTDOWN;
	
	private long duration = -1;
	
	private long lastTick = 0;
	
	private boolean running = false;
	
	public Timer () {
		table.rawset("Start", startMethod);
		table.rawset("Stop", stopMethod);
		table.rawset("Tick", tickMethod);
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
