/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig;

import se.krka.kahlua.vm.*;

import java.util.Vector;

public class Action extends EventTable {
	
	private boolean parameter;
	private boolean reciprocal = true;
	private boolean enabled;

	private Thing actor = null;
	private Vector targets = new Vector();
	private boolean universal;
	
	public String text;
	public String notarget;

	public Action () {
		// for serialization
	}
	
	public Action (KahluaTable table) {
		this.table = table; // XXX deep copy needed?
		KahluaTableIterator i = table.iterator();
		while (i.advance()) {
			Object o = i.getKey();
			if (o instanceof String) setItem((String)o, i.getValue());
		}
	}

	public void associateWithTargets () {
		if (!hasParameter()) return;
		if (isReciprocal()) {
			for (int j = 0; j < targets.size(); j++) {
				Thing t = (Thing)targets.elementAt(j);
				if (!t.actions.contains(this))
					t.actions.addElement(this);
			}
		}
		if (isUniversal() && !Engine.instance.cartridge.universalActions.contains(this)) {
			Engine.instance.cartridge.universalActions.addElement(this);
		}
	}

	public void dissociateFromTargets () {
		if (!hasParameter()) return;
		if (isReciprocal()) {
			for (int j = 0; j < targets.size(); j++) {
				Thing t = (Thing)targets.elementAt(j);
				t.actions.removeElement(this);
			}
		}
		if (isUniversal()) {
			Engine.instance.cartridge.universalActions.removeElement(this);
		}
	}

	protected String luaTostring () { return "a ZCommand instance"; }
	
	protected void setItem (String key, Object value) {
		if ("Text".equals(key)) {
			text = (String)value;
		} else if ("CmdWith".equals(key)) {
			boolean np = KahluaUtil.boolEval(value);
			if (np != parameter) {
				if (np) {
					parameter = true;
					associateWithTargets();
				} else {
					dissociateFromTargets();
					parameter = false;
				}
			}
		} else if ("Enabled".equals(key)) {
			enabled = KahluaUtil.boolEval(value);
		} else if ("WorksWithAll".equals(key)) {
			// XXX bug: when the command is dissociated and somebody updates this, it will re-associate
			dissociateFromTargets();
			universal = KahluaUtil.boolEval(value);
			associateWithTargets();
		} else if ("WorksWithList".equals(key)) {
			dissociateFromTargets();
			KahluaTableIterator i = ((KahluaTable)value).iterator();
			while (i.advance()) {
				targets.addElement(i.getValue());
			}
			associateWithTargets();
		} else if ("MakeReciprocal".equals(key)) {
			dissociateFromTargets();
			reciprocal = KahluaUtil.boolEval(value);
			associateWithTargets();
		} else if ("EmptyTargetListText".equals(key)) {
			notarget = value == null ? "(not available now)" : value.toString();
		}
	}
	
	public int visibleTargets(Container where) {
		int count = 0;
		KahluaTableIterator i = where.inventory.iterator();
		while (i.advance()) {
			Object o = i.getValue();
			if (!(o instanceof Thing)) continue;
			Thing t = (Thing)o;
			if (t.isVisible() && (targets.contains(t) || isUniversal())) count++;
		}
		return count;
	}
	
	public int targetsInside(KahluaTable v) {
		int count = 0;
		KahluaTableIterator i = v.iterator();
		while (i.advance()) {
			Object o = i.getValue();
			if (!(o instanceof Thing)) continue;
			Thing t = (Thing)o;
			if (t.isVisible() && (targets.contains(t) || isUniversal())) count++;
		}
		return count;
	}
	
	public boolean isTarget(Thing t) {
		return targets.contains(t) || isUniversal();
	}

	public Vector getTargets () {
		return targets;
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
	}
	
	public Thing getActor () {
		return actor;
	}

	public boolean isReciprocal () {
		return reciprocal;
	}
}
