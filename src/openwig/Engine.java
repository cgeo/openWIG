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
			
			try {
				InputStream is = getClass().getResourceAsStream("/media/code.txt");
				byte[] code = new byte[10];
				int len = is.read(code);
				String c = new String(code, 0, len);
				player.setCompletionCode(c.trim());
			} catch (Exception e) {
				player.setCompletionCode("<nic>");
			}
			
			origPos = new ZonePoint(player.position);
					
			Midlet.start();
			callEvent(cartridge, "OnStart", null);
			
			while (! end ) {
				try { Thread.sleep(1000); } catch (Exception e) {}

				if (Midlet.latitude != player.position.latitude
					|| Midlet.longitude != player.position.longitude
					|| Midlet.altitude != player.position.altitude) {
					
					player.position.latitude = Midlet.latitude;
					player.position.longitude = Midlet.longitude;
					player.position.altitude = Midlet.altitude;
					
					cartridge.walk(player.position);
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
	
	public static void kill () {
		if (instance == null) return;
		synchronized (instance.queue) {
			instance.end = true;
			instance.queue.notify();
		}
	}
		
	public static void message(LuaTable message) {
		String[] texts = {(String)message.rawget("Text")};
		Media[] media = {(Media)message.rawget("Media")};
		String button1 = null, button2 = null;
		LuaTable buttons = (LuaTable)message.rawget("Buttons");
		if (buttons != null) {
			button1 = (String)buttons.rawget(new Double(1));
			button2 = (String)buttons.rawget(new Double(2));
		}
		LuaClosure callback = (LuaClosure)message.rawget("Callback");
		Midlet.pushDialog(texts, media, button1, button2, callback);
	}
	
	public static void dialog(String[] texts, Media[] media) {
		Midlet.pushDialog(texts, media, null, null, null);
	}
	
	public static void input(EventTable input) {
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
	
	public static InputStream mediaFile (Media media) {
		String filename = media.jarFilename();
		return media.getClass().getResourceAsStream("/media/"+filename);
	}
}
