package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.platform.LocationService;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/** NMEAParser
 * <p>
 * This class translates NMEA sentences from the specified
 * input stream to sensible location data and provides them
 * through the LocationService interface.
 * <p>
 * The code is originally (c) Destil of HandyGeocaching,
 * released under GPL.
 * It was slightly modified for use in OpenWIG, mainly by genericizing
 * some actions, but the code is largely unchanged, so the comments
 * (including Javadocs) are from original author(s).
 */
public class NMEAParser implements Runnable, LocationService {
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
	protected int nmeaCount = 0;
	//Zephy 21.11.07 gpsstatus+\
	protected Hashtable signaldata = new Hashtable(); //Obsahuje cisla satelitu a jejich signal;
	//Zephy oprava 21.12.07 +\
	protected int activsat[] = new int[30];           //seznam aktivnich satelitu
	//Zephy oprava 21.12.07 +/
	protected String pdop,  hdop,  vdop;
	//Zephy 21.11.07 gpsstatus+/
	private String communicationURL;
	//zdroje dat
	private Thread thread = null;
	protected boolean connected = false;

	protected InputStream stream;

	protected Listener listener;

	public void setListener (Listener l) {
		listener = l;
	}

	protected void fireEvent (int event) {
		if (listener != null) listener.statusChanged(this, event);
	}

	public static interface Listener {
		public static final int CONNECTING = 0;
		public static final int CONNECTED = 1;
		public static final int FIX_ACQUIRED = 2;
		public static final int FIX_LOST = 3;
		public static final int DISCONNECTED = 4;

		public void statusChanged (NMEAParser source, int event);
	}

	public NMEAParser(InputStream stream) {
		this.stream = stream;
	}

	protected NMEAParser () { }

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
/*		if (source == INTERNAL) {
			return (long) (speed * 3.6);
		} else */{
			return (long) (speed * 1.852);
		}
	}

	public double getAltitude() {
		return altitude;
	}

	public String getSatelliteCount() {
/*		if (source == INTERNAL) {
			return "N/A";
		} else */{
			return fixSatellites + "/" + allSatellites;
		}
	}

	public String getAccuracy() {
/*		if (source == INTERNAL) {
			return String.valueOf((int) accuracy) + " m";
		} else */{
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
	
	public double getPrecision() {
		try {
			return Double.parseDouble(getPDOP()) * 5.0;
		} catch (Exception e) {
			return Double.POSITIVE_INFINITY;
		}
	}
	
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

	/** Connects and starts parsing the stream. */
	public void connect() {
		disconnect();
		thread = new Thread(this);
		thread.start();
	}

	/** Stops parsing and closes the stream. */
	public void disconnect() {
		if (stream != null) try { stream.close(); } catch (Exception e) {}
		connected = false;
		fireEvent(Listener.DISCONNECTED);
		nmeaCount = 0;
		thread = null;
	}

	private byte[] buffer = new byte[2048];
	private char[] charbuf = new char[2048];
	private int bufpos = 0;
	private String readline (InputStream is) throws IOException {
		int lbp = 0;
		endlessloop: while (true) {
			// find line in existing buffer content
			for (int i = lbp; i < bufpos; i++) {
				if (buffer[i] == '\n') break endlessloop;
			}
			// if not found, prefill buffer
			int av = is.available();
			lbp = bufpos;
			if (av > 0) {
				// read all of it into a buffer
				int len = Math.min(av, buffer.length - bufpos);
				bufpos += is.read(buffer, bufpos, len);
				if (bufpos == lbp) throw new IOException("GPS device disconnected.");
			} else {
				// read just one char
				int r = is.read();
				if (r == -1) throw new IOException("GPS device disconnected.");
				buffer[bufpos++] = (byte)r;
			}

			if (thread == null) // disconnect "gracefully"
				return "";
		}
		// now we either exception-ed out or have the line in buffer
		int br = 0;
		for (br = 0; br < bufpos; br++) {
			if (buffer[br] == '\n') break;
			charbuf[br] = (char)buffer[br];
		}
		String ret = String.valueOf(charbuf, 0, br);
		bufpos -= br + 1;
		System.arraycopy(buffer, br + 1, buffer, 0, bufpos);
		return ret;
	}


	/** Connects to a device.
	 * Subclasses should override this to do hardware-specific connection initialization.
	 * When this method returns, <code>connected</code> should be true.
	 * @throws IOException when the connection fails
	 */
	protected void makeConnection () throws IOException {
		connected = true;
	}

	/**
	 * Vlakno NMEA komunikace s GPS, na nic neceka, porad parsuje data
	 */
	public void run() {
		fireEvent(Listener.CONNECTING);
		try {
			makeConnection ();
			fireEvent(Listener.CONNECTED);

			//cteni dat
			boolean prevfix = false;
			while (thread != null) {
				receiveNmea(readline(stream));
				if (prevfix != fix) fireEvent(fix ? Listener.FIX_ACQUIRED : Listener.FIX_LOST);
				prevfix = fix;
			}
		} catch (IOException ex) {
			//Midlet.coordinates.gpsError(ex.getMessage());
		} catch (Throwable e) {
			GPSManager.showError("GPS thread died: "+e.toString());
		} finally {
			disconnect();
		}
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
			friendlyLatitude = param[b].charAt(0) + " " + degrees + "� " + minutes + "." + friendlyFraction;
		}
		if (param[c].length() > 9 && param[d].length() == 1) {
			degrees = Integer.parseInt(param[c].substring(0, 3));
			String degree2 = param[c].substring(0, 3);
			minutes = Integer.parseInt(param[c].substring(3, 5));
			fraction = Integer.parseInt(param[c].substring(6, 10).concat("0000").substring(0, 4));
			friendlyFraction = param[c].substring(6, 10).concat("0000").substring(0, 4);
			longitude = degrees + ((double) minutes + (double) fraction / 10000) / 60;
			if (param[d].charAt(0) == 'W') longitude = -longitude;
			friendlyLongitude = param[d].charAt(0) + " " + degree2 + "� " + minutes + "." + friendlyFraction;
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
					heading = Double.parseDouble(param[8]);
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
				fix = true;
				if (param[9].length() > 0) {
					altitude = Double.parseDouble(param[9]);
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
		} catch (Throwable e) {
			GPSManager.showError(e.toString()+"\n"+nmea);
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

	public int getState() {
		if (thread == null) return LocationService.OFFLINE;
		if (! connected) return LocationService.CONNECTING;
		if (! fix) return LocationService.NO_FIX;
		return LocationService.ONLINE;
	}
}


