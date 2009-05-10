package openwig;

import gui.Midlet;
import gwc.CartridgeFile;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.TableLib;

import java.io.*;
import java.util.*;

interface Caller {
	void call();
}


class EventCaller implements Caller {
	private EventTable target = null;
	private String event;
	private Object param;
	
	public EventCaller (EventTable target, String event, Object param) {
		this.target = target;
		this.event = event;
		this.param = param;
	}
	
	public void call() {
		target.callEvent(event, param);
	}
	
}

class CallbackCaller implements Caller {
	private LuaClosure callback;
	private Object value;
	
	public CallbackCaller (LuaClosure callback, Object value) {
		this.callback = callback;
		this.value = value;
	}
	
	public void call () {
		Engine.state.call(callback, value, null, null);
	}
}

public class Engine implements Runnable {

	public Cartridge cartridge;
	public Player player = new Player();

	private String codeUrl;
	private CartridgeFile gwcfile;
	private PrintStream log;
	
	private static Vector eventQueue;

	public static Engine instance;
	public static LuaState state;

	private boolean end = false;

	// **** utility for displaying status on loading screen
	private StringBuffer stdout = new StringBuffer("Creating engine...\n");
	private void write (String s) {
		stdout.append(s);
		Midlet.engineOutput.setText(stdout.toString());
	}

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
		if (out != null) log = new PrintStream(out);
	}

	public void run () {
		try {
			write("Creating state...\n");
			state = new LuaState(System.out);

			/*		write("Registering base libs...\n");
			BaseLib.register(state);
			MathLib.register(state);
			StringLib.register(state);
			CoroutineLib.register(state);
			OsLib.register(state);*/

			try {
				write("Loading stdlib...");
				InputStream stdlib = getClass().getResourceAsStream("/openwig/stdlib.lbc");
				LuaClosure closure = LuaPrototype.loadByteCode(stdlib, state.getEnvironment());
				write("calling...\n");
				state.call(closure, null, null, null);
				stdlib.close(); stdlib = null;

				write("Registering WIG libs...\n");
				LuaInterface.register(state);
				
				write("Building event queue...\n");
				eventQueue = new Vector();

				write("Loading gwc...");
				if (gwcfile == null) gwcfile = CartridgeFile.read(codeUrl);
				if (gwcfile == null) throw new Exception("invalid cartridge file");
				write("loading code...");
				byte[] lbc = gwcfile.getBytecode();

				PrintStream l = log; log = null; // prevent logging while loading
				write("parsing...");
				closure = LuaPrototype.loadByteCode(new ByteArrayInputStream(lbc), state.getEnvironment());
				write("calling...\n");
				state.call(closure, null, null, null);
				lbc = null;
				closure = null;

				write("Setting remaining properties...\n");
				player.rawset("CompletionCode", gwcfile.code.intern());
				player.rawset("Name", gwcfile.member.intern());
				log = l;

				write("Starting game...\n");
				Midlet.start();

			} catch (Exception e) {
				Midlet.end();
				stacktrace(e);
			}

			if (log != null) log.println("-------------------\ncartridge " + cartridge.toString() + " started\n-------------------");
			player.refreshLocation();
			cartridge.callEvent("OnStart", null);
			Midlet.refresh();

			long lastTick = System.currentTimeMillis();
			while (!end) {
				boolean events = false;
				while (!eventQueue.isEmpty()) {
					Caller c = (Caller)eventQueue.firstElement();
					eventQueue.removeElementAt(0);
					try {
						c.call();
						events = true;
					} catch (Throwable t) {
						stacktrace(t);
					}
				}
				if (events) Midlet.refresh();

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
				
				synchronized (this) {
					if (!eventQueue.isEmpty()) continue;
					
					long stm = System.currentTimeMillis();
					long sleep = lastTick + 1000 - stm;
					if (sleep < 1) {
						lastTick += 1000;
						continue;
					}
					try {
						Thread.sleep(sleep);
						lastTick += 1000;
					} catch (InterruptedException e) { }
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
		String msg = e.toString() + "\n\nstack trace: " + state.currentThread.stackTrace;
		log(msg);
		Midlet.error(msg);
	}

	public static void kill () {
		if (instance == null) return;
		instance.end = true;
	}

	public static void message (LuaTable message) {
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

	public static void dialog (String[] texts, Media[] media) {
		Midlet.pushDialog(texts, media, null, null, null);
	}

	public static void input (EventTable input) {
		Midlet.pushInput(input);
	}

	public static void callEvent (EventTable subject, String name, Object param) {
		if (!subject.hasEvent(name)) return;
		EventCaller ec = new EventCaller(subject, name, param);
		
		synchronized (Engine.instance) {
			eventQueue.addElement(ec);
			Engine.instance.notify();
		}
	}

	public static void invokeCallback (LuaClosure callback, Object value) {
		log("BTTN: " + value.toString() + " pressed");
		CallbackCaller cc = new CallbackCaller(callback, value);
		synchronized (Engine.instance) {
			eventQueue.addElement(cc);
			Engine.instance.notify();
		}
	}

	public static byte[] mediaFile (Media media) throws Exception {
		/*String filename = media.jarFilename();
		return media.getClass().getResourceAsStream("/media/"+filename);*/
		return instance.gwcfile.getFile(media.id);
	}

	public static void log (String s) {
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

	public static void tableInsert (LuaTable table, int position, Object item) {
		TableLib.insert(state, table, position, item);
	}
	public static void tableInsert (LuaTable table, Object item) {
		TableLib.insert(state, table, item);
	}
	public static Object tableRemove (LuaTable table, int position) {
		return TableLib.remove(state, table, position);
	}
	public static Object tableRemove (LuaTable table) {
		return TableLib.remove(state, table);
	}
}
