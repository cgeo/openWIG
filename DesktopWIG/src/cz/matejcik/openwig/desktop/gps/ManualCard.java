package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.desktop.common.SwingHelpers;
import cz.matejcik.openwig.platform.LocationService;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** GPS device with manual input of coordinates.
 * <p>
 * Used mainly for testing, because playing Wherigo by entering coordinates
 * by hand is in all cases unbearable and in many cases you can't even
 * complete cartridges, because you don't know when to input the coordinates
 * to hit a hidden zone. For example.
 * <p>
 * At this point, it is very simplistic, it can only parse coordinates
 * in decimal format understood by <code>Double.parseDouble()</code>.
 * TODO: implement parsing of at least the Geocaching and DMS coordinate formats.
 */
public class ManualCard implements GPSManager.Card, LocationService {
	
	private double latitude, longitude;

	private Box card = Box.createVerticalBox();
	private JTextField lat = new JTextField();
	private JTextField lon = new JTextField();

	public ManualCard () {
		JPanel p = new JPanel();
		JLabel ll = new JLabel("Latitude:");
		ll.setLabelFor(lat);
		p.add(ll);
		p.add(lat);
		card.add(p);

		p = new JPanel();
		ll = new JLabel("Longitude:");
		ll.setLabelFor(lon);
		p.add(ll);
		p.add(lon);
		card.add(p);

		JButton jb = new JButton("Go");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				updateCoordinates();
			}
		});
		card.add(jb);

		SwingHelpers.setPreferredWidth(lat, 200);
		SwingHelpers.setPreferredWidth(lon, 200);
	}

	/** Updates the built-in <code>LocationService</code> with data from the text fields. */
	private void updateCoordinates () {
		try {
			latitude = Double.parseDouble(lat.getText());
			longitude = Double.parseDouble(lon.getText());
		} catch (Exception e) {
			// TODO sensible parsing
		}
	}

	public Component getCard () {
		return card;
	}

	public String getLabel () {
		return "Enter coordinates";
	}

	public String getKey () {
		return "manual";
	}

	public void saveSettings () { }

	public LocationService getGPS () {
		return this;
	}

	public boolean autoconnect () { return false; }

	public double getLatitude () { return latitude; }

	public double getLongitude () { return longitude; }

	public double getAltitude () { return 115; }

	public double getHeading () { return 0; }

	public double getPrecision () { return 1; }

	public int getState () {
		return LocationService.ONLINE;
	}

	public void connect () { }

	public void disconnect () { }

}
