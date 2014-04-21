/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.*;

public class Container extends EventTable {

	public KahluaTable inventory = Engine.platform.newTable();
	public Container container = null;
	
	private static JavaFunction moveTo = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Container subject = (Container) callFrame.get(0);
			Container target = (Container) callFrame.get(1);
			subject.moveTo(target);
			return 0;
		}
	};
	
	private static JavaFunction contains = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Container p = (Container) callFrame.get(0);
			Thing t = (Thing) callFrame.get(1);
			callFrame.push(KahluaUtil.toBoolean(p.contains(t)));
			return 1;
		}
	};

	public static void register (KahluaTable wherigo) {
		wherigo.rawset("MoveTo", moveTo);
		wherigo.rawset("Contains", contains);
	}
	
	protected Container() {
		table.rawset("MoveTo", moveTo);
		table.rawset("Contains", contains);
		table.rawset("Inventory", inventory);
	}
	
	public void moveTo(Container c) {
		String cn = c == null ? "(nowhere)" : c.name;
		Engine.log("MOVE: "+name+" to "+cn, Engine.LOG_CALL);
		if (container != null) TableLib.removeItem(container.inventory, this);
		// location.things.removeElement(this);
		if (c != null) {
			TableLib.rawappend(c.inventory, this);
			if (c == Engine.instance.player) setPosition(null);
			else if (position != null) setPosition(c.position);
			else if (container == Engine.instance.player) setPosition(ZonePoint.copy(Engine.instance.player.position));
			container = c;
		} else {
			container = null;
			rawset("ObjectLocation", null);
		}
	}

	public boolean contains (Thing t) {
		KahluaTableIterator i = inventory.iterator();
		while (i.advance()) {
			Object value = i.getValue();
			if (value instanceof Thing) {
				if (value == t) return true;
				if (((Thing)value).contains(t)) return true;
			}
		}
		return false;
	}
	
	public boolean visibleToPlayer () {
		if (!isVisible()) return false;
		if (container == Engine.instance.player) return true;
		if (container instanceof Zone) {
			Zone z = (Zone)container;
			return z.showThings();
		}
		return false;
	}
	
	public Object rawget (Object key) {
		if ("Container".equals(key)) return container;
		else return super.rawget(key);
	}
	
	public void serialize (DataOutputStream out)
	throws IOException {
		super.serialize(out);
		Engine.instance.savegame.storeValue(container, out);
	}

	public void deserialize (DataInputStream in)
	throws IOException {
		super.deserialize(in);
		container = (Container)Engine.instance.savegame.restoreValue(in, null);
		inventory = (KahluaTable)table.rawget("Inventory");
	}
}
