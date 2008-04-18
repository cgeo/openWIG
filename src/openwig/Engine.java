package openwig;

import gui.Midlet;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;

import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;

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
			
			player.setCompletionCode("<nic>");
			player.position = (ZonePoint)cartridge.table.rawget("StartingLocation");
			if (player.position == null) {
				player.position = new ZonePoint(Midlet.latitude, Midlet.longitude, Midlet.altitude);
			} else {
				Midlet.latitude = player.position.latitude + 0.0001;
				Midlet.longitude = player.position.longitude;
				Midlet.altitude = player.position.altitude;
			}
			origPos = new ZonePoint(player.position);
					
			Midlet.start();
			cartridge.callEvent("OnStart", null);
			
			while (! end ) {
				try { Thread.sleep(1000); } catch (Exception e) {}
/*				synchronized (queue) {
					if (queue.isEmpty()) {
						try { queue.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
						continue;
					}

					player.position = (ZonePoint)queue.elementAt(0);
					queue.removeElementAt(0);
				}*/
				if (Midlet.latitude != player.position.latitude
					|| Midlet.longitude != player.position.longitude
					|| Midlet.altitude != player.position.altitude) {
					
					player.position.latitude = Midlet.latitude;
					player.position.longitude = Midlet.longitude;
					player.position.altitude = Midlet.altitude;
					
					cartridge.newPosition(player.position);
				} else {
					cartridge.tick();
				}
			} 
			
		} catch (Exception e) {
			stacktrace(e);
		}
	}
	
	public static void stacktrace (Exception e) {
		e.printStackTrace();
		System.out.println(state.currentThread.stackTrace);
		Midlet.error(e.toString()+"\n\nstack trace: " + state.currentThread.stackTrace);
	}

	public static void newPosition (ZonePoint z) {
		synchronized (instance.queue) {
			instance.queue.addElement(z);
			instance.queue.notify();
		}
	}
	
	public static void kill () {
		if (instance == null) return;
		synchronized (instance.queue) {
			instance.end = true;
			instance.queue.notify();
		}
	}
		
	public static void message(LuaTable message) {
		String[] texts = {(String)message.rawget("Text")};
		String button1 = null, button2 = null;
		LuaTable buttons = (LuaTable)message.rawget("Buttons");
		if (buttons != null) {
			button1 = (String)buttons.rawget(new Double(1));
			button2 = (String)buttons.rawget(new Double(2));
		}
		LuaClosure callback = (LuaClosure)message.rawget("Callback");
		Midlet.pushDialog(texts, button1, button2, callback);
	}
	
	public static void dialog(String[] texts) {
		Midlet.pushDialog(texts, null, null, null);
	}
	
	public static void input(LuaTable input) {
		Midlet.pushInput(input);
	}
	
	public static void callEvent(EventTable subject, String name, Object param) {
		EventCaller ec = new EventCaller(subject, name, param);
		ec.start();
	}
	
	public static void invokeCallback(LuaClosure callback, Object value) {
		CallbackCaller cc = new CallbackCaller(callback, value);
		cc.start();
	}
	
	private static ZonePoint origPos;
	public static ZonePoint diff;
	public static boolean shifted = false;
	
	public static void reposition(double lat, double lon, double alt) {
		if (origPos == null) return;
		if (shifted) return;
		shifted = true;
		diff = new ZonePoint(origPos.latitude - lat, origPos.longitude - lon, 0);
		instance.cartridge.reposition(diff);
	}
}
