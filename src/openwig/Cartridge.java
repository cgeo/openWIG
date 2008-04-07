package openwig;

import gui.Midlet;
import java.util.Vector;
import se.krka.kahlua.vm.*;

public class Cartridge extends EventTable {
	public Vector zones = new Vector();
	public Zone currentZone = null;
	
	public Vector things = new Vector();
	public Vector universalActions = new Vector();
	
	public static void register(LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Cartridge.class, metatable);
	}	
		
	public void newPosition (ZonePoint zp) {
		if (currentZone != null && !currentZone.contains(zp)) {
			Engine.callEvent(currentZone, "OnExit", null);
			currentZone = null;
			Midlet.refresh(); // XXX workaround for item counter
		}
		
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			if (z == currentZone) continue;
			if (z.contains(zp)) {
				if (currentZone != null) Engine.callEvent(currentZone, "OnExit", null);
				currentZone = z;
				Engine.callEvent(z, "OnEnter", null);
			}
		}
	}
	
	public int visibleZones () {
		int count = 0;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			if (z.isActive() && z.isVisible()) count++;
		}
		return count;
	}
	
	public int visibleThings () {
		if (currentZone == null) return 0;
		return currentZone.visibleThings();
	}
	
	public int visibleUniversalActions () {
		int count = 0;
		for (int i = 0; i < universalActions.size(); i++) {
			Action a = (Action)universalActions.elementAt(i);
			if (a.isEnabled()) count++;
		}
		return count;
	}
	
	public void reposition (ZonePoint diff) {
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			z.reposition(diff);
		}
	}
}
