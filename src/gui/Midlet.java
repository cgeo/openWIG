package gui;

import openwig.Engine;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;

import java.io.*;
import java.util.Vector;

import javax.microedition.io.file.FileConnection;
import se.krka.kahlua.stdlib.*;
import se.krka.kahlua.vm.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;

public class Midlet extends MIDlet implements CommandListener {
	
	public static Coordinates coordinates;
	public static Zones zones;
	public static Things inventory, surroundings;
	public static Tasks tasks;
	public static MainMenu mainMenu;
	
	public static TextBox filename;
	private static String sourceUrl = "resource:/openwig/cartridge.gwc";
	
	private static List baseMenu = new List("menu", List.IMPLICIT);
	private static final int MNU_START = 0;
	private static final int MNU_GPS = 1;
	private static final int MNU_FILE = 2;
	private static final int MNU_END = 3;
	
	public static final int MAINSCREEN = 0;
	public static final int DETAILSCREEN = 1;
	public static final int INVENTORYSCREEN = 2;
	public static final int ITEMSCREEN = 3;
	public static final int LOCATIONSCREEN = 4;
	public static final int TASKSCREEN = 5;	
	
	public static final Command CMD_EXIT = new Command("Konec", Command.EXIT, 10);
	public static final Command CMD_SELECT = new Command("Vybrat", Command.ITEM, 0);
	public static final Command CMD_CANCEL = new Command("Zrušit", Command.BACK, 2);
	public static final Command CMD_BACK = new Command("Zpìt", Command.BACK, 2);
	
	public static Midlet instance;
	public static Display display;
	
	private static Cancellable currentDialog = null;
	private static Vector screens = new Vector();
	private static Displayable currentScreen = null;
	
	public static Displayable getCurrentScreen () { return currentScreen; }
	
	public static double latitude, longitude, altitude;
	public static double heading = 0;
	
	/////////////////////////////////////
	//
	//    Midlet maintenance
	
	public void startApp() {
		instance = this;
		display = Display.getDisplay(this);
		
		baseMenu.append("Start", null);
		baseMenu.append("GPS", null);
		baseMenu.append("Select file", null);
		baseMenu.append("Konec", null);
		baseMenu.setSelectCommand(CMD_SELECT);
		baseMenu.setCommandListener(this);
		
		coordinates = new Coordinates();
		filename = new TextBox("enter full resource path", "", 1000, TextField.URL);
		filename.setCommandListener(this);
		filename.addCommand(new Command("OK", Command.SCREEN, 1));
		filename.addCommand(CMD_BACK);
		
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
						Form f = new Form("splash");
						f.append(new StringItem(null, "Loading LUA..."));
						f.addCommand(CMD_EXIT);
						f.setCommandListener(this);
						Display.getDisplay(this).setCurrent(f);

						Thread t = new Thread(new Engine(sourceUrl));
						t.start();
						break;
						
					case MNU_GPS:
						push(coordinates);
						break;
						
					case MNU_FILE:
						filename.setString(sourceUrl);
						push(filename);
						break;
						
					case MNU_END:
						destroyApp(false);
						break;
				}
			}
		} else if (disp == filename) {
			if (cmd.getCommandType() == Command.SCREEN) {
				sourceUrl = filename.getString();
			}
			pop(filename);
		} else if (cmd.getCommandType() == Command.EXIT) {
			destroyApp(false);
		}
	}
	
	////////////////////////////////////////////
	//
	//   functions for other components to use
	
	synchronized public static void error (String text) {
		Alert a = new Alert("chyba",text,null,AlertType.ERROR);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a, display.getCurrent());
	}

	private static FileConnection lastCon;
	public static InputStream connect (String url) throws Exception {
		if (url.startsWith("resource:")) {
			return Midlet.class.getResourceAsStream(url.substring(9));
		} else if (url.startsWith("file:")) {
			if (lastCon == null || !lastCon.getURL().equals(url)) {
				lastCon = (FileConnection)Connector.open(url, Connector.READ);
			}
			return lastCon.openInputStream();
		} else return null;
	}
	
	synchronized public static void pushDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
		Dialog d = new Dialog(texts, media, button1, button2, callback);

//		display.flashBacklight(500);
//		display.vibrate(500);
		
		if (currentDialog != null) popDialog(currentDialog);
		currentDialog = d;
		push(d);
	}
	
	synchronized public static void pushInput(EventTable input) {
		Input i = new Input(input);
		if (currentDialog != null) popDialog(currentDialog);
		currentDialog = i;
		push(i);
	}
	
	synchronized public static void popDialog(Cancellable d) {
		d.cancel();
		pop((Displayable)d);
		if (currentDialog == d) currentDialog = null;
	}
	
	synchronized public static void push (Displayable d) {
		screens.addElement(d);
		currentScreen = d;
		if (d instanceof Pushable) ((Pushable)d).prepare();
		display.setCurrent(d);
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
		//if (currentScreen instanceof Pushable) ((Pushable)currentScreen).prepare();
		display.setCurrent(currentScreen);
	}
	
	/////////////////
	//
	//   ????
	
	public static void start () {
		mainMenu = new MainMenu();
		zones = new Zones();
		inventory = new Things("Inventáø", Things.INVENTORY);
		surroundings = new Things("Okolí", Things.SURROUNDINGS);
		tasks = new Tasks();
		//Engine.reposition(gpsParser.getLatitude(), gpsParser.getLongitude(), gpsParser.getAltitude());
		push(mainMenu);
	}
	
	synchronized public static void refresh () {
		int ss = screens.size();
		for (int i = 0; i < ss; i++) {
			Object d = screens.elementAt(i);
			if (d instanceof Pushable) {
				((Pushable)d).prepare();
				if (screens.size() < ss) { // suppose the prepare() just popped the screen
					// also suppose that nothing worse happened ;e)
					ss = screens.size();
					i--;
				}
			}
		}
	}
	
	synchronized public static void showScreen (int which, EventTable details) {
		if (currentDialog != null) popDialog(currentDialog);
		while (currentScreen != mainMenu) pop(currentScreen);
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
		}
	}
}
