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

import util.*;

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
	
	public static void dialog(String[] texts) {
		Dialog d = new Dialog();
		
		//display.flashBacklight(500);
		//display.vibrate(500);
		
		synchronized (Midlet.class) {
			if (currentDialog != null) {
				currentDialog.kill();
				pop();
			}
			currentDialog = d;
			push(d);
		}
		d.run(texts);
		synchronized (Midlet.class) {
			if (currentDialog == d) {
				currentDialog = null;
				pop();
			}
		}
	}
	
	synchronized public static void push (Displayable d) {
		if (d instanceof Pushable) ((Pushable)d).prepare();
		screens.addElement(d);
		display.setCurrent(d);
	}
	
	synchronized public static void pop () {
		int ss = screens.size();
		if (ss > 1) {
			Displayable d = (Displayable)screens.elementAt(ss-2);
			display.setCurrent(d);
			screens.removeElementAt(ss-1);
		} else {
			// XXX ??
			screens.removeAllElements();
			screens.addElement(mainForm);
			// XXX
			display.setCurrent(mainForm);
		}
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
