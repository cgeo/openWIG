package gui;

import gps.LocationProvider;
import javax.microedition.lcdui.*;

public class Coordinates extends Form implements CommandListener, Pushable, Runnable, LocationProvider {
	
	private TextField latitude = new TextField("latitude", null, 99, TextField.DECIMAL);
	private TextField longitude = new TextField("longitude", null, 99, TextField.DECIMAL);
	
	private double lat = 0;
	private double lon = 0;
	
	private StringItem lblGps = new StringItem("GPS: ", null);
	private StringItem lblLat = new StringItem("\nLatitude: ", null);
	private StringItem lblLon = new StringItem("\nLongitude: ", null);
	private StringItem lblAlt = new StringItem("\nAltitude: ", null);
	
	private static Command CMD_SET = new Command("Set", Command.SCREEN, 0);
	
	private static Command CMD_GPS_ON = new Command("Connect", Command.SCREEN, 5);
	private static Command CMD_GPS_OFF = new Command("Disconnect", Command.SCREEN, 5);
	
	private Alert gpsError;

	private static String[] states = { "offline", "connecting", "no fix", "online" };
	
	public Coordinates () {
		super("GPS");
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
	}
	
	private void setMode () {
		deleteAll();
		append(lblGps);
		switch (Midlet.gpsType) {
			case Midlet.GPS_MANUAL:
				removeCommand(CMD_GPS_ON);
				removeCommand(CMD_GPS_OFF);
				addCommand(CMD_SET);
				append(latitude);
				append(longitude);
				break;
			default:
				removeCommand(CMD_SET);
				addCommand(CMD_GPS_ON);
				addCommand(CMD_GPS_OFF);
				append(lblLat);
				append(lblLon);
				append(lblAlt);
				break;
		}
	}
	
	public void prepare () {
		setMode();
		if (Midlet.gps != this && Midlet.gps.getState() != LocationProvider.OFFLINE) {
			start();
		} else {
			stop();
			updateScreen();
		}
	}
	
	private boolean running = false;
	
	synchronized public void start () {
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
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
			if (Midlet.getCurrentScreen() != this) stop();
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
	
	private String makeFriendlyAngle (double angle) {
		boolean neg = false; 
		if (angle < 0) {
			neg = true;
			angle *= -1;
		}
		int degrees = (int)angle;
		angle = (angle - degrees) * 60;
		String an = String.valueOf(angle);
		if (an.indexOf('.') != -1)
			an = an.substring(0, Math.min(an.length(), an.indexOf('.') + 5));
		return (neg ? "- " : "+ ") + String.valueOf(degrees) + "° " + an;
	}
	
	private String makeFriendlyLat (double angle) {
		return makeFriendlyAngle(angle).replace('+', 'N').replace('-', 'S');
	}
	
	private String makeFriendlyLon (double angle) {
		return makeFriendlyAngle(angle).replace('+', 'E').replace('-', 'W');
	}
	
	private void updateScreen () {
		lblGps.setText(states[Midlet.gps.getState()]);
		if (Midlet.gps == this) return;
		if (Midlet.gps.getState() == LocationProvider.ONLINE) {
			lblLat.setText(makeFriendlyLat(Midlet.gps.getLatitude()));
			lblLon.setText(makeFriendlyLon(Midlet.gps.getLongitude()));
			lblAlt.setText(String.valueOf(Midlet.gps.getAltitude()));
			
			latitude.setString(shortenNokiaDecimal(Midlet.gps.getLatitude()));
			longitude.setString(shortenNokiaDecimal(Midlet.gps.getLongitude()));
		} else {
			lblLat.setText(null);
			lblLon.setText(null);
			lblAlt.setText(null);
		}
	}

	private void startGPS() {
		Midlet.gps.connect();
	}

	private void stopGPS() {
		Midlet.gps.disconnect();
	}
	
	public double getLatitude() { return lat; }
	public double getLongitude() { return lon; }
	public double getAltitude() { return 100; }
	public double getHeading() { return 0; }
	public double getPrecision() { return 1; }
	
	private double scandbl (String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_GPS_ON) {
				startGPS();
				prepare();
			} else if (cmd == CMD_GPS_OFF) {
				stopGPS();
				prepare();
			} else if (cmd == CMD_SET) {
				lat = scandbl(latitude.getString());
				lon = scandbl(longitude.getString());
			} else if (cmd == Midlet.CMD_BACK) {
				Midlet.pop(this);
			}
		} else if (disp == gpsError) {
			if (cmd.getCommandType() == Command.OK) {
				Midlet.gps.connect();
				Midlet.display.setCurrent(Midlet.getCurrentScreen());
			} else {
				Midlet.showScreen(Midlet.COORDSCREEN, null);
			}
		}
	}
	
	public void gpsConnected () {
		updateScreen();
	}
	
	public void gpsDisconnected () {
		prepare();
	}
	
	public void fixChanged (boolean fix) {
		prepare();
	}
	
	public void gpsError (String error) {
		gpsDisconnected();
		gpsError = new Alert("GPS connection failed", error+"\nTry to reconnect?",
			null, AlertType.CONFIRMATION);
		gpsError.addCommand(new Command("Yes", Command.OK, 1));
		gpsError.addCommand(new Command("No", Command.CANCEL, 2));
		gpsError.setTimeout(Alert.FOREVER);
		gpsError.setCommandListener(this);
		Midlet.display.setCurrent(gpsError);
	}

	public int getState() {
		return LocationProvider.ONLINE;
	}

	public void connect() { }

	public void disconnect() { }
}
