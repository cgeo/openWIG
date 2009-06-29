package gps;

public interface LocationService {
	public double getLatitude();
	public double getLongitude();
	public double getAltitude(); // in metres
	
	public double getHeading();
	public double getPrecision(); // in metres
	
	public static final int OFFLINE = 0;
	public static final int CONNECTING = 1;
	public static final int NO_FIX = 2;
	public static final int ONLINE = 3;
	public int getState ();
	
	public void connect();
	public void disconnect();
}
