package openwig;

import java.util.Vector;
import se.krka.kahlua.vm.*;

public class Cartridge extends EventTable {
	public Vector zones = new Vector();
	public Zone currentZone = null;
	
	public Vector things = new Vector();
	public Vector universalActions = new Vector();
	
	public Vector tasks = new Vector();
	
	private static class Method implements JavaFunction {
		public int call (LuaCallFrame frame, int n) {
			return 0;
		}
	}
	private static Method m = new Method();
	
	public static void register(LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Cartridge.class, metatable);
	}
	
	public Cartridge () {
		table.rawset("RequestSync", m);
	}
		
	public void newPosition (ZonePoint zp) {
		if (currentZone != null && !currentZone.contains(zp)) {
			Engine.callEvent(currentZone, "OnExit", null);
			currentZone = null;
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
	
	public int visibleTasks () {
		int count = 0;
		for (int i = 0; i < tasks.size(); i++) {
			Task a = (Task)tasks.elementAt(i);
			if (a.isVisible()) count++;
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
