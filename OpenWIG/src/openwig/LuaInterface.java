
package openwig;

import gui.Midlet;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.*;

public class LuaInterface implements JavaFunction {
	
	private static final int _REQUIRE = 0;
	private static final int ZONEPOINT = 1;
	private static final int DISTANCE = 2;
	private static final int CARTRIDGE = 3;
	private static final int MESSAGEBOX = 4;
	private static final int ZONE = 5;
	private static final int DIALOG = 6;
	private static final int ZCHARACTER = 7;
	private static final int ZITEM = 8;
	private static final int ZCOMMAND = 9;
	private static final int ZMEDIA = 10;
	private static final int ZINPUT = 11;
	private static final int ZTIMER = 12;
	private static final int ZTASK = 13;
	private static final int AUDIO = 14;
	private static final int GETINPUT = 15;
	private static final int NOCASEEQUALS = 16;
	private static final int SHOWSCREEN = 17;
	private static final int TRANSLATEPOINT = 18;
	private static final int SHOWSTATUSTEXT = 19;
	private static final int VECTORTOPOINT = 20;
	private static final int COMMAND = 21; // Wherigo.Command
	
	private static final int NUM_FUNCTIONS = 22;
	
	private static final String[] names;
	static {
		names = new String[NUM_FUNCTIONS];
		names[_REQUIRE] = "require";
		names[ZONEPOINT] = "ZonePoint";
		names[DISTANCE] = "Distance";
		names[CARTRIDGE] = "ZCartridge";
		names[MESSAGEBOX] = "MessageBox";
		names[ZONE] = "Zone";
		names[DIALOG] = "Dialog";
		names[ZCHARACTER] = "ZCharacter";
		names[ZITEM] = "ZItem";
		names[ZCOMMAND] = "ZCommand";
		names[ZMEDIA] = "ZMedia";
		names[ZINPUT] = "ZInput";
		names[ZTIMER] = "ZTimer";
		names[ZTASK] = "ZTask";
		names[AUDIO] = "PlayAudio";
		names[GETINPUT] = "GetInput";
		names[NOCASEEQUALS] = "NoCaseEquals";
		names[SHOWSCREEN] = "ShowScreen";
		names[TRANSLATEPOINT] = "TranslatePoint";
		names[SHOWSTATUSTEXT] = "ShowStatusText";
		names[VECTORTOPOINT] = "VectorToPoint";
		names[COMMAND] = "Command";
	}

	private int index;
	private static LuaInterface[] functions;
	static {
		functions = new LuaInterface[NUM_FUNCTIONS];
		for (int i = 0; i < NUM_FUNCTIONS; i++) {
			functions[i] = new LuaInterface(i);
		}
	}
	
	public LuaInterface(int index) {
		this.index = index;
	}

	public static void register(LuaState state) {
		LuaTable wig = new LuaTableImpl();
		state.getEnvironment().rawset("Wherigo", wig);
		for (int i = 1; i < NUM_FUNCTIONS; i++) {
			wig.rawset(names[i], functions[i]);
		}
		
		wig.rawset("__index", wig);
		
		wig.rawset("Player", Engine.instance.player);
		wig.rawset("INVALID_ZONEPOINT", null);
		
		// screen constants
		wig.rawset("MAINSCREEN", new Double(Midlet.MAINSCREEN));
		wig.rawset("DETAILSCREEN", new Double(Midlet.DETAILSCREEN));
		wig.rawset("ITEMSCREEN", new Double(Midlet.ITEMSCREEN));
		wig.rawset("INVENTORYSCREEN", new Double(Midlet.INVENTORYSCREEN));
		wig.rawset("LOCATIONSCREEN", new Double(Midlet.LOCATIONSCREEN));
		wig.rawset("TASKSCREEN", new Double(Midlet.TASKSCREEN));
		
		state.getEnvironment().rawset("require", functions[0]);
		
		LuaTable env = new LuaTableImpl();
		env.rawset("Device", "Windows PPC");
		env.rawset("DeviceID", "You don't need to see his ID.");
		env.rawset("Platform", "MIDP-2.0/CLDC-1.1");
		env.rawset("CartFolder", "c:/what/is/it/to/you");
		env.rawset("SyncFolder", "c:/what/is/it/to/you");
		env.rawset("LogFolder", "c:/what/is/it/to/you");
		env.rawset("CartFilename", "cartridge.gwc");
		env.rawset("PathSep", "/"); // no. you may NOT do file i/o on this device.
		env.rawset("Version", "2.11ow");
		env.rawset("Downloaded", new Double(0));
		state.getEnvironment().rawset("Env", env);
		
		Media.reset();
	}

