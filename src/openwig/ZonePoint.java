package openwig;

import henson.midp.Float11;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.BaseLib;

public class ZonePoint {
	public double latitude;
	public double longitude;
	public double altitude;
	
	private static class Method implements JavaFunction {

		private static final int LEN = 0;
		private static final int INDEX = 1;
		private static final int NEWINDEX = 2;
		private int index;

		public Method(int index) {
			this.index = index;
		}

		private int len(LuaCallFrame frame, int n) {
			frame.push(LuaState.toDouble(3));
			return 1;
		}

		private int index(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 2, "not enough parameters");
			ZonePoint z = (ZonePoint) frame.get(0);
			String name = (String) frame.get(1);
			Object ret = null;
			if (name == "latitude")
				ret = LuaState.toDouble(z.latitude);
			else if (name == "longitude")
				ret = LuaState.toDouble(z.longitude);
			else if (name == "altitude")
				// ret = z.altitude;
				ret = LuaState.toDouble(z.altitude);
			frame.push(ret);
			return 1;
		}

		private int newindex(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 3, "not enough parameters");
			ZonePoint z = (ZonePoint) frame.get(0);
			String name = (String) frame.get(1);
			Object value = frame.get(2);
			if (name == "latitude")
				z.latitude = LuaState.fromDouble(value);
			else if (name == "longitude")
				z.longitude = LuaState.fromDouble(value);
			else if (name == "altitude")
				// z.altitude = value;
				z.altitude = LuaState.fromDouble(value);
			return 0;
		}

		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case LEN: return len(callFrame, nArguments);
				case INDEX: return index(callFrame, nArguments);
				case NEWINDEX: return newindex(callFrame, nArguments);
				default: return 0;
			}
		}
	}
	protected static LuaTable metatable;
	
	public static void register(LuaState state) {
		if (metatable == null) {
			metatable = new LuaTable();
			metatable.rawset("__metatable", "restricted");
			metatable.rawset("__len", new Method(Method.LEN));
			metatable.rawset("__index", new Method(Method.INDEX));
			metatable.rawset("__newindex", new Method(Method.NEWINDEX));
		}
		state.setUserdataMetatable(ZonePoint.class, metatable);
	}	
	
	public static final double LATITUDE_COEF = 110940.00000395167;
	public static final double METRE_COEF = 9.013881377e-6;
	public static final double PI_180 = Math.PI / 180;
	public static final double DEG_PI = 180 / Math.PI;
	public static final double PI_2 = Math.PI / 2;
	
	public static ZonePoint copy (ZonePoint z) {
		if (z == null) return null;
		else return new ZonePoint (z);
	}
	
	public ZonePoint (ZonePoint z) {
		latitude = z.latitude;
		longitude = z.longitude;
		altitude = z.altitude;
	}
	
	public ZonePoint (double lat, double lon, double alt)
	{
		latitude = lat;
		longitude = lon;
		altitude = alt;
	}
	
	public ZonePoint translate (double angle, double distance) {
		double rad = angle * PI_180;
		double x = m2lat(distance * Math.sin(rad));
		double y = m2lon(latitude, distance * Math.cos(rad));
		return new ZonePoint(latitude + x, longitude + y, altitude);
	}
	
	public void diff (ZonePoint z) {
		latitude -= z.latitude;
		longitude -= z.longitude;
	}
	
	public static double lat2m (double degrees) {
		return degrees * LATITUDE_COEF;
	}
	
	public static double lon2m (double latitude, double degrees) {
		return degrees * PI_180 * Math.cos(latitude * PI_180) * 6367449;
	}
	
	public static double m2lat (double metres) {
		return metres * METRE_COEF;
	}
	
	public static double m2lon (double latitude, double metres) {
		return metres / (PI_180 * Math.cos(latitude * PI_180) * 6367449);
	}
	
	public double distance (double lat, double lon) {
		double mx = Math.abs(ZonePoint.lat2m(lat - latitude));
		double my = Math.abs(ZonePoint.lon2m(latitude, lon - longitude));
		return Math.sqrt(mx * mx + my * my);
	}
	
	public double bearing (double lat, double lon) {
		// calculates bearing from specified point to here
		return Float11.atan2(latitude - lat, longitude - lon);
	}
	
	public static double angle2azimuth (double angle) {
		double degrees = -((angle - PI_2) * DEG_PI);
		while (degrees < 0) degrees += 360;
		while (degrees > 360) degrees -= 360;
		return degrees;
	}
	
	public static double azimuth2angle (double azim) {
		return -(azim * PI_180) + PI_2;
	}
}
