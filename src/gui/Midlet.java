package gui;

import gps.InternalProviderRedirector;
import gps.LocationService;
import gps.NMEAParser;
import gwc.CartridgeFile;
import java.io.OutputStream;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import java.util.Vector;

import se.krka.kahlua.vm.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;
import util.Config;

public class Midlet extends MIDlet implements CommandListener {
	
	public static Coordinates coordinates;
	public static Options options;
	public static Zones zones;
	public static Things inventory, surroundings;
	public static Tasks tasks;
	public static MainMenu mainMenu;
	public static Browser browser;
	
	private static List baseMenu;
	private static final int MNU_START = 0;
	private static final int MNU_GPS = 1;
	private static final int MNU_OPTIONS = 2;
	private static final int MNU_END = 3;
	
	public static final int MAINSCREEN = 0;
	public static final int DETAILSCREEN = 1;
	public static final int INVENTORYSCREEN = 2;
	public static final int ITEMSCREEN = 3;
	public static final int LOCATIONSCREEN = 4;
	public static final int TASKSCREEN = 5;	
	public static final int COORDSCREEN = 6;
	
	public static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 10);
	public static final Command CMD_SELECT = new Command("Select", Command.ITEM, 0);
	public static final Command CMD_CANCEL = new Command("Cancel", Command.BACK, 2);
	public static final Command CMD_BACK = new Command("Back", Command.BACK, 9);
	
	public static Midlet instance;
	public static Display display;
	public static Config config;
	
	private static Cancellable currentDialog = null;
	private static Vector screens = new Vector();
	private static Displayable currentScreen = null;
	
	public static Displayable getCurrentScreen () { return currentScreen; }
	
	public static LocationService gps;
	public static final int GPS_MANUAL = 0;
	public static final int GPS_SERIAL = 1;
	public static final int GPS_BLUETOOTH = 2;
	public static final int GPS_INTERNAL = 3;
	public static final int GPS_SOCKET = 4;
	public static int gpsType;
	
	/////////////////////////////////////
	//
	//    Midlet maintenance
	
	public void startApp() {
		instance = this;
		display = Display.getDisplay(this);
		config = new Config("_configuration");
		
		baseMenu = new List("menu", List.IMPLICIT);
		baseMenu.append("Start", null);
		baseMenu.append("GPS", null);
		baseMenu.append("Options", null);
		baseMenu.append("Quit", null);
		baseMenu.setSelectCommand(CMD_SELECT);
		baseMenu.setCommandListener(this);
		
		coordinates = new Coordinates();
		options = new Options();
		browser = new Browser();
		
		resetGps();
		
		push(baseMenu);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
		Engine.kill();
		notifyDestroyed();
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == baseMenu) {
			if (cmd == CMD_SELECT) {
				switch (baseMenu.getSelectedIndex()) {
					case MNU_START:
						push(browser);
						break;
						
					case MNU_GPS:
						push(coordinates);
						break;
						
					case MNU_OPTIONS:
						push(options);
						break;
						
					case MNU_END:
						destroyApp(false);
						break;
				}
			}
		} else if (cmd.getCommandType() == Command.EXIT) {
			end();
		}
	}
	
	////////////////////////////////////////////
	//
	//   functions for other components to use
	
	public static void error (String text) {
		Alert a = new Alert("error",text,null,AlertType.ERROR);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a, display.getCurrent());
	}
	
	public static void pushDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
		Dialog d = new Dialog(texts, media, button1, button2, callback);

