package gps;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.platform.LocationService;
import javax.microedition.location.*;

public class InternalProvider implements LocationService, LocationListener {
	
	private LocationProvider provider;
	private int state;
	private QualifiedCoordinates coords = null;
	private float course = 0;

	private static final int SAMPLES = 6;
	private double[] lastlat = new double[SAMPLES];
	private double[] lastlon = new double[SAMPLES];
	private ZonePoint point = new ZonePoint();
	private int li = 0;

	public InternalProvider() throws Exception {
		try {
			Criteria c = new Criteria();
			c.setAltitudeRequired(true);
			c.setSpeedAndCourseRequired(true);
			c.setCostAllowed(true);
			c.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
			// first attempt - our preferred config
			provider = LocationProvider.getInstance(c);
			if (provider == null) {
				// samsungs apparently don't provide this
				c.setSpeedAndCourseRequired(false);
				// second attempt
				provider = LocationProvider.getInstance(c);
				if (provider == null) {
					// third attempt - just give us whatever
					provider = LocationProvider.getInstance(null);
				}
			}

			if (provider != null) {
				//provider.setLocationListener(this, -1, -1, -1);
				provider.setLocationListener(this, 1, 1, 1);
				Location loc = LocationProvider.getLastKnownLocation();
				if (loc != null && loc.isValid()) {
					coords = loc.getQualifiedCoordinates();
					course = loc.getCourse();
					state = ONLINE;
				} else {
					coords = new QualifiedCoordinates(0, 0, -1000, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
					state = NO_FIX;
				}
			} else {
				coords = new QualifiedCoordinates(0, 0, -1000, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
				state = OFFLINE;
			}
		} catch (LocationException e) {
			throw new Exception(e.toString());
		}
	}

	public double getLatitude() {
		return coords.getLatitude();
	}

	public double getLongitude() {
		return coords.getLongitude();
	}

	public double getAltitude() {
		return coords.getAltitude();
	}

	public double getHeading() {
		return course;
	}

	public double getPrecision() {
		return coords.getHorizontalAccuracy();
	}

	public int getState() {
		return state;
	}

	public void connect() {
	}

	public void disconnect() {
	}

	public void locationUpdated(LocationProvider prov, Location location) {
		if (location != null && location.isValid()) {
			coords = location.getQualifiedCoordinates();

			lastlat[li] = coords.getLatitude();
			lastlon[li] = coords.getLongitude();
			li = (li + 1) % 5;

			course = location.getCourse();
			if (course == Float.NaN) {
				// calculate course from last samples
				int vs = 0;
				double aa = 0, oo = 0;
				for (int ls = (li + 4) % SAMPLES; ls != li; ls = (ls + 1) % SAMPLES) {
					double a = lastlat[ls], o = lastlon[ls];
					if (a != 0 && o != 0 && point.latitude != 0 && point.longitude != 0) {
						vs++;
						aa += a - point.latitude;
						oo += o - point.longitude;
					}
					point.latitude = a; point.longitude = o;
				}
				if (vs > 0) {
					aa /= vs;
					oo /= vs;
					point.latitude = 0; point.longitude = 0;
					course = (float)point.bearing(aa, oo);
				}
			}
			state = ONLINE;
		} else {
			state = NO_FIX;
			// nothing, keep the old values?
		}
	}

	public void providerStateChanged(LocationProvider prov, int newstate) {
		if (newstate != LocationProvider.AVAILABLE) state = OFFLINE;
	}

}
