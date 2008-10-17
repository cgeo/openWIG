package gps;

public interface LocationProvider {
	public double getLatitude();
	public double getLongitude();
	public double getAltitude(); // in metres
	
	public double getHeading();
	public double getPrecision(); // in metres
}
