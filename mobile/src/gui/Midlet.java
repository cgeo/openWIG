package gui;

import cz.matejcik.openwig.platform.LocationService;
import gps.NMEAParser;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.io.OutputStream;
import javax.microedition.media.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import se.krka.kahlua.vm.*;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.WherigoLib;
import cz.matejcik.openwig.platform.UI;
import java.io.ByteArrayInputStream;
import util.Config;

public class Midlet extends MIDlet implements CommandListener, UI, PlayerListener {

	public static final String VERSION = "386";
	
	// basemenu screens
	public static Coordinates coordinates;
	public static Options options;
	public static Browser browser;
	public static List baseMenu;
	public static Navigation navigation; // not mainmenu, but used by browser
	public static CartridgeDetails cartridgeDetails;
	
	private static final int MNU_START = 0;
	private static final int MNU_GPS = 1;
	private static final int MNU_OPTIONS = 2;
	private static final int MNU_END = 3;	
	
	// main in-game menu screens
	public static MainMenu mainMenu;
	public static Zones zones;
	public static Things inventory, surroundings;
	public static Tasks tasks;
	
	// detail screens
	public static Actions actions;
	public static Details details;
	public static Targets targets;
	
	// cancellable screens
	public static Dialog dialog;
	public static Input input;

	// save blockers
	private Alert savingAlert = new Alert("Saving", "Saving, please wait...", null, AlertType.INFO);
	private Gauge savingGauge = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
	
	public static StringItem engineOutput = null;
	
	public static StringBuffer backlog = new StringBuffer();
	
