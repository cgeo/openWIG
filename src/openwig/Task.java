package openwig;

import se.krka.kahlua.vm.*;

public class Task extends EventTable {
	
	private boolean active;
	private boolean complete;
	
	public static final int PENDING = 0;
	public static final int DONE = 1;
	public static final int FAILED = 2;
	
	public boolean isVisible () { return visible && active; }
	public boolean isComplete () { return complete; }
	public int state () {
		if (!complete) return PENDING;
		else return DONE;
	}
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Task.class, metatable);
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Visible") {
			visible = LuaState.boolEval(value);
		} else if (key == "Active") {
			active = LuaState.boolEval(value);
		} else if (key == "Complete") {
			complete = LuaState.boolEval(value);
		} else super.setItem(key, value);
	}
}
