package gps;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import gui.Midlet;
import javax.bluetooth.BluetoothConnectionException;

/**
 * Tato trida se stara o zpracovani NMEA zprav zasilanych od GPS
 */
public class GpsParser implements Runnable {
	//gps udaje
	protected double latitude;
	protected double longitude;
	protected String friendlyLatitude;
	protected String friendlyLongitude;
	protected double heading = 0,  speed = 0,  altitude = 0;
	protected double accuracy = 50;
	protected int allSatellites,  fixSatellites;
	protected boolean fix;
	protected int hour,  minute,  second;
	protected int day,  month,  year;
	protected int nmeaCount;
	//Zephy 21.11.07 gpsstatus+\
	protected Hashtable signaldata = new Hashtable(); //Obsahuje cisla satelitu a jejich signal;
	//Zephy oprava 21.12.07 +\
	protected int activsat[] = new int[30];           //seznam aktivnich satelitu
	//Zephy oprava 21.12.07 +/
	protected String pdop,  hdop,  vdop;
	//Zephy 21.11.07 gpsstatus+/
	private String communicationURL;
	protected String nmea;
	//zdroje dat
	public static final int BLUETOOTH = 0;
	public static final int GPS_GATE = 1;
	public static final int INTERNAL = 2;
	public int source;
	private Thread thread;

	/**
	 * Pripojeni k neznamemu zarizeni
	 */
	public GpsParser(String address, int gpsSource) {
		nmeaCount = 0;
		communicationURL = address;
		source = gpsSource;
	}

	/**
	 * getry a setry jednotlivych private promennych
	 */
	public boolean isOpen() {
		return thread != null;
	}

	public int getNmeaCount() {
		return nmeaCount;
	}

	public double getHeading() {
		return heading;
	}

	/**
	 * Vraci rychlost v km/h
	 */
	public long getSpeed() {
		if (source == INTERNAL) {
			return (long) (speed * 3.6);
		} else {
			return (long) (speed * 1.852);
		}
	}

	public double getAltitude() {
		return altitude;
	}

	public String getSatelliteCount() {
		if (source == INTERNAL) {
			return "N/A";
		} else {
			return fixSatellites + "/" + allSatellites;
		}
	}

	public String getAccuracy() {
		if (source == INTERNAL) {
			return String.valueOf((int) accuracy) + " m";
		} else {
			return accuracy + "(PDOP)";
		}
	}
	//Zephy 21.11.07 gpsstatus+\
	public String getPDOP() {
		return pdop;
	}

	public String getHDOP() {
		return hdop;
	}