	public static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 10);
	public static final Command CMD_CANCEL = new Command("Cancel", Command.BACK, 2);
	public static final Command CMD_BACK = new Command("Back", Command.BACK, 9);

	public static final Image NULL_IMAGE = Image.createImage(1, 1);
	
	public static Midlet instance = null;
	public static Display display;
	public static Config config;
	
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

	public static String err = "";
	public void startApp() {
		try {
		err = "getting display";
		display = Display.getDisplay(this);
		if (instance == null) {
			WherigoLib.env.put(WherigoLib.DEVICE_ID, System.getProperty("microedition.platform"));
			
			instance = this;
			err = "config";
			config = new Config("_configuration");

			err = "basemenu";
			baseMenu = new List("OpenWIG r"+VERSION, List.IMPLICIT);
			baseMenu.append("Start", null);
			baseMenu.append("GPS", null);
			baseMenu.append("Options", null);
			baseMenu.append("Quit", null);
			baseMenu.setCommandListener(this);

			err = "save alert";
			savingAlert.setIndicator(savingGauge);
			savingAlert.setTimeout(999999999);

			err = "coordinates";
			coordinates = new Coordinates();
			err = "options";
			options = new Options();
			err = "browser";
			browser = new Browser();

			err = "navigation";
			navigation = new Navigation();
			err = "details";
			cartridgeDetails = new CartridgeDetails();

			err = "resetting GPS";
			resetGps();

			err = "almost done";
			push(baseMenu);
		}
		} catch (Throwable t) {
			Alert a = new Alert("error r"+VERSION, t.toString() + " ("+t.getMessage()+") at "+ err,null,AlertType.ERROR);
			a.setTimeout(Alert.FOREVER);
			display.setCurrent(a);
		}
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
		Engine.kill();
		instance = null;
		notifyDestroyed();
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == baseMenu) {
			if (cmd == List.SELECT_COMMAND) {
				switch (baseMenu.getSelectedIndex()) {
					case MNU_START:
						push(browser);
						break;
						
					case MNU_GPS:
						push(coordinates.reset(baseMenu));
						break;
						
					case MNU_OPTIONS:
						push(options.reset());
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

	private static Ticker ticker = new Ticker("");
	private static boolean showTicker = false;
	public void setStatusText (String text) {
		if (text != null) {
			ticker.setString(text);
			showTicker = true;
			currentDisplay.setTicker(ticker);
		} else {
			showTicker = false;
			currentDisplay.setTicker(null);
		}
	}
	
	public static void error (String text) {
		Alert a = new Alert("error",text,null,AlertType.ERROR);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a, currentDisplay);
	}

	public void showError (String text) {
		error(text);
	}
	
	synchronized public void pushDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		dialog.reset(texts, media, button1, button2, callback, parent);
		push(dialog);

//		display.flashBacklight(500);
//		display.vibrate(500);
	}
	
	synchronized public void pushInput(EventTable table) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		input.reset(table, parent);
		input.push();
		//push(input);
	}

	public static void push (Displayable d) {
		if (d instanceof Pushable) ((Pushable)d).push();
		else show(d);
	}
	
	public static void show (Displayable d) {
		synchronized (instance) {
			if (showTicker) d.setTicker(ticker);
			else d.setTicker(null);
			currentDisplay = d;
			display.setCurrent(d);
		}
	}
	
	/////////////////
	//
	//   ????

	public static void resetGps () {
		if (gps != null) gps.disconnect();
		gpsType = config.getInt(Config.GPS_TYPE);
		switch (gpsType) {
			case GPS_MANUAL:
				err = "manual gps";
				gps = coordinates;
				break;
			case GPS_SERIAL:
				err = "serial port";
				gps = new NMEAParser("comm:"+config.get(Config.GPS_SERIAL_PORT)+";baudrate=9600");
				break;
			case GPS_BLUETOOTH:
				err = "bluetooth";
				gps = new NMEAParser(config.get(Config.GPS_BT_URL));
				break;
			case GPS_SOCKET:
				err = "socket";
				gps = new NMEAParser("socket://localhost:"+config.get(Config.GPS_TCP_PORT));
				break;
			case GPS_INTERNAL: try {
				err = "internal";
				Class internal = Class.forName("gps.InternalProvider");
				gps = (LocationService)internal.newInstance();
				} catch (Throwable e) { throw new RuntimeException(e.toString() + " ("+e.getMessage()+")"); }
				break;
			default:
				gpsType = GPS_MANUAL;
				gps = coordinates;
		}
	}

	private static void makesplash () {
		Form f = new Form("splash");
		f.append(engineOutput = new StringItem(null, "Creating engine..."));
		f.addCommand(CMD_EXIT);
		f.setCommandListener(instance);
		show(f);
	}
	
	public static void loadCartridge (CartridgeFile cf, OutputStream log) {
		try {
			makesplash();
			Engine.newInstance(cf, log, instance, gps).start();
		} catch (Throwable t) {
			error(t.toString());
		}
	}

	public static void restoreCartridge (CartridgeFile cf, OutputStream log) {
		try {
			makesplash();
			Engine.newInstance(cf, log, instance, gps).restore();
		} catch (Throwable t) {
			error(t.toString());
		}
	}
	
	public void start () {
		mainMenu = new MainMenu();
		
		zones = new Zones();
		inventory = new Things("Inventory", Things.INVENTORY);
		surroundings = new Things("You see", Things.SURROUNDINGS);
		tasks = new Tasks();
		
		// ordering is important! some of those set parents in constructor
		details = new Details();
		actions = new Actions();
		targets = new Targets();
		
		dialog = new Dialog();
		input = new Input();
		
		push(mainMenu);
	}
	
	public void end () {
		Engine.kill();
		mainMenu = null;
		zones = null; inventory = null; surroundings = null; tasks = null;
		actions = null; details = null; dialog = null; input = null; targets = null;
		
		push(baseMenu);
	}
	
	public static Displayable currentDisplay;
	synchronized public void refresh () {
		if (currentDisplay instanceof Pushable && ! (currentDisplay instanceof Cancellable))
			((Pushable)currentDisplay).push();
	}
	
	synchronized public void showScreen (int which, EventTable param) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		switch (which) {
			case UI.MAINSCREEN:
				push(mainMenu);
				return;
			case UI.DETAILSCREEN:
				// TODO maybe check whether the screen is not a child of the one pushed?
				push(details.reset(param, parent));
				return;
			case UI.INVENTORYSCREEN:
				push(inventory);
				return;
			case UI.ITEMSCREEN:
				push(surroundings);
				return;
			case UI.LOCATIONSCREEN:
				push(zones);
				return;
			case UI.TASKSCREEN:
				push(tasks);
				return;
/*			case UI.COORDSCREEN:
				push(coordinates.reset(parent));
				return;*/
		}
	}

	private StringBuffer stdout = new StringBuffer();
	public void debugMsg (String msg) {
		stdout.append(msg);
		engineOutput.setText(stdout.toString());
	}

	public void playSound (byte[] data, String mime) {
		javax.microedition.media.Player p = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			p = javax.microedition.media.Manager.createPlayer(bis,mime);
			p.addPlayerListener(this);
			p.start();
		} catch (Exception e) {
			if (p != null) p.close();
			e.printStackTrace();
		}
	}

	public void playerUpdate (Player player, String event, Object eventData) {
		if (event == PlayerListener.END_OF_MEDIA ||
			event == PlayerListener.ERROR || event == PlayerListener.STOPPED) {
			player.close();
		}
	}

	public void blockForSaving () {
		display.setCurrent(savingAlert);
	}

	public void unblock () {
		display.setCurrent(currentDisplay);
	}
}
