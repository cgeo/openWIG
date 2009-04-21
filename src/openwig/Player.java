package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;

public class Player extends Thing {

	private static class Method implements JavaFunction {
		public static final int REFRESHLOCATION = 0;
		
		private int index;
		public Method (int index) {
			this.index = index;
		}
		
		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case REFRESHLOCATION:
					return refreshLocation();
				default:
					return 0;
			}
		}
		
		private int refreshLocation () {
			Engine.instance.player.refreshLocation();
			return 0;
		}
	}
	
	private static Method refreshLocation = new Method(Method.REFRESHLOCATION);
	
	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Player.class, metatable);
	}
	
	public Player() {
		super(true);
		table.rawset("PositionAccuracy", new Distance(1,"metres"));
		table.rawset("RefreshLocation", refreshLocation);
		setPosition(new ZonePoint(360,360,0));
	}
	
	public int visibleThings() {
		int count = 0;
		Object key = null;
		while ((key = inventory.next(key)) != null) {
			Object o = inventory.rawget(key);
			if (o instanceof Thing && ((Thing)o).isVisible()) count++;
		}
		return count;
	}

	public void refreshLocation() {
		position.latitude = Midlet.gps.getLatitude();
		position.longitude = Midlet.gps.getLongitude();
		position.altitude.setValue(Midlet.gps.getAltitude(), "metres");
		Engine.instance.cartridge.walk(position);
	}
}
