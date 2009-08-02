package openwig;

import java.io.DataInputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.*;

public class Container extends EventTable {

	public LuaTable inventory = new LuaTableImpl();
	protected Container location = null;
	
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
			callFrame.push(LuaState.toBoolean(p.contains(t)));
			return 1;
		}
	};
	
	public Container() {
		table.rawset("MoveTo", moveTo);
		table.rawset("Contains", contains);
		table.rawset("Inventory", inventory);
	}
	
	public void moveTo(Container c) {
		if (location != null) TableLib.removeItem(location.inventory, this);
		// location.things.removeElement(this);
		if (c != null) {
			Engine.tableInsert(c.inventory, this);
			location = c;
			table.rawset("Container", c);
		} else {
			location = null;
			table.rawset("Container", LuaState.toBoolean(false));
		}
	}

	public boolean contains (Thing t) {
		return TableLib.contains(inventory, t);
	}
	
	public boolean visibleToPlayer () {
		if (!isVisible()) return false;
		if (location == Engine.instance.player) return true;
		if (location instanceof Zone) {
			Zone z = (Zone)location;
			return z.showThings();
		}
		return false;
	}

	public void deserialize (DataInputStream in)
	throws IOException {
		super.deserialize(in);
		inventory = (LuaTable)table.rawget("Inventory");
		Object o = table.rawget("Container");
		if (o instanceof Container) location = (Container)o;
		else location = null;
	}
}
