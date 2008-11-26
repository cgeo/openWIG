package gps;

import javax.microedition.location.*;

public class InternalProvider implements gps.LocationProvider, LocationListener {
	
	private javax.microedition.location.LocationProvider provider;
	private int state;
	private Location lastLocation = null;
	
	public InternalProvider () throws Exception {
		try {
		provider = javax.microedition.location.LocationProvider.getInstance(new Criteria());
		provider.setLocationListener(this, -1, -1, -1);
		lastLocation = provider.getLastKnownLocation();
		state = provider.getState();
		} catch (LocationException e) {
			throw new Exception(e.toString());
		}
	}

	public double getLatitude() {
		return lastLocation.getQualifiedCoordinates().getLatitude();
	}

	public double getLongitude() {
		return lastLocation.getQualifiedCoordinates().getLongitude();
	}

	public double getAltitude() {
		return lastLocation.getQualifiedCoordinates().getAltitude();
	}

	public double getHeading() {
		return lastLocation.getCourse();
	}

	public double getPrecision() {
		return lastLocation.getQualifiedCoordinates().getHorizontalAccuracy();
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
		lastLocation = location;
	}

	public void providerStateChanged(javax.microedition.location.LocationProvider prov, int newstate) {
		state = newstate;
	}

}
