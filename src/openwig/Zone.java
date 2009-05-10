package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;

public class Zone extends Container {

	public boolean isVisible () {
		return active && visible && contain > NOWHERE;
	}
	
	public boolean isLocated () {
		return true;
	}
	
	private ZonePoint[] points;
	
	private boolean active = false;
	
	public static final int INSIDE = 2;
	public static final int PROXIMITY = 1;
	public static final int DISTANT = 0;
	public static final int NOWHERE = -1;
	
	public int contain = NOWHERE;
	public int ncontain = NOWHERE;
	private int ticks = 0;

	public static final int S_ALWAYS = 0;
	public static final int S_ONENTER = 1;
	public static final int S_ONPROXIMITY = 2;
	public static final int S_NEVER = 3;
	
	private int showObjects = S_ONENTER;
	
	public double distance = Double.MAX_VALUE; // distance in metres
	public double nearestX, nearestY;
	private double distanceRange = -1, proximityRange = -1;
	
	protected void setItem (String key, Object value) {
		if ("Points".equals(key) && value != null) {
			LuaTable lt = (LuaTable) value;
			int n = lt.len();
			points = new ZonePoint[n];
			for (int i = 1; i <= n; i++) {
				ZonePoint zp = (ZonePoint) lt.rawget(new Double(i));
				points[i-1] = zp;
			}
			if (active) {
				walk(Engine.instance.player.position);
				//setcontain();
			}
		} else if ("Active".equals(key)) {
			boolean a = LuaState.boolEval(value);
			if (a != active) callEvent("OnZoneState", null);
			active = a;
			if (active) {
				walk(Engine.instance.player.position);
				//setcontain();
			} else { // if the zone is deactivated, remove player, just to be sure
				contain = ncontain = (distanceRange < 0) ? DISTANT : NOWHERE;
				if (Engine.instance.player.location == this) Engine.instance.player.moveTo(null);
			}
		} else if ("Visible".equals(key)) {
			boolean a = LuaState.boolEval(value);
			if (a != visible) callEvent("OnZoneState", null);
			visible = a;
		} else if ("DistanceRange".equals(key) && value instanceof Distance) {
			distanceRange = ((Distance)value).getValue("meters");
			if (distanceRange < 0 && contain == NOWHERE) {
				contain = ncontain = DISTANT;
			}
		} else if ("ProximityRange".equals(key) && value instanceof Distance) {
			proximityRange = ((Distance)value).getValue("meters");
		} else if ("ShowObjects".equals(key)) {
			String v = (String)value;
			if ("Always".equals(v)) {
				showObjects = S_ALWAYS;
			} else if ("OnProximity".equals(v)) {
				showObjects = S_ONPROXIMITY;
			} else if ("OnEnter".equals(v)) {
				showObjects = S_ONENTER;
			} else if ("Never".equals(v)) {
				showObjects = S_NEVER;
			}
		} else super.setItem(key, value);
	}
	
	public void tick () {
		if (!active) return;
		if (ncontain == contain) ticks = 0;
		else if (ncontain > contain) setcontain();
		else if (Midlet.gpsType == Midlet.GPS_MANUAL) setcontain();
		else {
			ticks ++;
			if (ticks % 5 == 0) setcontain();
		}
	}
	
	private void setcontain () {
		if (contain == ncontain) return;
		if (contain == INSIDE) {
			//Engine.callEvent(this, "OnExit", null);
			callEvent("OnExit", null);
		}
		contain = ncontain;
		if (contain == INSIDE) {
			Engine.instance.player.moveTo(this);
		} else if (Engine.instance.player.location == this) {
			Engine.instance.player.moveTo(null);
		}
		switch (contain) {
			case INSIDE:
				//Engine.callEvent(this, "OnEnter", null);
				callEvent("OnEnter", null);
				break;
			case PROXIMITY:
				//Engine.callEvent(this, "OnProximity", null);
				callEvent("OnProximity", null);
				break;
			case DISTANT:
				//Engine.callEvent(this, "OnDistant", null);
				callEvent("OnDistant", null);
				break;
			case NOWHERE:
				//Engine.callEvent(this, "OnNotInRange", null);
				callEvent("OnNotInRange", null);
				break;
			default:
				return;
		}
		Midlet.refresh();
	}
	
	public void walk (ZonePoint z) {
		if (!active || points == null || points.length == 0 || z == null) {
			return;
		}
		
		double x, y, dist, ndist = Double.MAX_VALUE;
		double ax = points[0].latitude, ay = points[0].longitude;
		double nx = ax, ny = ay;
		int qtotal = 0, quad = ((ax > z.latitude) ? ((ay > z.longitude) ? 1 : 4) : ((ay > z.longitude) ? 2 : 3));
		for (int i = 1; i <= points.length; i++) {
			double bx = points[i % points.length].latitude, by = points[i % points.length].longitude;
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
			dist = (x-z.latitude)*(x-z.latitude) + (y-z.longitude)*(y-z.longitude);
			if (dist < ndist) {
				nx = x; ny = y; ndist = dist;
			}
			ax = bx; ay = by;
		}
		nearestX = nx; nearestY = ny;
		distance = z.distance(nx, ny);

		if (qtotal == 4 || qtotal == -4) ncontain = INSIDE;
		else if (distance < proximityRange) ncontain = PROXIMITY;
		else if (distance < distanceRange || distanceRange < 0) ncontain = DISTANT;
		else ncontain = NOWHERE;
		//tick();
	}

	private boolean showThings () {
		if (!active) return false;
		switch (showObjects) {
			case S_ALWAYS: return true;
			case S_ONPROXIMITY: return contain >= PROXIMITY;
			case S_ONENTER: return contain == INSIDE;
			case S_NEVER: return false;
			default: return false;
		}
	}
	
	public int visibleThings() {
		if (!showThings()) return 0;
		int count = 0;
		Object key = null;
		while ((key = inventory.next(key)) != null) {
			Object o = inventory.rawget(key);
			if (o instanceof Player) continue;
			if (!(o instanceof Thing)) continue;
			if (((Thing)o).isVisible()) count++;
		}
		return count;
	}
	
	public void collectThings (LuaTable c) {
		if (!showThings()) return;
		Object key = null;
		while ((key = inventory.next(key)) != null) {
			Object z = inventory.rawget(key);
			if (z instanceof Thing && ((Thing)z).isVisible())
				Engine.tableInsert(table, z);
		}
	}
	
	public boolean contains (Thing t) {
		if (t == Engine.instance.player) {
			return contain == INSIDE;
		} else return super.contains(t);
	}
}