	public String toString() {
		return names[index];
	}
	

	public int call(LuaCallFrame callFrame, int nArguments) {
		switch (index) {
			case _REQUIRE: return requireWherigo(callFrame, nArguments);
			case ZONEPOINT: return zonePoint(callFrame, nArguments);
			case DISTANCE: return distance(callFrame, nArguments);
			case CARTRIDGE: return cartridge(callFrame, nArguments);
			case MESSAGEBOX: return messageBox(callFrame, nArguments);
			case ZONE: return zone(callFrame, nArguments);
			case DIALOG: return dialog(callFrame, nArguments);
			case ZITEM: return item(callFrame, nArguments, false);
			case ZCHARACTER: return item(callFrame, nArguments, true);
			case ZCOMMAND: return command(callFrame, nArguments);
			case ZMEDIA: return media(callFrame, nArguments);
			case ZINPUT:
				EventTable et = new EventTable();
				Engine.instance.cartridge.addObject(et);
				callFrame.push(et);
				return 1;
			case ZTIMER: return timer(callFrame, nArguments);
			case ZTASK: return task(callFrame, nArguments);
			case NOCASEEQUALS: return nocaseequals(callFrame, nArguments);
			case GETINPUT: return getinput(callFrame, nArguments);
			case SHOWSCREEN: return showscreen(callFrame, nArguments);
			case TRANSLATEPOINT: return translatePoint(callFrame, nArguments);
			case AUDIO: return playAudio(callFrame, nArguments);
			case VECTORTOPOINT: return vectorToPoint(callFrame, nArguments);
			case COMMAND:
				return 0;
			default: return 0;
		}
	}
	
	private int requireWherigo (LuaCallFrame callFrame, int nArguments) {
		return 0;
	}
	
	private int cartridge (LuaCallFrame callFrame, int nArguments) {
		Engine.instance.cartridge = new Cartridge();
		callFrame.push(Engine.instance.cartridge);
		return 1;
	}
	
	private int zonePoint (LuaCallFrame callFrame, int nArguments) {
		double a = LuaState.fromDouble(callFrame.get(0));
		double b = LuaState.fromDouble(callFrame.get(1));
		double c = LuaState.fromDouble(callFrame.get(2));
		callFrame.push(new ZonePoint(a,b,c));
		return 1;
	}
	
	private int distance (LuaCallFrame callFrame, int nArguments) {
		double a = LuaState.fromDouble(callFrame.get(0));
		String b = (String)callFrame.get(1);
		callFrame.push(new Distance(a,b));
		return 1;
	}
	
	private int messageBox (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		Engine.message(lt);
		return 0;
	}
	
	private int dialog (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		int n = lt.len();
		String[] texts = new String[n];
		Media[] media = new Media[n];
		for (int i = 1; i <= n; i++) {
			LuaTable item = (LuaTable)lt.rawget(new Double(i));
			texts[i-1] = Engine.removeHtml((String)item.rawget("Text"));
			media[i-1] = (Media)item.rawget("Media");
		}
		Engine.dialog(texts, media);
		return 0;
	}
	
