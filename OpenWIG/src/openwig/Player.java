package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;

public class Player extends Thing {
	
	private static JavaFunction refreshLocation = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Engine.instance.player.refreshLocation();
			return 0;
		}
	};
	
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
