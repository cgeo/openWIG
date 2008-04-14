package openwig;

import se.krka.kahlua.vm.*;

import java.util.Vector;

public class Action extends EventTable {
	
	private boolean parameter;
	private boolean enabled;

	private Thing actor = null;
	private Vector targets = new Vector();
	private boolean universal;
	
	public String text;
	
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
			text = (String)value;
		} else if (key == "CmdWith") {
			parameter = LuaState.boolEval(value);
		} else if (key == "Enabled") {
			enabled = LuaState.boolEval(value);
		} else if (key == "WorksWithAll") {
			universal = LuaState.boolEval(value);
		} else if (key == "WorksWithList") {
			LuaTable lt = (LuaTable)value;
			Object i = null;
			while ((i = lt.next(i)) != null) {
				targets.addElement(lt.rawget(i));
			}
			setTargets();
		}
	}
	
	private void setTargets() {
		if (actor == null) return;
		
		for (int i = 0; i < targets.size(); i++) {
			Thing target = (Thing)targets.elementAt(i);
			target.foreignActions.addElement(this);
		}
	}
	
	public int visibleTargets(Container where) {
		int count = 0;
		for (int i = 0; i < where.things.size(); i++) {
			Thing t = (Thing)where.things.elementAt(i);
			if (t.isVisible() && (targets.contains(t) || isUniversal())) count++;
		}
		return count;
	}
	
	public int targetsInside(Vector v) {
		int count = 0;
		for (int i = 0; i < v.size(); i++) {
			Thing t = (Thing)v.elementAt(i);
			if (t.isVisible() && (targets.contains(t) || isUniversal())) count++;
		}
		return count;

	}
	
	public boolean isTarget(Thing t) {
		return targets.contains(t) || isUniversal();
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
	
	public boolean isUniversal() {
		return universal;
	}
	
	public void setActor (Thing a) {
		actor = a;
		setTargets();
	}
	
	public Thing getActor () {
		return actor;
	}
}