	private int zone (LuaCallFrame callFrame, int nArguments) {
		Object param = callFrame.get(0);
		Zone z = new Zone();
		Engine.instance.cartridge.addObject(z);
		if (param instanceof LuaTable) z.setTable((LuaTable)param);
		callFrame.push(z);
		return 1;
	}
	
	private int media (LuaCallFrame callFrame, int nArguments) {
		Media m = new Media();
		Engine.instance.cartridge.addObject(m);
		callFrame.push(m);
		return 1;
	}
	
	private int timer (LuaCallFrame callFrame, int nArguments) {
		Timer t = new Timer();
		Engine.instance.cartridge.addObject(t);
		callFrame.push(t);
		return 1;
	}
	
	private int item (LuaCallFrame callFrame, int nArguments, boolean character) {
		Object o = callFrame.get(0);
		Cartridge c;
		Container cont = null;
		if (o instanceof Cartridge) {
			c = (Cartridge)o;
		} else if (o instanceof LuaTable) {
			LuaTable lt = (LuaTable)o;
			c = (Cartridge)lt.rawget("Cartridge");
			cont = (Container)lt.rawget("Container");
		} else {
			throw new RuntimeException("unknown constructor format: "+o.getClass().getName());
		}
		Thing i = new Thing(character);
		c.addObject(i);
		if (cont != null) i.moveTo(cont);
		callFrame.push(i);
		return 1;
	}
	
	private int command (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		Action a = new Action(lt);
		Engine.instance.cartridge.addObject(a);
		callFrame.push(a);
		return 1;
	}
	
	private int task (LuaCallFrame callFrame, int nArguments) {
		Task t = new Task();
		Engine.instance.cartridge.addObject(t);
		callFrame.push(t);
		return 1;
	}
	
	private int nocaseequals (LuaCallFrame callFrame, int nArguments) {
		Object a = callFrame.get(0); Object b = callFrame.get(1);
		String aa = a == null ? null : a.toString();
		String bb = b == null ? null : b.toString();
		boolean result = (aa == bb || (aa != null && aa.equalsIgnoreCase(bb)));
		callFrame.push(LuaState.toBoolean(result));
		return 1;
	}
	
	private int getinput (LuaCallFrame callFrame, int nArguments) {
		EventTable lt = (EventTable)callFrame.get(0);
		Engine.input(lt);
		return 1;
	}
	
	private int showscreen (LuaCallFrame callFrame, int nArguments) {
		int screen = (int)LuaState.fromDouble(callFrame.get(0));
		EventTable et = null;
		if (nArguments > 1) {
			Object o = callFrame.get(1);
			if (o instanceof EventTable) et = (EventTable)o;
		}
		Engine.log("CALL: ShowScreen("+screen+") " + (et == null ? "" : et.name));
		Midlet.showScreen(screen, et);
		return 0;
	}
	
	private int translatePoint (LuaCallFrame callFrame, int nArguments) {
		BaseLib.luaAssert(nArguments >= 3, "insufficient arguments for TranslatePoint");
		ZonePoint z = (ZonePoint)callFrame.get(0);
		Distance dist = (Distance)callFrame.get(1);
		double angle = LuaState.fromDouble(callFrame.get(2));
		callFrame.push(z.translate(angle, dist));
		return 1;
	}
	
	private int vectorToPoint (LuaCallFrame callFrame, int nArguments) {
		BaseLib.luaAssert(nArguments >= 2, "insufficient arguments for VectorToPoint");
		ZonePoint a = (ZonePoint)callFrame.get(0);
		ZonePoint b = (ZonePoint)callFrame.get(1);
		double bearing = ZonePoint.angle2azimuth(b.bearing(a.latitude, a.longitude));
		double distance = b.distance(a.latitude, a.longitude);
		callFrame.push(LuaState.toDouble(bearing));
		callFrame.push(LuaState.toDouble(distance));
		return 2;
	}

	private int playAudio (LuaCallFrame callFrame, int nArguments) {
		Media m = (Media)callFrame.get(0);
		m.play();
		return 0;
	}
}
