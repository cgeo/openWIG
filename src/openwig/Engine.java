package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;

import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;

import util.*;

public class Engine implements Runnable {
	
	private Vector queue = new Vector();
	public Cartridge cartridge;
	public Player player = new Player();
	
	private InputStream code;
	
	public static Engine instance;
	public static LuaState state;
	
	private boolean end = false;

	public Engine (InputStream code) {
		instance = this;
		this.code = code;
	}
	
	public void run() {
		state = new LuaState(System.out);
		
		BaseLib.register(state);
		MathLib.register(state);
		StringLib.register(state);
		CoroutineLib.register(state);

		LuaInterface.register(state);
		
		try {
			InputStream stdlib = getClass().getResourceAsStream("/openwig/stdlib.lbc");
			LuaClosure closure = LuaPrototype.loadByteCode(stdlib, state.environment);
			state.call(closure, null, null, null);
			stdlib.close(); stdlib = null;
			
			closure = LuaPrototype.loadByteCode(code, state.environment);
			state.call(closure, null, null, null);
			code.close(); code = null;
			closure = null;
			
			player.position = (ZonePoint)cartridge.table.rawget("StartingLocation");
			
			Midlet.state("yay for us!!");
			
			/*Coordinates c = new Coordinates();
			Midlet.push(c);*/
			
			Midlet.start();
			cartridge.callEvent("OnStart", null);
			
			while (! end ) {
				synchronized (queue) {
					if (queue.isEmpty()) {
						try { queue.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
						continue;
					}

					player.position = (ZonePoint)queue.elementAt(0);
					queue.removeElementAt(0);
				}
				cartridge.newPosition(player.position);
			} 
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			Midlet.state("Runtime Exception... "+e.toString());
			System.out.println(state.currentThread.stackTrace);
		} catch (IOException e) {
			Midlet.state("IOException... "+e.getMessage());
		}
	}

	public static void newPosition (ZonePoint z) {
		synchronized (instance.queue) {
			instance.queue.addElement(z);
			instance.queue.notify();
		}
	}
	
	public static void kill () {
		synchronized (instance.queue) {
			instance.end = true;
			instance.queue.notify();
		}
	}
		
	public static void message(String text) {
		String[] texts = {text};
		dialog(texts);
	}
	
	public static void dialog(String[] texts) {
		Midlet.pushDialog(texts);
	}
	
	public static void callEvent(EventTable subject, String name, Object param) {
		EventCaller ec = new EventCaller(subject, name, param);
		ec.start();
	}
}
