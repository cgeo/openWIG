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
	
	public ZonePoint[] points;
	
	private boolean active = false;
	
	public static final int INSIDE = 2;
	public static final int PROXIMITY = 1;
	public static final int DISTANT = 0;
	public static final int NOWHERE = -1;
	
	public int contain = NOWHERE;
	private int ncontain = NOWHERE;

	public static final int S_ALWAYS = 0;
	public static final int S_ONENTER = 1;
	public static final int S_ONPROXIMITY = 2;
	public static final int S_NEVER = 3;
	
	private int showObjects = S_ONENTER;
	
	public double distance = Double.MAX_VALUE; // distance in metres
	public double nearestX, nearestY;
	private double distanceRange = -1, proximityRange = -1;

	public double bbTop, bbBottom, bbLeft, bbRight; // zone's bounding box
	public double pbbTop, pbbBottom, pbbLeft, pbbRight; // pbb = proximity bounding box
	private ZonePoint bbCenter = new ZonePoint(0,0,0);
	private double diameter; // approximate zone diameter - distance from bounding-box center to farthest vertex
	private double insideTolerance = 5, proximityTolerance = 10, distantTolerance = 20; // hysteresis tolerance

	private static final double DEFAULT_PROXIMITY = 300.0;
	
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
			Engine.callEvent(this, "OnExit", null);
		}
		contain = ncontain;
		if (contain == INSIDE) {
			Engine.instance.player.moveTo(this);
		} else if (Engine.instance.player.location == this) {
			Engine.instance.player.moveTo(null);
		}
		switch (contain) {
			case INSIDE:
				Engine.log("ZONE: inside "+name);
				Engine.callEvent(this, "OnEnter", null);
				break;
			case PROXIMITY:
				Engine.log("ZONE: proximity "+name);
				Engine.callEvent(this, "OnProximity", null);
				break;
			case DISTANT:
				Engine.log("ZONE: distant "+name);
				Engine.callEvent(this, "OnDistant", null);
				break;
			case NOWHERE:
				Engine.log("ZONE: out-of-range "+name);
				Engine.callEvent(this, "OnNotInRange", null);
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

		double dist = 0;
		// are we inside proximity bounding-box?
		if (z.latitude > pbbBottom && z.latitude < pbbTop && z.longitude > pbbLeft && z.longitude < pbbRight) {
			ncontain = PROXIMITY;
			// are we within zone bounding box?
			if (z.latitude > bbBottom && z.latitude < bbTop && z.longitude > bbLeft && z.longitude < bbRight && points.length > 2) {
				// yes, we need precise inside evaluation
				// the following code is adapted from http://www.visibone.com/inpoly/
				double xt = z.latitude, yt = z.longitude;
				double ax = points[points.length - 1].latitude, ay = points[points.length - 1].longitude;
				boolean inside = false;
				for (int i = 0; i < points.length; i++) {
					double bx = points[i].latitude, by = points[i].longitude;
					double x1, y1, x2, y2;
					if (bx > ax) {
						x1 = ax; y1 = ay;
						x2 = bx; y2 = by;
					} else {
						x1 = bx; y1 = by;
						x2 = ax; y2 = ay;
					}
					if (x1 < xt && xt <= x2) { // consider!
						if (ay > yt && by > yt) { // we're completely below -> flip
							inside = !inside;
						} else if (ay < yt && by < yt) { // we're completely above -> ignore
							// ...
						} else if ((yt - y1)*(x2 - x1) < (y2 - y1)*(xt - x1)) {
							// we're below (hopefully)
							inside = !inside;
						}
					}
					ax = bx; ay = by;
				}
				if (inside) {
					ncontain = INSIDE;
					distance = dist = 0;
					nearestX = z.latitude; nearestY = z.longitude;
				}
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

		// account for tolerances (notice no breaks)
		if (ncontain < contain) switch (contain) {
			case DISTANT:
				if (dist - distantTolerance < distanceRange) ncontain = DISTANT;
			case PROXIMITY:
				if (dist - proximityTolerance < proximityRange) ncontain = PROXIMITY;
			case INSIDE:
				if (dist - insideTolerance < 0)	ncontain = INSIDE;
		}
	}

	public boolean showThings () {
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
				Engine.tableInsert(c, z);
		}
	}
	
	public boolean contains (Thing t) {
		if (t == Engine.instance.player) {
			return contain == INSIDE;
		} else return super.contains(t);
	}
}
