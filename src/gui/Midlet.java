package gui;

import gps.InternalProviderRedirector;
import gps.LocationService;
import gps.NMEAParser;
import gwc.CartridgeFile;
import java.io.OutputStream;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import se.krka.kahlua.vm.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;
import util.Config;

public class Midlet extends MIDlet implements CommandListener {
	
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
	
	public static StringItem engineOutput = null;
	
	public static StringBuffer backlog = new StringBuffer();
	
	// showScreen codes
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
			instance = this;
			err = "config";
			config = new Config("_configuration");

			err = "basemenu";
			baseMenu = new List("menu", List.IMPLICIT);
			baseMenu.append("Start", null);
			baseMenu.append("GPS", null);
			baseMenu.append("Options", null);
			baseMenu.append("Quit", null);
			baseMenu.setSelectCommand(CMD_SELECT);
			baseMenu.setCommandListener(this);

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
			Alert a = new Alert("error",t.toString() + " ("+t.getMessage()+") at "+ err,null,AlertType.ERROR);
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
			if (cmd == CMD_SELECT) {
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
	
	public static void error (String text) {
		Alert a = new Alert("error",text,null,AlertType.ERROR);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a, currentDisplay);
	}
	
	synchronized public static void pushDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		dialog.reset(texts, media, button1, button2, callback, parent);
		push(dialog);

//		display.flashBacklight(500);
//		display.vibrate(500);
	}
	
	synchronized public static void pushInput(EventTable table) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		input.reset(table, parent);
		push(input);
	}
	
	public static void push (Displayable d) {
		if (d instanceof Pushable) ((Pushable)d).push();
		else show(d);
	}
	
	synchronized public static void show (Displayable d) {
		currentDisplay = d;
		display.setCurrent(d);
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
		f.append(engineOutput = new StringItem(null, "Creating engine..."));
		f.addCommand(CMD_EXIT);
		f.setCommandListener(instance);
		show(f);

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
		
		// ordering is important! some of those set parents in constructor
		details = new Details();
		actions = new Actions();
		targets = new Targets();
		
		dialog = new Dialog();
		input = new Input();
		
		push(mainMenu);
	}
	
	public static void end () {
		Engine.kill();
		mainMenu = null;
		zones = null; inventory = null; surroundings = null; tasks = null;
		actions = null; details = null; dialog = null; input = null; targets = null;
		
		push(baseMenu);
	}
	
	public static Displayable currentDisplay;
	synchronized public static void refresh () {
		/*mainMenu.refresh();
		zones.refresh();
		inventory.refresh();
		surroundings.refresh();
		tasks.refresh();
		actions.refresh();
		details.refresh();
		targets.refresh();*/
		if (currentDisplay instanceof Pushable && ! (currentDisplay instanceof Cancellable))
			((Pushable)currentDisplay).push();
	}
	
	synchronized public static void showScreen (int which, EventTable param) {
		Displayable parent = currentDisplay;
		if (parent instanceof Cancellable) {
			parent = ((Cancellable)parent).cancel();
		}
		switch (which) {
			case MAINSCREEN:
				push(mainMenu);
				return;
			case DETAILSCREEN:
				// TODO maybe check whether the screen is not a child of the one pushed?
				push(details.reset(param, parent));
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
				push(coordinates.reset(parent));
				return;
		}
	}
}