	public String getVDOP() {
		return vdop;
	}
	//Zephy 21.11.07 gpsstatus+/
	public boolean hasFix() {
		return fix;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getFriendlyLatitude() {
		return friendlyLatitude;
	}

	public String getFriendlyLongitude() {
		return friendlyLongitude;
	}

	public String getDateTime() {
		return day + "." + month + ". " + hour + ":" + minute;
	}

	public String getNmea() {
		return nmea;
	}

	//Zephy 21.11.07 gpsstatus+\
	public Hashtable getSignalData() {
		return signaldata;
	}

	public void clearSignalData() {
		signaldata.clear();
	}

	public int[] getActivSat() {
		return activsat;
	}
	//Zephy 21.11.07 gpsstatus+/
	/**
	 * Otevreni spojeni z GPSkou
	 */
	public void open() {
		close();
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Zavreni spojeni z GPSkou
	 */
	public void close() {
		nmeaCount = 0;
		thread = null;
	}

	public void connectionSuccessful() {
		// notify midlet
	}
	
	public void fixChanged() {
		// notify midlet
	}

	/**
	 * Vlakno NMEA komunikace s GPS, na nic neceka, porad parsuje data
	 */
	public void run() {
		StreamConnection streamConnection = null;
		InputStream inputStream = null;
		try {
			streamConnection = (StreamConnection) Connector.open(communicationURL);
			inputStream = streamConnection.openInputStream();
		} catch (Exception e) {
			Midlet.error(e.toString());
			Midlet.coordinates.gpsDisconnected();
			close();
			return;
		}

		//uspesne pripojeni
		Midlet.coordinates.gpsConnected();

		//cteni dat
		ByteArrayOutputStream byteArrayOutputStream = null;
		boolean prevfix = fix;
		while (thread != null) {
			try {
				String s;
				byteArrayOutputStream = new ByteArrayOutputStream();
				int ch = 0;
				//cteni dat
				if (inputStream != null) {
					while ((ch = inputStream.read()) != '\n') {
						byteArrayOutputStream.write(ch);
					}
				}
				byteArrayOutputStream.flush();
				byte[] b = byteArrayOutputStream.toByteArray();
				s = new String(b);
				nmea = s;
				receiveNmea(s);
				byteArrayOutputStream.close();
				if (prevfix != fix) Midlet.coordinates.fixChanged(fix);
				prevfix = fix;
			} catch (BluetoothConnectionException e) {
				close();
				//Midlet.coordinates.gpsDisconnected();
				Midlet.coordinates.gpsError(e.getMessage());
			} catch (Exception ex) {
				Midlet.error(ex.toString());
			}
		}

		if (inputStream != null) try { inputStream.close(); } catch (Exception e) {}
		if (streamConnection != null) try { streamConnection.close(); } catch (Exception e) {}
		Midlet.coordinates.gpsDisconnected();
	}
	
	/**
	 * Funkce pro zjisteni souradnic s ruznych NMEA zprav
	 */
	private void extractData(String[] param, int a, int b, int c, int d, int e) {
		int degrees, minutes, fraction;
		String friendlyFraction;
		if (param[a].length() > 8 && param[b].length() == 1) {
			degrees = Integer.parseInt(param[a].substring(0, 2));
			minutes = Integer.parseInt(param[a].substring(2, 4));
			fraction = Integer.parseInt(param[a].substring(5, 9).concat("0000").substring(0, 4));
			friendlyFraction = param[a].substring(5, 9).concat("0000").substring(0, 4);
			latitude = degrees + ((double) minutes + (double) fraction / 10000) / 60;
			if (param[b].charAt(0) == 'S') latitude = -latitude;
			friendlyLatitude = param[b].charAt(0) + " " + degrees + "° " + minutes + "." + friendlyFraction;
			Midlet.latitude = latitude;
		}
		if (param[c].length() > 9 && param[d].length() == 1) {
			degrees = Integer.parseInt(param[c].substring(0, 3));
			String degree2 = param[c].substring(0, 3);
			minutes = Integer.parseInt(param[c].substring(3, 5));
			fraction = Integer.parseInt(param[c].substring(6, 10).concat("0000").substring(0, 4));
			friendlyFraction = param[c].substring(6, 10).concat("0000").substring(0, 4);
			longitude = degrees + ((double) minutes + (double) fraction / 10000) / 60;
			if (param[d].charAt(0) == 'W') longitude = -longitude;
			friendlyLongitude = param[d].charAt(0) + " " + degree2 + "° " + minutes + "." + friendlyFraction;
			Midlet.longitude = longitude;
		}
		if (param[e].length() > 5) {
			hour = Integer.parseInt(param[e].substring(0, 2));
			minute = Integer.parseInt(param[e].substring(2, 4));
			second = Integer.parseInt(param[e].substring(4, 6));
		}
	}

	//Zephy 21.11.07 gpsstatus+\
	protected void saveActivSat(String[] sat) {
		int Index = 0;
		int PocetPrvkuPole = sat.length;
		for (int i = 3; i <= 14; i++) {
			//Zephy oprava 20.12.07 +\
			if (i <= PocetPrvkuPole) //ochrana aby promenna "i" nelezla mimo rozsah pole
			{
				if (sat[i].equals("")) {
					activsat[Index] = 0;
				} else {
					activsat[Index] = Integer.parseInt(sat[i]);
				}
				Index++;
			}
		//Zephy oprava 20.12.07 +/
		}
	}

	//Zephy 21.11.07 gpsstatus+/
	/**
	 * Parsovani NMEA zprav a ziskavani potrebnych udaju
	 */
	protected void receiveNmea(String nmea) {
		try {
		int starIndex = nmea.indexOf('*');
		if (starIndex == -1) {
			return;
		}
		String[] param = split(nmea.substring(0,starIndex), ',');
		//Zephy oprava 20.12.07 +\
		int len = param.length;
		//Zephy oprava 20.12.07 +/
		if (param[0].equals("$GPGSV")) {
			nmeaCount++;
			allSatellites = Integer.parseInt(param[3]);
			//Zephy 21.11.07 gpsstatus+\
			if (param[2].equals("1")) {
				signaldata.clear();
			}

			//Zephy oprava 20.12.07 +\
			//1.cast
			if ((len > 7)) //kontrola zda nelezu mimo rozsah
			{
				String key = NullToNula(param[4]);
				String val = NullToNula(param[7]);
				signaldata.put(key, val);
			}
			//2.cast
			if ((len > 11)) //kontrola zda nelezu mimo rozsah
			{
				String key = NullToNula(param[8]);
				String val = NullToNula(param[11]);
				signaldata.put(key, val);
			}

			//3.cast
			if ((len > 15)) //kontrola zda nelezu mimo rozsah
			{
				String key = NullToNula(param[12]);
				String val = NullToNula(param[15]);
				signaldata.put(key, val);
			}

			//4.cast
			if ((len > 19)) //kontrola zda nelezu mimo rozsah
			{
				String key = NullToNula(param[16]);
				String val = NullToNula(param[19]);
				signaldata.put(key, val);
			}
		//Zephy oprava 20.12.07 +/

		//Zephy 21.11.07 gpsstatus+/

		} else if (param[0].equals("$GPGLL")) {
			nmeaCount++;
			extractData(param, 1, 2, 3, 4, 5);
			fix = (param[6].charAt(0) == 'A');
		} else if (param[0].equals("$GPRMC")) {
			nmeaCount++;
			extractData(param, 3, 4, 5, 6, 1);
			fix = (param[2].charAt(0) == 'A');
			if (fix) {
				day = Integer.parseInt(param[9].substring(0, 2));
				month = Integer.parseInt(param[9].substring(2, 4));
				year = 2000 + Integer.parseInt(param[9].substring(4, 6));
				if (param[7].length() > 0) {
					speed = Double.parseDouble(param[7]);
				} else {
					speed = 0;
				}
				if (param[8].length() > 0) {
					Midlet.heading = heading = Double.parseDouble(param[8]);
				}
			}
		} else if (param[0].equals("$GPGGA")) {
			nmeaCount++;
			if (param[6].equals("0")) {
				fix = false;
				//Zephy 21.11.07 gpsstatus+\
				fixSatellites = 0;
			//Zephy 21.11.07 gpsstatus+/
			} else {
				extractData(param, 2, 3, 4, 5, 1);
				fixSatellites = Integer.parseInt(param[7]);
				if (param[9].length() > 0) {
					altitude = Double.parseDouble(param[9]);
					Midlet.altitude = altitude;
				}
			}
		} else if (param[0].equals("$GPGSA")) {
			//Zephy 21.11.07 gpsstatus+\
			//Zephy oprava 20.12.07 +\
			if (param.length >= 17) //ochrana jestli veta ma vubec tolik prvku. I kdyz je divny ze by nemela.
			{
				if (param[15].length() > 0) {
					accuracy = Double.parseDouble(param[15]);
					pdop = param[15];
				}
				if (param[16].length() > 0) {
					hdop = param[16];
				}
				if (param[17].length() > 0) {
					vdop = param[17];
				}

				saveActivSat(param);
			//Zephy 21.11.07 gpsstatus+/
			} //Zephy oprava 20.12.07 +/
			else {
				accuracy = 0;
				pdop = "0";
				hdop = "0";
				vdop = "0";
			}
			nmeaCount++;
		} else if (!param[0].equals(null)) {
			nmeaCount++;
		}
		} catch (Exception e) {
			Midlet.error(e.toString()+"\n"+nmea);
		}
	}

	private String NullToNula(String Hodnota) {
		if (Hodnota.length() == 0) return "00";
		else return Hodnota;
	}

	private String[] split(String s, char sep) {
		Vector v = new Vector();
		int pos = 0;
		int found = 0;
		while ((found = s.indexOf(sep, pos)) > -1) {
			v.addElement(s.substring(pos, found));
			pos = found+1;
		}
		v.addElement(s.substring(pos));

		String[] ret = new String[v.size()];
		for (int i = 0; i < ret.length; i++) ret[i] = (String) v.elementAt(i);
		return ret;
	}
}


