package openwig;

import java.util.Vector;
import se.krka.kahlua.vm.*;

public class Container extends EventTable {
	
	public Vector things = new Vector();
	protected Container location = null;
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Container.class, metatable);
	}

	private static class Method implements JavaFunction {
		
		private static final int MOVETO = 0;
		private static final int CONTAINS = 1;
		private int index;
		
		public Method(int i) {
			index = i;
		}
		
		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case CONTAINS:
					Container p = (Container) callFrame.get(0);
					Thing t = (Thing) callFrame.get(1);
					callFrame.push(LuaState.toBoolean(p.things.contains(t)));
					return 1;
				case MOVETO:
					Container subject = (Container) callFrame.get(0);
					Container target = (Container) callFrame.get(1);
					subject.moveTo(target);
					return 0;
				default:
					return 0;
			}
		}
	}
	
	private static Method moveto = new Method(Method.MOVETO);
	private static Method contains = new Method(Method.CONTAINS);
	
	public Container() {
		table.rawset("MoveTo", moveto);
		table.rawset("Contains", contains);
	}
	
	public void moveTo (Container c) {
		if (location != null) location.things.removeElement(this);
		if (c != null) c.things.addElement(this);
		location = c;
		table.rawset("Container", c);
	}
}
