package openwig;

import se.krka.kahlua.vm.*;
import java.util.Vector;

public class Thing extends Container {
	
	private boolean character = false;
	
	public Vector actions = new Vector();
	
	public Vector foreignActions = new Vector();
	
	public Thing(boolean character) {
		this.character = character;
	}
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Thing.class, metatable);
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Commands") {
			LuaTable lt = (LuaTable)value;
			Object i = null;
			while ((i = lt.next(i)) != null) {
				Action a = (Action)lt.rawget(i);
				a.name = (String)i;
				a.setActor(this);
				actions.addElement(a);
				if (a.hasParameter()) {
					
				}
			}
		} else super.setItem(key, value);
	}
	
	public int visibleActions() {
		int count = 0;
		for (int i = 0; i < actions.size(); i++) {
			Action c = (Action)actions.elementAt(i);
			if (c.isEnabled()) count++;
		}
		for (int i = 0; i < foreignActions.size(); i++) {
			Action c = (Action)foreignActions.elementAt(i);
			if (c.isEnabled()) count++;
		}
		return count;
	}
	
	public boolean isItem() {
		return !character;
	}
	
	public boolean isCharacter() {
		return character;
	}
}
