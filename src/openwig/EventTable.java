package openwig;

import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.BaseLib;

public class EventTable {

	public LuaTable table = new LuaTable();
	
	public String name, description;
	public ZonePoint position = null;
	protected boolean visible = false;
	
	public boolean isVisible() { return visible; }

	public void setPosition(ZonePoint location) {
		position = location;
		table.rawset("ObjectLocation", location);
	}
	
	public boolean isLocated() {
		return position != null;
	}

	private static class Method implements JavaFunction {

		private static final int LEN = 0;
		private static final int INDEX = 1;
		private static final int NEWINDEX = 2;
		private int index;

		public Method(int index) {
			this.index = index;
		}

		private int len(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 1, "not enough parameters");
			EventTable z = (EventTable) frame.get(0);
			frame.push(LuaState.toDouble(z.table.len()));
			return 1;
		}

		private int index(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 2, "not enough parameters");
			EventTable z = (EventTable) frame.get(0);
			frame.push(z.table.rawget(frame.get(1)));
			return 1;
		}

		private int newindex(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 3, "not enough parameters");
			EventTable z = (EventTable) frame.get(0);
			Object key = frame.get(1);
			Object value = frame.get(2);
			z.table.rawset(key, value);
			if (key instanceof String) {
				z.setItem((String) key, value);
			}
			return 0;
		}

		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case LEN: return len(callFrame, nArguments);
				case INDEX: return index(callFrame, nArguments);
				case NEWINDEX: return newindex(callFrame, nArguments);
				default: return 0;
			}
		}
	}
	protected static LuaTable metatable;

	public static void register(LuaState state) {
		if (metatable == null) {
			metatable = new LuaTable();
			metatable.rawset("__metatable", "restricted");
			metatable.rawset("__len", new Method(Method.LEN));
			metatable.rawset("__index", new Method(Method.INDEX));
			metatable.rawset("__newindex", new Method(Method.NEWINDEX));
		}
		state.setUserdataMetatable(EventTable.class, metatable);
	}

	protected void setItem(String key, Object value) {
		if (key == "Name") {
			name = (String)value;
		} else if (key == "Description") {
			description = (String)value;
		} else if (key == "Visible") {
			visible = LuaState.boolEval(value);
		} else if (key == "ObjectLocation") {
			position = ZonePoint.copy((ZonePoint)value);
		}
	}
	
	public void setTable (LuaTable table) {
		Object n = null;
		while ((n = table.next(n)) != null) {
			Object val = table.rawget(n);
			this.table.rawset(n, val);
			if (n instanceof String) setItem((String)n, val);
		}
	}
	
	public void callEvent(String name, Object param) {
		synchronized (Engine.state) {
			try {
				Object o = table.rawget(name.intern());
				if (o != null && o instanceof LuaClosure) {
					LuaClosure event = (LuaClosure) o;
					Engine.state.call(event, this, param, null);
				}
			} catch (Exception e) {
				Engine.stacktrace(e);
			}
		}
	}	
}
