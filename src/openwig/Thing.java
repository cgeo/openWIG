package openwig;

import java.io.*;
import se.krka.kahlua.vm.*;
import java.util.Vector;

public class Thing extends Container {
	
	private boolean character = false;
	
	public Vector actions = new Vector();

	public Thing () {
		// for serialization
	}

	public void serialize (DataOutput out) throws IOException {
		out.writeBoolean(character);
		super.serialize(out);
	}

	public void deserialize (DataInput in) throws IOException {
		character = in.readBoolean();
		super.deserialize(in);
	}
	
	public Thing(boolean character) {
		this.character = character;
	}
	
	protected void setItem (String key, Object value) {
		if ("Commands".equals(key)) {
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
		return count;
	}
	
	public boolean isItem() {
		return !character;
	}
	
	public boolean isCharacter() {
		return character;
	}
}
