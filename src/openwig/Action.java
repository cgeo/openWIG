package openwig;

import se.krka.kahlua.vm.*;

public class Action extends EventTable {
	
	private String name;
	private boolean parameter;
	private boolean enabled;
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Action.class, metatable);
	}
	
	public Action (LuaTable table) {
		this.table = table; // XXX deep copy needed?
		Object o = null;
		while ((o = table.next(o)) != null) {
			if (o instanceof String) setItem((String)o, table.rawget(o));
		}
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Text") {
			name = (String)value;
		} else if (key == "CmdWith") {
			parameter = LuaState.boolEval(value);
		} else if (key == "Enabled") {
			enabled = LuaState.boolEval(value);
		}
	}

	public String getName() {
		return name;
	}

	public boolean hasParameter() {
		return parameter;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
