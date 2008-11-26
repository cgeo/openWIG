package gps;

import javax.microedition.location.*;

public class InternalProvider implements gps.LocationProvider {
	
	private javax.microedition.location.LocationProvider provider;
	
	public InternalProvider () throws Exception {
		try {
		provider = javax.microedition.location.LocationProvider.getInstance(new Criteria());
		} catch (LocationException e) {
			throw new Exception(e.toString());
		}
	}

	public double getLatitude() {
		return provider.getLastKnownLocation().getQualifiedCoordinates().getLatitude();
	}

	public double getLongitude() {
		return provider.getLastKnownLocation().getQualifiedCoordinates().getLongitude();
	}

	public double getAltitude() {
		return provider.getLastKnownLocation().getQualifiedCoordinates().getAltitude();
	}

	public double getHeading() {
		return provider.getLastKnownLocation().getCourse();
	}

	public double getPrecision() {
		QualifiedCoordinates c = provider.getLastKnownLocation().getQualifiedCoordinates();
		return c.getHorizontalAccuracy();
	}

	public int getState() {
		int ps = provider.getState();
		if (ps == provider.AVAILABLE) return ONLINE;
		else if (ps == provider.TEMPORARILY_UNAVAILABLE) return NO_FIX;
		else return OFFLINE;
	}

	public void connect() {
	}

	public void disconnect() {
	}

}
