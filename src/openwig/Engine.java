package openwig;

import gui.Midlet;
import gwc.CartridgeFile;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.*;

import java.io.*;
import java.util.Calendar;

public class Engine implements Runnable {

	public Cartridge cartridge;
	public Player player = new Player();
	
	private String codeUrl;
	private CartridgeFile gwcfile;
	private PrintStream log;
	
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
	
	public Engine (CartridgeFile cf, OutputStream out) {
		instance = this;
		gwcfile = cf;
		if (out!=null) log = new PrintStream(out);
	}	
	
	public void run() {
		try {
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
			
			PrintStream l = log; log = null; // prevent logging while loading
			closure = LuaPrototype.loadByteCode(new ByteArrayInputStream(lbc), state.environment);
			state.call(closure, null, null, null);
			lbc = null;
			closure = null;
			
			player.setprop("CompletionCode", gwcfile.code.intern());
			player.setprop("Name", gwcfile.member.intern());
			log = l;
					
			Midlet.start();

		} catch (Exception e) {
			Midlet.end();
			stacktrace(e);
		}
		
		if (log!=null) log.println("-------------------\ncartridge "+cartridge.toString()+" started\n-------------------");
		player.refreshLocation();
		callEvent(cartridge, "OnStart", null);
			
		while (!end) {
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
			if (!end) break;

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
		log.close();
		} catch (Throwable t) {
			Engine.stacktrace(t);
		} finally {
			instance = null;
			state = null;
		}
	}
	
	public static void stacktrace (Throwable e) {
		e.printStackTrace();
		System.out.println(state.currentThread.stackTrace);
		String msg = e.toString()+"\n\nstack trace: " + state.currentThread.stackTrace;
		log(msg);
		Midlet.error(msg);
	}
	
	public static void kill () {
		Timer.kill();
		if (instance == null) return;
		instance.end = true;
	}
		
	public static void message(LuaTable message) {
		String[] texts = {removeHtml((String)message.rawget("Text"))};
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
		log("BTTN: "+value.toString()+" pressed");
		CallbackCaller cc = new CallbackCaller(callback, value);
		cc.start();
	}
	
	public static ZonePoint diff;
	
	public static byte[] mediaFile (Media media) throws Exception {
		/*String filename = media.jarFilename();
		return media.getClass().getResourceAsStream("/media/"+filename);*/
		return instance.gwcfile.getFile(media.id);
	}
	
	public static void log(String s) {
		if (instance.log == null) return;
		Calendar now = Calendar.getInstance();
		instance.log.print(now.get(Calendar.HOUR_OF_DAY));
		instance.log.print(':');
		instance.log.print(now.get(Calendar.MINUTE));
		instance.log.print(':');
		instance.log.print(now.get(Calendar.SECOND));
		instance.log.print('|');
		instance.log.print((int)(Midlet.gps.getLatitude() * 10000 + 0.5) / 10000.0);
		instance.log.print('|');
		instance.log.print((int)(Midlet.gps.getLongitude() * 10000 + 0.5) / 10000.0);
		instance.log.print('|');
		instance.log.print(Midlet.gps.getAltitude());
		instance.log.print('|');
		instance.log.print(Midlet.gps.getPrecision());
		instance.log.print("|:: ");
		instance.log.println(s);
		instance.log.flush();
	}
	
	public static String removeHtml (String s) {
		if (s == null) return null;
		StringBuffer sb = new StringBuffer(s.length());
		int pos = 0;
		while (pos < s.length()) {
			int np = s.indexOf("<BR>", pos);
			if (np == -1) break;
			sb.append(s.substring(pos, np));
			pos = np + 4;
		}
		sb.append(s.substring(pos));
		s = sb.toString(); pos = 0; sb.delete(0, sb.length());
		while (pos < s.length()) {
			int np = s.indexOf("&nbsp;", pos);
			if (np == -1) break;
			sb.append(s.substring(pos, np));
			sb.append(' ');
			pos = np + 6;
		}
		sb.append(s.substring(pos));
		return sb.toString();
	}
}
