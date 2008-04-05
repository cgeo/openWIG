package openwig;

import java.util.Vector;
import se.krka.kahlua.vm.*;

public class Player extends EventTable implements Container {
	private Vector things = new Vector();
	public ZonePoint position;
	
	private static class ContainsMethod implements JavaFunction {
		public int call(LuaCallFrame callFrame, int nArguments) {
			Player p = (Player)callFrame.get(0);
			Thing t = (Thing)callFrame.get(1);
			callFrame.push(LuaState.toBoolean(p.things().contains(t)));
			return 1;
		}
	}
	private static ContainsMethod contains = new ContainsMethod();
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Player.class, metatable);
	}
	
	public Player() {
		table.rawset("Contains", contains);
	}

	public Vector things() { return things; }
	
	public int visibleThings() {
		int count = 0;
		for (int i = 0; i < things.size(); i++) {
			Thing t = (Thing)things.elementAt(i);
			if (t.isVisible()) count++;
		}
		return count;
	}
}
