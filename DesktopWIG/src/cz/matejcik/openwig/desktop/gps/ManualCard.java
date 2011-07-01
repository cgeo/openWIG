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
 * At this point, it is very simplistic, but parses DDD.ddd, DDD MM.mmm and
 * DDD MM SS.sss with either directional prefix or suffix.
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

	/* Parse a coordinate in any of these formats:
	 *   DDD.dddd
	 *   DDD MM.mmm
	 *   DDD MM SS.sss
	 * The hemisphere can either precede or follow these formats.  The
	 * hemisphere designator will be case-insensitive and depend on if this
	 * is a latitude (N/S) or longitude (E/W), or be a sign (+/-).  Right
	 * now, the negative specifier is checked while anything else is
	 * positive.
	 *
	 * Right now, where spaces are in the formats above, any non-digit or
	 * non-decimal character is accepted.  Decimal characters accepted are
	 * period and comma.  All digit portions are limited to the first nine
	 * digits.  Decimal portion is optional.
	 *
	 * The goal with this specification is that correct coordinate strings
	 * will be parsed correctly.  Other strings will be parsed as well as
	 * they can be.  Any values outside of the valid range will be clipped
	 * to the valid range.  (Latitude is -90 to +90.  Longitude is -180 to
	 * +180.)
	 */
	private static int[] coordDivisors = {1, 10, 100, 1000, 10000, 100000,
		1000000, 10000000, 100000000, 1000000000};

	private int coordClip (int value, int min_val, int max_val) {
		// Assume programmer passed correct parameters
		if (value < min_val) {
			return min_val;
		} else if (value > max_val) {
			return max_val;
		}
		return value;
	}

	private double parseCoordinate (String coordinate, boolean isLatitude) {
		int hem = 0;                    // Sign of hemisphere (0=not found)
		int[] value = {0, 0, 0, 0};    // Need at most 4 runs of digits
		int[] digits = {0, 0, 0, 0};    // Number of digits in each run
		int part_idx = -1;              // Index of current part
		int frac_idx = -1;              // Index of fractional part
		boolean inNumber = false;       // Currently parsing a number?
		boolean hemiSearch = true;      // Pre-hemisphere search is on?
		char[] coord = coordinate.toCharArray();

		// Just find first four runs of digits
		character_loop:
		for (char c : coord) {
			if (c >= '0' && c <= '9') {   // If a digit, ...
				hemiSearch = false;     // A digit has been found
				if (!inNumber) {         // If not currently in number, ...
					inNumber = true;
					value[++part_idx] = (int)c - (int)'0';
					digits[part_idx] = 1;
				} else if (digits[part_idx] < 9) {
					value[part_idx] = 10 * value[part_idx] + (int)c - (int)'0';
					digits[part_idx]++;    // Increment number of digits
				}
			} else {                      // Otherwise, not a digit
				// Is this a decimal point?  (American or European)
				if ((c == '.' || c == ',') && frac_idx < 0) { // 1st decimal
					frac_idx = part_idx + 1;            // Remember it
					hemiSearch = false; // Search is over
				}

				/* Check for hemisphere (First non-digit/dec after spaces)*/
				if (hemiSearch && c != ' ' & c != '\t') {
					hemiSearch = false; // Some hemisphere indicator found
					switch (c) {
						case '-':
							hem = -1;
							break;
						case 's':
						case 'S':
							hem = isLatitude ? -1 : +1; // Emphasize + or -
							break;
						case 'w':
						case 'W':
							hem = isLatitude ? +1 : -1; // Emphasize + or -
							break;
						case '+':           // Emphasize that '+' is handled
						default:            // "eEnN" also handled by this
							hem = +1;       // Some indicator was found
							break;
					}
				}

				// Was still in a range of digits?
				if (inNumber) {          // If was in a number, ...
					inNumber = false;
					if (part_idx == value.length - 1) {  // If enough ...
						break character_loop;           // ... stop
					}
				}
			}
		}

		// Check for direction specification as last character
		if (hem == 0) {                  // If hemisphere not yet set, ...
			for (int i = coord.length; --i >= 0;) { // Find last non-blank
				char c = coord[i];
				if (c >= '0' && c <= '9') {
					break;
				}
				if (c != ' ' && c != '\t') {
					switch (c) {
						case 's':
						case 'S':
							hem = isLatitude ? -1 : +1; // Emphasize + or -
							break;
						case 'w':
						case 'W':
							hem = isLatitude ? +1 : -1; // Emphasize + or -
							break;
					}
				}
			}
		}

		// Handle what has been parsed
		int deg_range = isLatitude ? 90 : 180;
		int deg = coordClip(value[0], -deg_range, deg_range);
		int min = coordClip(value[1], -59, 59);
		int sec = coordClip(value[2], -59, 59);
		double result = 0.0;    // Logic relies on this initialized to zero
		switch (frac_idx) {
			case 0:             // Fraction is very first value
				result = value[frac_idx] / (double)coordDivisors[digits[frac_idx]];
				break;
			case 1:             // Fraction is second value
				result = value[frac_idx] / (double)coordDivisors[digits[frac_idx]];
				result = deg + result;
				break;
			case 2:             // Fraction is third value
				result = value[frac_idx] / (double)coordDivisors[digits[frac_idx]];
				result = deg + (min + result) / 60.0;
				break;
			case 3:             // Fraction is fourth value
				result = value[frac_idx] / (double)coordDivisors[digits[frac_idx]];
				result = deg + min / 60.0 + (sec + result) / (60.0 * 60.0);
				break;
			default:
				switch (part_idx) {
					case -1:    // Nothing present
						break;
					case 0:     // Only degrees present
						result = deg;
						break;
					case 1:     // Degrees and minutes present
						result = deg + min / 60.0;
						break;
					default:    // Degrees, minutes and seconds present
						result = deg + min / 60.0 + sec / (60.0 * 60.0);
						break;
				}
				break;
		}

		if (hem < 0)
			result = -result;
		return result;
	}

        /** Updates the built-in <code>LocationService</code> with data from the text fields. */
	private void updateCoordinates () {
		try {
			latitude  = parseCoordinate(lat.getText(), true);
			longitude = parseCoordinate(lon.getText(), false);
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
