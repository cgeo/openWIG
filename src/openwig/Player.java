package openwig;

import se.krka.kahlua.vm.*;

public class Player extends Container {
	public ZonePoint position;
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Player.class, metatable);
	}
	
	public int visibleThings() {
		int count = 0;
		for (int i = 0; i < things.size(); i++) {
			Thing t = (Thing)things.elementAt(i);
			if (t.isVisible()) count++;
		}
		return count;
	}
}
