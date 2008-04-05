package openwig;

import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;
import java.util.Vector;

public class Zone extends EventTable implements Container {

	public boolean isVisible() {
		return visible;
	}

	public boolean isActive() {
		return active;
	}
	
	private Vector points = new Vector();
	private Vector things = new Vector();
	private double atop = -500,  aleft = 500,  abottom = 500,  aright = -500;
	private boolean visible = false;
	private boolean active = false;
	
	public static void register(LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Zone.class, metatable);
	}

	private void approximate() {
		// XXX my idea will probably be broken somewhere in the Pacific,
		// and possibly on the poles too.
		// i doubt that Wherigo Player will be better off, though
		for (int i = 0; i < points.size(); i++) {
			ZonePoint zp = (ZonePoint) points.elementAt(i);
			atop = Math.max(atop, zp.latitude);
			abottom = Math.min(abottom, zp.latitude);
			aleft = Math.min(aleft, zp.longitude);
			aright = Math.max(aright, zp.longitude);
		}
		Engine.message("approx coordinates for zone '"+((String)table.rawget("Name"))+"' are:\ntop: "+atop+"\nbottom: "+abottom+"\nleft: "+aleft+"\nright: "+aright);
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Points" && value != null) {
			LuaTable lt = (LuaTable) value;
			int n = lt.len();
			for (int i = 1; i <= n; i++) {
				ZonePoint zp = (ZonePoint) lt.rawget(new Double(i));
				points.addElement(zp);
			}
			approximate();
		} else if (key == "Visible") {
			visible = LuaState.boolEval(value);
		} else if (key == "Active") {
			active = LuaState.boolEval(value);
		}
	}

	public boolean contains(ZonePoint z) {
		if (!active) {
			return false;
		}
		// TODO full polygon intersection
		return (z.latitude > abottom &&
			z.latitude < atop &&
			z.longitude > aleft &&
			z.longitude < aright);
	}

	public Vector things() { return things; }
	
	public int visibleThings() {
		int count = 0;
		for (int i = 0; i < things.size(); i++) {
			Thing z = (Thing)things.elementAt(i);
			if (z.isVisible()) count++;
		}
		return count;
	}
}
