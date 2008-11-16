package openwig;

import gui.Midlet;
import gwc.CartridgeFile;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;

import java.io.*;

public class Engine implements Runnable {

	public Cartridge cartridge;
	public Player player = new Player();
	
	private String codeUrl;
	private CartridgeFile gwcfile;
	
	public static Engine instance;
	public static LuaState state;
	
	private boolean end = false;

	public Engine (String codeUrl) {
		instance = this;
		this.codeUrl = codeUrl;
	}
	
	public Engine (CartridgeFile cf) {
		instance = this;
		gwcfile = cf;
	}
	
	public void run() {
		state = new LuaState(System.out);
		
		BaseLib.register(state);
		MathLib.register(state);
		StringLib.register(state);
		CoroutineLib.register(state);
		OsLib.register(state);

		LuaInterface.register(state);
		
		try {
			InputStream stdlib = getClass().getResourceAsStream("/openwig/stdlib.lbc");
			LuaClosure closure = LuaPrototype.loadByteCode(stdlib, state.environment);
			state.call(closure, null, null, null);
			stdlib.close(); stdlib = null;
			
			if (gwcfile == null) gwcfile = CartridgeFile.read(codeUrl);
			if (gwcfile == null) throw new Exception("invalid cartridge file");
			byte[] lbc = gwcfile.getBytecode();
			
			closure = LuaPrototype.loadByteCode(new ByteArrayInputStream(lbc), state.environment);
			state.call(closure, null, null, null);
			lbc = null;
			closure = null;
			
			player.setCompletionCode(gwcfile.code);
					
			Midlet.start();

		} catch (Exception e) {
			Midlet.end();
			stacktrace(e);
		}
		
		player.refreshLocation();
		callEvent(cartridge, "OnStart", null);
			
		while (!end) {
			try { Thread.sleep(1000); } catch (Exception e) { }

			try {
				if (Midlet.gps.getLatitude() != player.position.latitude
					|| Midlet.gps.getLongitude() != player.position.longitude
					|| Midlet.gps.getAltitude() != player.position.altitude.value) {
					player.refreshLocation();
				}
				cartridge.tick();
			} catch (Exception e) {
				stacktrace(e);
			}
		} 
	}
	
	public static void stacktrace (Exception e) {
		e.printStackTrace();
		System.out.println(state.currentThread.stackTrace);
		Midlet.error(e.toString()+"\n\nstack trace: " + state.currentThread.stackTrace);
	}
	
	public static void kill () {
		Timer.kill();
		if (instance == null) return;
		instance.end = true;
		instance = null;
		state = null;
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
		if (!subject.hasEvent(name)) return;
		EventCaller ec = new EventCaller(subject, name, param);
		ec.start();
	}
	
	public static void invokeCallback(LuaClosure callback, Object value) {
		CallbackCaller cc = new CallbackCaller(callback, value);
		cc.start();
	}
	
	public static ZonePoint diff;
	
	public static byte[] mediaFile (Media media) throws Exception {
		/*String filename = media.jarFilename();
		return media.getClass().getResourceAsStream("/media/"+filename);*/
		return instance.gwcfile.getFile(media.id);
	}
}
