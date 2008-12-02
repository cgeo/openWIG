package gps;

import javax.microedition.location.*;

public class InternalProvider implements gps.LocationProvider, LocationListener {
	
	private javax.microedition.location.LocationProvider provider;
	private int state;
	private static final QualifiedCoordinates invalidCoordinates = new QualifiedCoordinates(360, 360, -360, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	private QualifiedCoordinates coords = null;
	private float course = 0;
	
	public InternalProvider () throws Exception {
		try {
		provider = javax.microedition.location.LocationProvider.getInstance(new Criteria());
		provider.setLocationListener(this, -1, -1, -1);
		Location loc = provider.getLastKnownLocation();
		if (loc != null && loc.isValid()) {
			coords = loc.getQualifiedCoordinates();
			course = loc.getCourse();
		} else coords = invalidCoordinates;
		state = provider.getState();
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
		if (state == provider.AVAILABLE) return ONLINE;
		else if (state == provider.TEMPORARILY_UNAVAILABLE) return NO_FIX;
		else return OFFLINE;
	}

	public void connect() {
	}

	public void disconnect() {
	}

	public void locationUpdated(javax.microedition.location.LocationProvider prov, Location location) {
		if (location != null && location.isValid()) {
			coords = location.getQualifiedCoordinates();
			course = location.getCourse();
		} else {
			// nothing, keep the old values?
		}
	}

	public void providerStateChanged(javax.microedition.location.LocationProvider prov, int newstate) {
		state = newstate;
	}

}
