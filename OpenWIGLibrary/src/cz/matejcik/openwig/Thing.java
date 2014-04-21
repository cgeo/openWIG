/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig;

import java.io.*;
import se.krka.kahlua.vm.*;
import java.util.Vector;
import se.krka.kahlua.stdlib.BaseLib;

public class Thing extends Container {
	
	private boolean character = false;

	protected String luaTostring () { return character ? "a ZCharacter instance" : "a ZItem instance"; }
	
	public Vector actions = new Vector();

	public Thing () {
		// for serialization
	}

	public void serialize (DataOutputStream out) throws IOException {
		out.writeBoolean(character);
		super.serialize(out);
	}

	public void deserialize (DataInputStream in) throws IOException {
		character = in.readBoolean();
		super.deserialize(in);
	}
	
	public Thing(boolean character) {
		this.character = character;
		table.rawset("Commands", Engine.platform.newTable());
	}
	
	protected void setItem (String key, Object value) {
		if ("Commands".equals(key)) {
			// clear out existing actions
			for (int i = 0; i < actions.size(); i++) {
				Action a = (Action)actions.elementAt(i);
				a.dissociateFromTargets();
			}
			actions.removeAllElements();

			// add new actions
			KahluaTableIterator it = ((KahluaTable)value).iterator();
			while (it.advance()) {
				Action a = (Action)it.getValue();
				Object ikey = it.getKey();
				//a.name = (String)i;
				if (ikey instanceof Double) a.name = KahluaUtil.numberToString((Double)ikey);
				else a.name = ikey.toString();
				a.setActor(this);
				actions.addElement(a);
				a.associateWithTargets();
			}
		} else super.setItem(key, value);
	}
	
	public int visibleActions() {
		int count = 0;
		for (int i = 0; i < actions.size(); i++) {
			Action c = (Action)actions.elementAt(i);
			if (!c.isEnabled()) continue;
			if (c.getActor() == this || c.getActor().visibleToPlayer()) count++;
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
