package gui;

import gps.GpsParser;
import javax.microedition.lcdui.*;
import javax.bluetooth.*;
import net.benhui.btgallery.bluelet.BLUElet;
import openwig.Engine;

public class Coordinates extends Form implements CommandListener, Pushable, Runnable {
	
	private TextField latitude = new TextField("latitude", null, 99, TextField.DECIMAL);
	private TextField longitude = new TextField("longitude", null, 99, TextField.DECIMAL);
	
	private StringItem lblGps = new StringItem("GPS: ", null);
	private StringItem lblLat = new StringItem("\nLatitude: ", null);
	private StringItem lblLon = new StringItem("\nLongitude: ", null);
	private StringItem lblAlt = new StringItem("\nAltitude: ", null);
	
	private StringItem lblRepos = new StringItem(null, "\n-- repositioned --");
	private StringItem lblLatR = new StringItem("\nLatitude: ", null);
	private StringItem lblLonR = new StringItem("\nLongitude: ", null);
	
	private static Command CMD_SET = new Command("Nastavit", Command.SCREEN, 0);
	
	private static Command CMD_GPS_ON = new Command("Zapnout GPS", Command.SCREEN, 5);
	private static Command CMD_GPS_OFF = new Command("Vypnout GPS", Command.SCREEN, 5);
	private static Command CMD_REPOSITION = new Command("Reposition", Command.SCREEN, 6);
	
	private static final int MODE_MANUAL = 0;
	private static final int MODE_GPS = 1;
	private int mode = MODE_MANUAL;
	
	private BLUElet bluelet;
	private static GpsParser gpsParser;
	
	private Alert gpsError;
	
	private static int gpsStatus = 0;
	
	private static final int GPS_OFF = 0;
	private static final int GPS_SEARCHING = 1;
	private static final int GPS_LOCKING = 2;
	private static final int GPS_ON = 3;
	private static String[] states = { "offline", "pøipojuje se", "zjišuje pozici", "online" };
	
	public Coordinates () {
		super("souøadnice");
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
		prepare();
	}
	
	public void prepare () {
		deleteAll();
		append(lblGps);
		switch (mode) {
			case MODE_MANUAL:
				removeCommand(CMD_GPS_OFF);
				removeCommand(CMD_REPOSITION);
				addCommand(CMD_SET);
				addCommand(CMD_GPS_ON);
				append(latitude);
				append(longitude);
				break;
			case MODE_GPS:
				removeCommand(CMD_SET);
				removeCommand(CMD_GPS_ON);
				addCommand(CMD_GPS_OFF);
				addCommand(CMD_REPOSITION);
				append(lblLat);
				append(lblLon);
				append(lblAlt);
				break;
		}
		if (Engine.shifted) {
			removeCommand(CMD_REPOSITION);
			append(lblRepos);
			append(lblLatR);
			append(lblLonR);
		}
		
		if (gpsStatus == GPS_ON) {
			start();
		} else {
			stop();
			updateScreen();
		}
	}
	
	private boolean running = false;
	
	synchronized public void start () {
		if (Midlet.getCurrentScreen() != this) return;
		if (!running) {
			running = true;
			Thread t = new Thread(this);
			t.start();
		}
	}
	synchronized public void stop () {
		running = false;
	}
	
	public void run () {
		while (running) {
			updateScreen();
			try { Thread.sleep(1000); } catch (Exception e) { }
		}
	}
	
	private String shortenNokiaDecimal(double d) {
		// Nokia Series40 can't handle TextField.setString with more than
		// 8 places after decimal point (when said TextField is in DECIMAL
		// mode. This should shorten it to 7 places.
		String num = String.valueOf(d);
		int len = num.length();
		int dot = num.indexOf('.');
		if (len > dot + 8) {
			return num.substring(0,dot+8);
		} else {
			return num;
		}
	}
	
	private void updateScreen () {
		lblGps.setText(states[gpsStatus]);
		if (gpsStatus == GPS_ON) {
			lblLat.setText(gpsParser.getFriendlyLatitude());
			lblLon.setText(gpsParser.getFriendlyLongitude());
			lblAlt.setText(String.valueOf(gpsParser.getAltitude()));
			
			latitude.setString(shortenNokiaDecimal(gpsParser.getLatitude()));
			longitude.setString(shortenNokiaDecimal(gpsParser.getLongitude()));
		} else {
			lblLat.setText(null);
			lblLon.setText(null);
			lblAlt.setText(null);
		}
		if (Engine.shifted) {
			lblLatR.setText(String.valueOf(Midlet.latitude + Engine.diff.latitude));
			lblLonR.setText(String.valueOf(Midlet.longitude + Engine.diff.longitude));
		}
	}

	private void startGPS() {
		bluelet = new BLUElet(Midlet.instance, this);
		bluelet.startApp();
		Midlet.push(bluelet.getUI());
		bluelet.startInquiry(DiscoveryAgent.GIAC, new UUID[]{new UUID(0x1101)});
	}

	private void stopGPS() {
		if (gpsParser != null) gpsParser.close();
		gpsParser = null;
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_GPS_ON) {
				startGPS();
			} else if (cmd == CMD_GPS_OFF) {
				stopGPS();
				mode = MODE_MANUAL;
				prepare();
			} else if (cmd == CMD_SET) {
				Midlet.latitude = Double.parseDouble(latitude.getString());
				Midlet.longitude = Double.parseDouble(longitude.getString());
				Midlet.altitude = 0;
			} else if (cmd == CMD_REPOSITION) {
				Engine.reposition(gpsParser.getLatitude(), gpsParser.getLongitude(), gpsParser.getAltitude());
				prepare();
			} else if (cmd == Midlet.CMD_BACK) {
				Midlet.pop(this);
			}
		} else if (bluelet != null && disp == bluelet.getUI()) {
			if (cmd == BLUElet.SELECTED) {
				Midlet.pop(bluelet.getUI());
				mode = MODE_GPS;
				gpsStatus = GPS_SEARCHING;
				prepare();
			} else if (cmd == BLUElet.COMPLETED) {
				ServiceRecord serviceRecord = bluelet.getFirstDiscoveredService();
				bluelet = null;
				String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
				gpsParser = new GpsParser(url, GpsParser.BLUETOOTH);
				gpsParser.open();
			} else if (cmd == BLUElet.BACK) {
				Midlet.pop(bluelet.getUI());
				bluelet = null;
			}
		} else if (disp == gpsError) {
			if (cmd.getCommandType() == Command.OK) {
				gpsParser.open();
			}
		}
	}
	
	public void gpsConnected () {
		gpsStatus = GPS_LOCKING;
		updateScreen();
	}
	
	public void gpsDisconnected () {
		gpsStatus = GPS_OFF;
		mode = MODE_MANUAL;
		prepare();
	}
	
	public void fixChanged (boolean fix) {
		if (fix) gpsStatus = GPS_ON;
		else gpsStatus = GPS_LOCKING;
		prepare();
	}
	
	public void gpsError (String error) {
		gpsError = new Alert("chyba bluetooth", "došlo k chybì v bluetooth spojení:\n"+error+"\nobnovit spojení?",
			null, AlertType.ERROR);
		gpsError.setTimeout(Alert.FOREVER);
		gpsError.addCommand(new Command("Ano", Command.OK, 1));
		gpsError.addCommand(new Command("Ne", Command.CANCEL, 2));
		Midlet.display.setCurrent(gpsError, Midlet.display.getCurrent());
	}
}
