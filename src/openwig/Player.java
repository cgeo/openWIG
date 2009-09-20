package openwig;

import gui.Midlet;
import java.io.DataInputStream;
import java.io.IOException;
import se.krka.kahlua.vm.*;

public class Player extends Thing {
	
	private static JavaFunction refreshLocation = new JavaFunction() {
		public int call (LuaCallFrame callFrame, int nArguments) {
			Engine.instance.player.refreshLocation();
			return 0;
		}
	};

	public static void register () {
		Engine.instance.savegame.addJavafunc(refreshLocation);
	}
	
	public Player() {
		super(true);
		table.rawset("PositionAccuracy", new Distance(1,"metres"));
		table.rawset("RefreshLocation", refreshLocation);
		setPosition(new ZonePoint(360,360,0));
	}

	public void deserialize (DataInputStream in)
	throws IOException {
		super.deserialize(in);
		Engine.instance.player = this;
		//setPosition(new ZonePoint(360,360,0));
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

	public void rawset (Object key, Object value) {
		if ("ObjectLocation".equals(key)) return;
		super.rawset(key, value);
	}

	public Object rawget (Object key) {
		if ("ObjectLocation".equals(key)) return ZonePoint.copy(position);
		return super.rawget(key);
	}
}
