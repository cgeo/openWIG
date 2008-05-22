package openwig;

import java.util.Vector;
import se.krka.kahlua.vm.*;

public class Cartridge extends EventTable {
	public Vector zones = new Vector();
	
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
	
	private boolean locationSet = false;
	protected void setItem (String key, Object value) {
		if (key == "StartingLocation" && value != null && !locationSet) { // i.e. set only once
			Engine.instance.player.setPosition((ZonePoint)value);
			locationSet = true;
		} else super.setItem(key, value);
	}
		
	public void walk (ZonePoint zp) {		
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			z.walk(zp);
		}
	}
	
	public void tick () {
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			z.tick();
		}		
	}
	
	public int visibleZones () {
		int count = 0;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			if (z.isVisible()) count++;
		}
		return count;
	}
	
	public int visibleThings () {
		int count = 0;
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			count += z.visibleThings();
		}
		return count;
	}
	
	public Vector currentThings () {
		Vector ret = new Vector();
		for (int i = 0; i < zones.size(); i++) {
			Zone z = (Zone)zones.elementAt(i);
			z.collectThings(ret);
		}
		return ret;
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
