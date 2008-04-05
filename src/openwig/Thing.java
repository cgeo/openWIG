package openwig;

import se.krka.kahlua.vm.*;
import java.util.Vector;

public class Thing extends EventTable {
	
	private boolean visible = false;
	private boolean character = false;
	private Container location = null;
	
	public Vector actions = new Vector();
	
	private static class MovetoMethod implements JavaFunction {
		public int call(LuaCallFrame callFrame, int nArguments) {
			Thing t = (Thing)callFrame.get(0);
			Container c = (Container)callFrame.get(1);
			if (t.location != null) t.location.things().removeElement(t);
			c.things().addElement(t);
			t.location = c;
			return 0;
		}
		
	}
	private static MovetoMethod moveTo = new MovetoMethod();
	
	public Thing(boolean character) {
		this.character = character;
		this.table.rawset("MoveTo", moveTo);
	}
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Thing.class, metatable);
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Visible") {
			visible = LuaState.boolEval(value);
		} else if (key == "Commands") {
			LuaTable lt = (LuaTable)value;
			Object i = null;
			while ((i = lt.next(i)) != null) {
				actions.addElement(lt.rawget(i));
			}
		}
	}
	
	public int visibleActions() {
		int count = 0;
		for (int i = 0; i < actions.size(); i++) {
			Action c = (Action)actions.elementAt(i);
			if (c.isEnabled()) count++;
		}
		return count;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isItem() {
		return !character;
	}
	
	public boolean isCharacter() {
		return character;
	}
}
