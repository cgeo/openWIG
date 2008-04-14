package openwig;

import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;

public class Zone extends Container {

	public boolean isActive() {
		return active;
	}
	
	private ZonePoint[] points;

	private double atop = -500,  aleft = 500,  abottom = 500,  aright = -500;
	private boolean active = false;
	
	public boolean contains = false;
	public double distance = Double.MAX_VALUE;
	
	private static final double LATITUDE_COEF = 110940.00000395167;
	private static final double PI_180 = Math.PI / 180;
	
	public static void register(LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Zone.class, metatable);
	}

	private void approximate() {
		// XXX my idea will probably be broken somewhere in the Pacific,
		// and possibly on the poles too.
		// i doubt that Wherigo Player will be better off, though
		for (int i = 0; i < points.length; i++) {
			ZonePoint zp = points[i];
			atop = Math.max(atop, zp.latitude);
			abottom = Math.min(abottom, zp.latitude);
			aleft = Math.min(aleft, zp.longitude);
			aright = Math.max(aright, zp.longitude);
		}
	}
	
	protected void setItem (String key, Object value) {
		if (key == "Points" && value != null) {
			LuaTable lt = (LuaTable) value;
			int n = lt.len();
			points = new ZonePoint[n];
			for (int i = 1; i <= n; i++) {
				ZonePoint zp = (ZonePoint) lt.rawget(new Double(i));
				points[i-1] = zp;
			}
			approximate();
		} else if (key == "Active") {
			active = LuaState.boolEval(value);
		} else super.setItem(key, value);
	}

	public boolean contains(ZonePoint z) {
		if (!active || points.length == 0) {
			return false;
		}
		// TODO full polygon intersection
		
		double x, y, dist, ndist = Double.MAX_VALUE;
		double ax = points[0].latitude, ay = points[0].longitude;
		double nx = ax, ny = ay;
		int qtotal = 0, quad = ((ax > z.latitude) ? ((ay > z.longitude) ? 1 : 4) : ((ay > z.longitude) ? 2 : 3));
		for (int i = 1; i < points.length; i++) {
			double bx = points[i].latitude, by = points[i].longitude;
			int nextquad = ((bx > z.latitude) ? ((by > z.longitude) ? 1 : 4) : ((by > z.longitude) ? 2 : 3));
			int qdif = nextquad - quad;
			switch (qdif) {
				case 2: case -2:
					// find out where it passed
					double xaxis = (bx - (((by - z.longitude) * (ax - bx)) / (ay - by)));
					if (xaxis > z.latitude) qdif = -qdif;
					break;
				case 3: qdif = -1; break;
				case -3: qdif = 1; break;
			}
			qtotal += qdif; quad = nextquad;
			
			// find distance to vertex (ax,ay)-(bx,by)
			double dot_ta = (z.latitude - ax) * (bx - ax) + (z.longitude - ay) * (by - ay);
			if (dot_ta <= 0) {// IT IS OFF THE AVERTEX
				x = ax;
				y = ay;
			} else {
				double dot_tb = (z.latitude - bx) * (ax - bx) + (z.longitude - by) * (ay - by);
				if (dot_tb <= 0) { // SEE IF b IS THE NEAREST POINT - ANGLE IS OBTUSE
					x = bx;
					y = by;
				} else {
					// FIND THE REAL NEAREST POINT ON THE LINE SEGMENT - BASED ON RATIO
					x = ax + ((bx - ax) * dot_ta) / (dot_ta + dot_tb);
					y = ay + ((by - ay) * dot_ta) / (dot_ta + dot_tb);
				}
			}
			dist = x*x + y*y;
			if (dist < ndist) {
				nx = x; ny = y; ndist = dist;
			}
			ax = bx; ay = by;
		}
		
		double mx = Math.abs(nx - z.latitude) * LATITUDE_COEF;
		double my = Math.abs(ny - z.longitude);

		return (qtotal % 4 == 0);
		/*return (z.latitude > abottom &&
			z.latitude < atop &&
			z.longitude > aleft &&
			z.longitude < aright);*/
	}

	public int visibleThings() {
		int count = 0;
		for (int i = 0; i < things.size(); i++) {
			Thing z = (Thing)things.elementAt(i);
			if (z.isVisible()) count++;
		}
		return count;
	}
	
	public void reposition(ZonePoint diff) {
		for (int i = 0; i < points.length; i++) {
			points[i].diff(diff);
		}
		approximate();
	}
}
