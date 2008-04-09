
package openwig;

import se.krka.kahlua.vm.*;

public class LuaInterface implements JavaFunction {
	
	private static final int _REQUIRE = 0;
	private static final int ZONEPOINT = 1;
	private static final int DISTANCE = 2;
	private static final int CARTRIDGE = 3;
	private static final int MESSAGEBOX = 4;
	private static final int ZONE = 5;
	private static final int DIALOG = 6;
	private static final int CHARACTER = 7;
	private static final int ITEM = 8;
	private static final int COMMAND = 9;
	private static final int MEDIA = 10;
	private static final int INPUT = 11;
	private static final int TIMER = 12;
	private static final int TASK = 13;
	private static final int AUDIO = 14;
	private static final int GETINPUT = 15;
	
	private static final int NUM_FUNCTIONS = 16;
	
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
		names[CHARACTER] = "ZCharacter";
		names[ITEM] = "ZItem";
		names[COMMAND] = "ZCommand";
		names[MEDIA] = "ZMedia";
		names[INPUT] = "ZInput";
		names[TIMER] = "ZTimer";
		names[TASK] = "ZTask";
		names[AUDIO] = "PlayAudio";
		names[GETINPUT] = "GetInput";
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
		LuaTable wig = new LuaTable();
		state.environment.rawset("Wherigo", wig);
		for (int i = 1; i < NUM_FUNCTIONS; i++) {
			wig.rawset(names[i], functions[i]);
		}
		
		wig.rawset("__index", wig);
		
		wig.rawset("Player", Engine.instance.player);
		wig.rawset("INVALID_ZONEPOINT", null);
		
		state.environment.rawset("require", functions[0]);
		
		Zone.register(state);
		Cartridge.register(state);
		Thing.register(state);
		Action.register(state);
		Player.register(state);
		Task.register(state);
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
			case ITEM: return item(callFrame, nArguments, false);
			case CHARACTER: return item(callFrame, nArguments, true);
			case COMMAND: return command(callFrame, nArguments);
			case MEDIA: case INPUT: case TIMER:
				callFrame.push(new LuaTable());
				return 1;
			case TASK: return task(callFrame, nArguments);
			default: return 0;
		}
	}
	
	private int requireWherigo (LuaCallFrame callFrame, int nArguments) {
		String what = (String)callFrame.get(0);
		if ("Wherigo".equals(what)) System.out.println("works");
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
		LuaTable lt = new LuaTable();
		double a = LuaState.fromDouble(callFrame.get(0));
		String b = (String)callFrame.get(1);
		lt.rawset("dist", new Double(a));
		lt.rawset("unit", b.intern());
		callFrame.push(lt);
		return 1;
	}
	
	private int messageBox (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		Engine.message((String)lt.rawget("Text"));
		return 0;
	}
	
	private int dialog (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		int n = lt.len();
		String[] texts = new String[n];
		for (int i = 1; i <= n; i++) {
			LuaTable item = (LuaTable)lt.rawget(new Double(i));
			texts[i-1] = (String)item.rawget("Text");
		}
		Engine.dialog(texts);
		return 0;
	}
	
	private int zone (LuaCallFrame callFrame, int nArguments) {
		Cartridge c = (Cartridge)callFrame.get(0);
		Zone z = new Zone();
		c.zones.addElement(z);
		callFrame.push(z);
		return 1;
	}
	
	private int item (LuaCallFrame callFrame, int nArguments, boolean character) {
		Object o = callFrame.get(0);
		Cartridge c;
		Container cont = null;
		if (o instanceof LuaTable) {
			LuaTable lt = (LuaTable)o;
			c = (Cartridge)lt.rawget("Cartridge");
			cont = (Container)lt.rawget("Container");
		} else if (o instanceof Cartridge) {
			c = (Cartridge)o;
		} else {
			throw new RuntimeException("unknown constructor format: "+o.getClass().getName());
		}
		Thing i = new Thing(character);
		c.things.addElement(i);
		if (cont != null) i.moveTo(cont);
		callFrame.push(i);
		return 1;
	}
	
	private int command (LuaCallFrame callFrame, int nArguments) {
		LuaTable lt = (LuaTable)callFrame.get(0);
		callFrame.push(new Action(lt));
		return 1;
	}
	
	private int task (LuaCallFrame callFrame, int nArguments) {
		Cartridge c = (Cartridge)callFrame.get(0);
		Task t = new Task();
		c.tasks.addElement(t);
		callFrame.push(t);
		return 1;
	}
}
