package openwig;

import henson.midp.Float11;

public class ZonePoint {
	public double latitude;
	public double longitude;
	public double altitude;
	
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
