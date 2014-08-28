package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.desktop.common.*;
import cz.matejcik.openwig.platform.LocationService;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.*;

/** Displays window for GPS configuration and handles
 * management of various GPS devices, their configuration
 * and stuff like autoconnecting.
 * <p>
 * It is a singleton. You can use it to display the GPS
 * dialog or get current GPS device
 */
public class GPSManager extends JDialog implements ActionListener {

	private static GPSManager instance;

	// ***** GPS STUFF *****
	/** Dummy <code>LocationService</code> to ensure that we don't
	 * crash before the user gets to configuration.
	 */
	private static final LocationService DUMMY_GPS = new LocationService() {
		public double getLatitude () { return 0; }
		public double getLongitude () { return 0; }
		public double getAltitude () { return 0; }
		public double getHeading () { return 0; }
		public double getPrecision () { return 0; }
		public int getState () { return LocationService.OFFLINE; }
		public void connect () { }
		public void disconnect () { }
	};

	/** current GPS */
	private LocationService gps = DUMMY_GPS;

	// buttons
	private JButton btnSave = new JButton("Save");
	private JButton btnClose = new JButton("Close");

	// ********* card flipper interface *********
	/** Interface for configuration panels of various GPS kinds */
	public static interface Card {
		/** Returns component suitable to be displayed in the GPS configuration dialog */
		public Component getCard ();
		/** Returns label for the GPS type combo */
		public String getLabel ();
		/** Returns key for unique identification of this Card object */
		public String getKey ();
		/** Stores configuration for this Card */
		public void saveSettings ();
		/** Returns <code>LocationService</code> managed by this Card */
		public LocationService getGPS ();
		/** Returns true if this device is supposed to connect automatically at startup. */
		public boolean autoconnect ();
	}

	private JComboBox gpsCombo = new JComboBox();
	private CardPanel cards = new CardPanel();
	private ArrayList<Card> cardList = new ArrayList<Card>();

	/** Install a new Card into the configuration panel */
	public void insertCard (Card card) {
		cardList.add(card);
		gpsCombo.addItem(card.getLabel());
		cards.add(card.getCard(), card.getKey());
	}

	private Component makeGPSSelectionPanel () {
		JPanel panel = new JPanel(new BorderLayout());

		// top part - dropdown for various cards of center
		JPanel topPart = new JPanel();
		JLabel l = new JLabel("GPS Type:");
		l.setLabelFor(gpsCombo);
		topPart.add(l);
		topPart.add(gpsCombo);

		panel.add(topPart, BorderLayout.NORTH);

		// center part - card layout with GPS types
		cards.setBorder(BorderFactory.createEtchedBorder());
		panel.add(cards, BorderLayout.CENTER);

		// bottom part - save, close
		Box bottomPart = Box.createHorizontalBox();
		bottomPart.add(Box.createHorizontalGlue());
		bottomPart.add(btnSave);
		bottomPart.add(btnClose);

		panel.add(bottomPart, BorderLayout.SOUTH);

		gpsCombo.addActionListener(this);
		btnSave.addActionListener(this);
		btnClose.addActionListener(this);

		return panel;
	}

	/** Returns the current Card or null if none is selected.
	 * (this most often happens at startup)
	 */
	private Card currentCard () {
		int i = gpsCombo.getSelectedIndex();
		if (i < 0) return null;
		else return cardList.get(gpsCombo.getSelectedIndex());
	}

	/** Makes the Card selected in the combo visible and sets
	 * global current GPS to that of this Card.
	 */
	private void updateCard () {
		Card card = currentCard();
		if (card == null) return;
		gps = card.getGPS();
		cards.show(card.getKey());
	}

	// ****** GPS status display ********
	private JLabel latitude, longitude, altitude, fixStatus;

	private static final String[] fixStates = {
		"Offline", "Connecting", "No fix", "Online"
	};

	private JLabel makeLabel (String text, int align, Font font) {
		JLabel l = new JLabel(text);
		l.setFont(font);
		l.setHorizontalAlignment(align);
		l.setAlignmentX(LEFT_ALIGNMENT);
		SwingHelpers.setMaximumWidth(l, Short.MAX_VALUE);
		return l;
	}

