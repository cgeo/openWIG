package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;

public class Player extends Thing {

	private static class Method implements JavaFunction {
		public static final int GETVALUE_NOP = 0;
		public static final int REFRESHLOCATION = 1;
		
		private int index;
		public Method (int index) {
			this.index = index;
		}
		
		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case GETVALUE_NOP:
					callFrame.push(new Double(1));
					return 1;
				case REFRESHLOCATION:
					return refreshLocation();
				default:
					return 0;
			}
		}
		
		private int refreshLocation () {
			ZonePoint z = Engine.instance.player.position;
			z.latitude = Midlet.latitude;
			z.longitude = Midlet.longitude;
			z.altitude = Midlet.altitude;
			Engine.instance.cartridge.walk(z);
			return 0;
		}
	}
	
	private static Method positionAccuracy_GetValue = new Method(Method.GETVALUE_NOP);
	private static Method refreshLocation = new Method(Method.REFRESHLOCATION);
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Player.class, metatable);
	}
	
	public Player() {
		super(true);
		table.rawset("PositionAccuracy",this);
		table.rawset("GetValue", positionAccuracy_GetValue);
		table.rawset("RefreshLocation", refreshLocation);
		setPosition(new ZonePoint(360,360,360));
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
		table.rawset("CompletionCode", code.intern());
	}
}
