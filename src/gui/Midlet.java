/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import openwig.Engine;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import java.io.*;
import java.util.Vector;

import se.krka.kahlua.stdlib.*;
import se.krka.kahlua.vm.*;
import openwig.Engine;

/**
 * @author matejcik
 */
public class Midlet extends MIDlet implements CommandListener {
	
	public static Coordinates coordinates;
	public static List zones;
	public static Things inventory;
	public static MainMenu mainMenu;
	public static Form mainForm;
	private static StringItem si;
	
	public static Command CMD_EXIT = new Command("Konec", Command.EXIT, 10);
	public static Command CMD_SELECT = new Command("Vybrat", Command.ITEM, 0);
	public static Command CMD_NEXT = new Command("Další", Command.OK, 1);
	public static Command CMD_OK = new Command("OK", Command.OK, 1);
	public static Command CMD_CANCEL = new Command("Zrušit", Command.BACK, 2);
	public static Command CMD_BACK = new Command("Zpìt", Command.BACK, 2);
	
	public static Midlet instance;
	public static Display display;
	
	private static Dialog currentDialog = null;
	private static Vector screens = new Vector();
	
	public void startApp() {
		instance = this;
		display = Display.getDisplay(this);

		mainForm = new Form("splash");
		si = new StringItem(null, "");
		mainForm.append(si);
		mainForm.addCommand(CMD_EXIT);
		mainForm.setCommandListener(this);
		Display.getDisplay(this).setCurrent(mainForm);
		si.setText("Loading LUA...");

		InputStream luacode = getClass().getResourceAsStream("/openwig/luac.out");
		Thread t = new Thread(new Engine(luacode));
		t.start();
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
		Engine.kill();
		notifyDestroyed();
	}

	public void commandAction(Command arg0, Displayable arg1) {
		destroyApp(false);
	}
	
	public static void state (String text) {
		si.setText(text);
		display.setCurrent(mainForm);
	}
	
	synchronized public static void pushDialog(String[] texts) {
		Dialog d = new Dialog(texts);

		//display.flashBacklight(500);
		//display.vibrate(500);
		
		if (currentDialog != null) pop(currentDialog);
		currentDialog = d;
		push(d);
	}
	
	synchronized public static void popDialog(Dialog d) {
		pop(d);
		if (currentDialog == d) currentDialog = null;
	}
	
	synchronized public static void push (Displayable d) {
		if (d instanceof Pushable) ((Pushable)d).prepare();
		screens.addElement(d);
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
			screens.addElement(mainForm);
			ss = 1;
		}
		
		display.setCurrent((Displayable)screens.elementAt(ss-1));
	}
	
	public static void start () {
		mainMenu = new MainMenu();
		coordinates = new Coordinates();
		zones = new Zones();
		inventory = new Things("Inventáø", Engine.instance.player);
		push(mainMenu);
	}
	
	synchronized public static void refresh () {
		int ss = screens.size();
		for (int i = 0; i < ss; i++) {
			Object d = screens.elementAt(i);
			if (d instanceof Pushable) ((Pushable)d).prepare();
		}
	}
}
