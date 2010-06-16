package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.platform.LocationService;
import java.awt.Component;
import javax.swing.JLabel;

/** Simulator GPS for easier debugging and testing cartridges.
 *
 * You can call <code>walkTo()</code> to place this "GPS" on specified
 * coordinates. In cooperation with DetailPane's simulator button,
 * you can use this to make OpenWIG think that you're walking around
 * in the right places.
 */
public class Simulator implements GPSManager.Card, LocationService {

	private JLabel description = new JLabel("Use buttons in navigation panes.", JLabel.CENTER);

	private double latitude, longitude;

	private static Simulator instance;

	public Simulator () {
		instance = this;
	}

	public Component getCard () {
		return description;
	}

	public String getLabel () {
		return "Simulation mode";
	}

	public String getKey () {
		return "simulator";
	}

	public void saveSettings () { }

	public LocationService getGPS () { return this; }

	public boolean autoconnect () { return false; }

	public double getLatitude () { return latitude; }

	public double getLongitude () { return longitude; }

	public double getAltitude () { return 0; }

	public double getHeading () { return 0; }

	public double getPrecision () { return 1; }

	public int getState () { return LocationService.ONLINE; }

	public void connect () { }

	public void disconnect () { }

	/** Places the GPS onto the specified <code>ZonePoint</code> */
	public static void walkTo (ZonePoint z) {
		instance.latitude = z.latitude;
		instance.longitude = z.longitude;
	}
}
