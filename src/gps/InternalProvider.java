package gps;

import javax.microedition.location.*;

public class InternalProvider implements gps.LocationProvider, LocationListener {
	
	private javax.microedition.location.LocationProvider provider;
	private int state;
	private QualifiedCoordinates coords = null;
	private float course = 0;

	public InternalProvider() throws Exception {
		try {
			Criteria c = new Criteria();
			c.setAltitudeRequired(true);
			c.setSpeedAndCourseRequired(true);
			c.setCostAllowed(true);
			provider = javax.microedition.location.LocationProvider.getInstance(c);
			provider.setLocationListener(this, -1, -1, -1);
			Location loc = provider.getLastKnownLocation();
			if (loc != null && loc.isValid()) {
				coords = loc.getQualifiedCoordinates();
				course = loc.getCourse();
				state = ONLINE;
			} else {
				coords = new QualifiedCoordinates(0, 0, -1000, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
				state = NO_FIX;
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

	public void locationUpdated(javax.microedition.location.LocationProvider prov, Location location) {
		if (location != null && location.isValid()) {
			coords = location.getQualifiedCoordinates();
			course = location.getCourse();
			state = ONLINE;
		} else {
			state = NO_FIX;
			// nothing, keep the old values?
		}
	}

	public void providerStateChanged(javax.microedition.location.LocationProvider prov, int newstate) {
		if (newstate != prov.AVAILABLE) state = OFFLINE;
	}

}
