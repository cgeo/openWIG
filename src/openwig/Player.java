package openwig;

import se.krka.kahlua.vm.*;

public class Player extends Thing {
	public ZonePoint position;
	
	private static class Method implements JavaFunction {
		public int call(LuaCallFrame callFrame, int nArguments) {
			callFrame.push(new Double(1));
			return 1;
		}
	}
	
	private static Method positionAccuracy_GetValue = new Method();
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Player.class, metatable);
	}
	
	public Player() {
		super(true);
		table.rawset("PositionAccuracy",this);
		table.rawset("GetValue", positionAccuracy_GetValue);
	}
	
	public int visibleThings() {
		int count = 0;
		for (int i = 0; i < things.size(); i++) {
			Thing t = (Thing)things.elementAt(i);
			if (t.isVisible()) count++;
		}
		return count;
	}
	
	public void setCompletionCode (String code) {
		table.rawset("CompletionCode", code);
	}
}