	private Component makeStatusDisplay () {
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Font label = UIManager.getFont("Label.font");
		Font light = new Font("Default", Font.PLAIN, label.getSize());
		box.add(makeLabel("GPS state:", JLabel.LEFT, label));
		box.add(fixStatus = makeLabel("no fix", JLabel.RIGHT, light));
		box.add(makeLabel("Latitude:", JLabel.LEFT, label));
		box.add(latitude = makeLabel("N/A", JLabel.RIGHT, light));
		box.add(makeLabel("Longitude:", JLabel.LEFT, label));
		box.add(longitude = makeLabel("N/A", JLabel.RIGHT, light));
		box.add(makeLabel("Altitude:", JLabel.LEFT, label));
		box.add(altitude = makeLabel("N/A", JLabel.RIGHT, light));

		SwingHelpers.setPreferredWidth(box, 150);
		refreshTimer = new FrameTimer(this, this, 500);

		return box;
	}

	/** Timer that handles refreshing of gps status panel. */
	private FrameTimer refreshTimer;

	/** Updates status display with data from gps (state, lat/lon/alt) */
	private void refreshTimer () {
		fixStatus.setText(fixStates[gps.getState()]);
		if (gps.getState() == LocationService.ONLINE) {
			latitude.setText(ZonePoint.makeFriendlyLatitude(gps.getLatitude()));
			longitude.setText(ZonePoint.makeFriendlyLongitude(gps.getLongitude()));
			altitude.setText(ZonePoint.makeFriendlyDistance(gps.getAltitude()));
		} else {
			latitude.setText("N/A");
			longitude.setText("N/A");
			altitude.setText("N/A");
		}
		repaint();
	}

	// **** the rest ****

	private GPSManager () {
		setModal(false);
		setTitle("GPS Configuration");

		setDefaultCloseOperation(HIDE_ON_CLOSE);

		getContentPane().add(makeStatusDisplay(), BorderLayout.WEST);
		getContentPane().add(makeGPSSelectionPanel(), BorderLayout.CENTER);

		insertCard(new ManualCard());
		//insertCard(new Simulator());
		insertCard(new NetworkCard());

		pack();

		loadSettings();
	}

	/** Loads global settings, sets the saved Card as current and
	 * if desired, autoconnects its device.
	 */
	private void loadSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		String key = prefs.get("gpsType", null);
		if (key == null) {
			// force gps configuration, like, now.
			setVisible(true);
		} else {
			for (Card card : cardList) {
				if (key.equals(card.getKey())) try {
					gps = card.getGPS();
					if (card.autoconnect()) gps.connect();
					gpsCombo.setSelectedItem(card.getLabel());
					cards.show(card.getKey());
				} catch (Exception e) { }
			}
		}
	}

	private void saveSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		Card card = currentCard();
		prefs.put("gpsType", card.getKey());
		card.saveSettings();
	}

	public static GPSManager getInstance () {
		if (instance == null) instance = new GPSManager();
		return instance;
	}

	/** Displays the GPS configuration dialog */
	public static void launch () {
		getInstance().setLocationByPlatform(true);
		getInstance().setVisible(true);
	}

	/** Disposes of the dialog and disconnects from GPS */
	public static void kill () {
		if (instance != null) {
			instance.dispose();
			instance.gps.disconnect();
		}
	}

	public void actionPerformed (ActionEvent e) {
		Object src = e.getSource();
		if (src == refreshTimer.getTimer()) {
			refreshTimer();
		} else if (src == gpsCombo) {
			updateCard();
		} else if (src == btnClose) {
			setVisible(false);
		} else if (src == btnSave) {
			saveSettings();
			setVisible(false);
		}
	}

	/** Returns global current GPS */
	public static LocationService getGPS () {
		return getInstance().gps;
	}

	public static void showError (String message) {
		JOptionPane.showMessageDialog(instance, message, "error", JOptionPane.ERROR_MESSAGE);
	}
}