//		display.flashBacklight(500);
//		display.vibrate(500);
	
		synchronized (Midlet.class) {
			popCurrentDialog();
			currentDialog = d;
			push(d);
		}
	}
	
	public static void pushInput(EventTable input) {
		Input i = new Input(input);
		synchronized (Midlet.class) {
			popCurrentDialog();
			currentDialog = i;
			push(i);
		}
	}
	
	synchronized public static void popCurrentDialog () {
		if (currentDialog != null) popDialog(currentDialog);
	}
	
	public static void popDialog(Cancellable d) {
		d.cancel();
		pop((Displayable)d);
		synchronized (Midlet.class) {
			if (currentDialog == d) currentDialog = null;
		}
	}
	
	public static void push (Displayable d) {
		if (d instanceof Pushable) ((Pushable)d).prepare();
		synchronized (Midlet.class) {
			screens.addElement(d);
			currentScreen = d;
			display.setCurrent(d);
		}
	}
	
	synchronized public static void pop (Displayable disp) {
		int ss = screens.size();
		for (int i = ss-1; i >= 0; i--) {
			Object o = screens.elementAt(i);
			if (o == disp) {
				screens.removeElementAt(i);
				ss--;
				break;
			}
		}
		if (ss == 0) {
			screens.addElement(baseMenu);
			ss = 1;
		}
		
		currentScreen = (Displayable)screens.elementAt(ss-1);
		display.setCurrent(currentScreen);
	}
	
	/////////////////
	//
	//   ????

	public static void resetGps () {
		if (gps != null) gps.disconnect();
		gpsType = config.getInt(Config.GPS_TYPE);
		switch (gpsType) {
			case GPS_MANUAL:
				gps = coordinates;
				break;
			case GPS_SERIAL:
				gps = new NMEAParser("comm:"+config.get(Config.GPS_SERIAL_PORT)+";baudrate=9600");
				break;
			case GPS_BLUETOOTH:
				gps = new NMEAParser(config.get(Config.GPS_BT_URL));
				break;
			case GPS_SOCKET:
				gps = new NMEAParser("socket://localhost:"+config.get(Config.GPS_TCP_PORT));
				break;
			case GPS_INTERNAL: try {
				gps = InternalProviderRedirector.getInstance();
				break;
				} catch (Exception e) { error(e.getMessage()); }
				// if exception is caught, no break and fallback to manual
			default:
				gpsType = GPS_MANUAL;
				gps = coordinates;
		}
	}
	
	public static void loadCartridge (CartridgeFile cf, OutputStream log) {
		try {
		Form f = new Form("splash");
		f.append(new StringItem(null, "Starting..."));
		f.addCommand(CMD_EXIT);
		f.setCommandListener(instance);
		Display.getDisplay(instance).setCurrent(f);

		Thread t = new Thread(new Engine(cf, log));
		t.start();
		} catch (Throwable t) {
			error(t.toString());
		}
	}
	
	public static void start () {
		mainMenu = new MainMenu();
		zones = new Zones();
		inventory = new Things("Inventory", Things.INVENTORY);
		surroundings = new Things("You see", Things.SURROUNDINGS);
		tasks = new Tasks();
		synchronized (Midlet.class) {
			popCurrentDialog();
			while (currentScreen != baseMenu) pop(currentScreen);
		}
		push(mainMenu);
	}
	
	public static void end () {
		Engine.kill();
		mainMenu = null; zones = null; inventory = null; surroundings = null; tasks = null;
		synchronized (Midlet.class) {
			popCurrentDialog();
			while (currentScreen != baseMenu) pop(currentScreen);
		}
	}
	
	public static void refresh () {
		Vector p = new Vector();
		synchronized (Midlet.class) {
			int ss = screens.size();
			for (int i = 0; i < ss; i++) {
				Object d = screens.elementAt(i);
				if (d instanceof Pushable) p.addElement(d);
			}
		}
		for (int i = 0; i < p.size(); i++) {
			Pushable pp = (Pushable)p.elementAt(i);
			pp.prepare();
		}
	}
	
	public static void showScreen (int which, EventTable details) {
		synchronized (Midlet.class) {
			popCurrentDialog();
			while (currentScreen != mainMenu && screens.size() > 1) pop(currentScreen);
		}
		switch (which) {
			case MAINSCREEN:
				return;
			case DETAILSCREEN:
				push(new Details(details, null));
				return;
			case INVENTORYSCREEN:
				push(inventory);
				return;
			case ITEMSCREEN:
				push(surroundings);
				return;
			case LOCATIONSCREEN:
				push(zones);
				return;
			case TASKSCREEN:
				push(tasks);
				return;
			case COORDSCREEN:
				push(coordinates);
				return;
		}
	}
}
