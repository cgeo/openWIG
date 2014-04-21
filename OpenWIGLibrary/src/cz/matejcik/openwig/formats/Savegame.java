/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig.formats;

import java.io.*;
import java.util.Hashtable;

import cz.matejcik.openwig.*;
import cz.matejcik.openwig.platform.FileHandle;
import se.krka.kahlua.stdlib.RandomLib;
import se.krka.kahlua.vm.*;

public class Savegame {

	private static final String SIGNATURE = "openWIG savegame - unified format\n";
	private static final int VERSION = 1;
	
	private FileHandle saveFile;

	public Savegame (FileHandle fc) {
		if (fc == null) throw new NullPointerException("savefile must not be null");
		saveFile = fc;
	}

	protected Savegame () {
		/* for test mockups */
	}

	public boolean exists () throws IOException {
		return saveFile.exists();
	}

	protected boolean debug = false;
	protected void debug (String s) { }

	protected Class classForName (String s) throws ClassNotFoundException {
		return Class.forName(s);
	}

	public void store (KahluaTable environment)
	throws IOException {
		DataOutputStream out = null;
		if (saveFile.exists())
			saveFile.truncate(0);
		else
			saveFile.create();
		try {
			Engine.log("STOR: storing game", Engine.LOG_CALL);
			out = saveFile.openDataOutputStream();

			out.writeUTF(SIGNATURE);
			out.writeShort(VERSION);
			resetObjectStore();

			//specialcase cartridge:
			storeValue(Engine.instance.cartridge, out);
			
			serializeRootTable(environment, out);
			Engine.log("STOR: store successful", Engine.LOG_CALL);
		} finally {
			try { out.close(); } catch (Exception e) { }
		}
	}

	protected void resetObjectStore () {
		objectStore = new Hashtable(256);
		// XXX why did i choose to use LuaTable over Hashtable?
		currentId = 0;
		level = 0;
	}

	public void restore (KahluaTable table)
	throws IOException {
		DataInputStream dis = saveFile.openDataInputStream();
		String sig = dis.readUTF();
		if (!SIGNATURE.equals(sig)) throw new IOException("Invalid savegame file: bad signature.");
		try {
			short ver = dis.readShort();
			if (ver != VERSION) throw new IOException("Savegame is for different version.");
		} catch (UTFDataFormatException e) {
			throw new IOException("Savegame is for different version.");
		}

		try {
			resetObjectStore();

			// specialcase cartridge: (TODO make a generic mechanism for this)
			Engine.instance.cartridge = (cz.matejcik.openwig.Cartridge)restoreValue(dis, null);
			
			restoreValue(dis, table);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Problem loading game: "+e.getMessage());
		} finally {
			dis.close();
		}
	}

	private Hashtable objectStore;
	private int currentId;

	private Hashtable idToFuncMap = new Hashtable(128);
	private Hashtable funcToIdMap = new Hashtable(128);
	
	private static StringBuffer path = new StringBuffer();

	public void buildFuncMap (KahluaTable environment) {
		// step 1: walk environment
		KahluaTableIterator it = environment.iterator();
		while (it.advance()) addKeyIfFunc(it.getKey().toString(), it.getValue());
		
		// step 2: walk known packages
		String[] packages = {"string", "math", "coroutine", "os", "table", "Wherigo"};
		for (int i = 0; i < packages.length; i++) {
			KahluaTable kt = (KahluaTable)environment.rawget(packages[i]);
			it = kt.iterator();
			while (it.advance()) {
				String key = (String)it.getKey();
				Object val = it.getValue();
				addKeyIfFunc(packages[i] + "." + key, val);
			}
		}
		
		// step 3: RandomLib hack
		for (int i = 0; i < RandomLib.functions.length; i++) {
			addKeyIfFunc("RandomLib[" + i + "]", RandomLib.functions[i]);
		}
	}
	
	public void walkPrototype (Prototype prototype) {
		walkPrototype("prototype", prototype);
	}
	
	private void walkPrototype (String name, Prototype prototype) {
		addKeyIfFunc(name, prototype);
		for (int i = 0; i < prototype.prototypes.length; i++)
			walkPrototype(name + "[" + i + "]", prototype.prototypes[i]);
	}

	private static final byte LUA_NIL	= 0x00;
	private static final byte LUA_DOUBLE	= 0x01;
	private static final byte LUA_STRING	= 0x02;
	private static final byte LUA_BOOLEAN	= 0x03;
	private static final byte LUA_TABLE	= 0x04;
	private static final byte LUA_CLOSURE	= 0x05;
	private static final byte LUA_OBJECT	= 0x06;
	private static final byte LUA_REFERENCE = 0x07;
	private static final byte LUA_JAVAFUNC	= 0x08;

	private static final byte LUATABLE_PAIR = 0x10;
	private static final byte LUATABLE_END  = 0x11;
	
	private static final byte OW_ACTION	= 0x20;
	private static final byte OW_CARTRIDGE	= 0x21;
	private static final byte OW_MEDIA	= 0x22;
	private static final byte OW_PLAYER	= 0x23;
	private static final byte OW_TASK	= 0x24;
	private static final byte OW_THING	= 0x25;
	private static final byte OW_TIMER	= 0x26;
	private static final byte OW_ZONE	= 0x27;
	private static final byte OW_ZONEPONINT	= 0x28;
	private static final byte OW_OTHER	= 0x2f;
	
	private static final Class[] knownClasses = new Class[] {
		Action.class,
		Cartridge.class,
		Media.class,
		Player.class,
		Task.class,
		Thing.class,
		Timer.class,
		Zone.class,
		ZonePoint.class
	};
	
	private static final Hashtable classmap = new Hashtable(8);
	static {
		for (int i = 0; i < knownClasses.length; i++) {
			classmap.put(knownClasses[i], new Byte((byte)(0x20 + i)));
		}
	}

	private void addKeyIfFunc (String key, Object func) {
		if (func instanceof JavaFunction || func instanceof Prototype) {
			idToFuncMap.put(key, func);
			funcToIdMap.put(func, key);
		}
	}

	private void storeObject (Object obj, DataOutputStream out)
	throws IOException {
		if (obj == null) {
			out.writeByte(LUA_NIL);
			return;
		}
		Integer i = (Integer)objectStore.get(obj);
		if (i != null) {
			out.writeByte(LUA_REFERENCE);
 			if (debug) debug("reference "+i.intValue()+" ("+obj.toString()+")");
			out.writeInt(i.intValue());
		} else {
			i = new Integer(currentId++);
			objectStore.put(obj, i);
			if (debug) debug("(ref"+i.intValue()+")");
			if (obj instanceof OWSerializable) {
				out.writeByte(LUA_OBJECT);
				Byte cl = (Byte)classmap.get(obj.getClass());
				if (cl == null) throw new RuntimeException("Unregistered Serializable: " + obj.getClass().getName());
				out.writeByte(cl.byteValue());
				if (debug) debug("known class: " + obj.getClass().getName() + " (" + obj.toString()+")");
				((OWSerializable)obj).serialize(out);
			} else if (obj instanceof KahluaTable) {
				out.writeByte(LUA_TABLE);
				if (debug) debug("table("+obj.toString()+"):\n");
				serializeLuaTable((KahluaTable)obj, out);
			} else if (obj instanceof LuaClosure) {
				out.writeByte(LUA_CLOSURE);
				if (debug) debug("closure("+obj.toString()+")");
				serializeLuaClosure((LuaClosure)obj, out);
			} else {
				// we're busted
				out.writeByte(LUA_NIL);
				if (debug) debug("UFO");
				Engine.log("STOR: at "+path+": unable to store object of type "+obj.getClass().getName(), Engine.LOG_WARN);
			}
		}
	}

	public void storeValue (Object obj, DataOutputStream out)
	throws IOException {
		if (obj == null) {
			if (debug) debug("nil");
			out.writeByte(LUA_NIL);
		} else if (obj instanceof String) {
			out.writeByte(LUA_STRING);
			if (debug) debug("\""+obj.toString()+"\"");
			out.writeUTF((String)obj);
		} else if (obj instanceof Boolean) {
			if (debug) debug(obj.toString());
			out.writeByte(LUA_BOOLEAN);
			out.writeBoolean(((Boolean)obj).booleanValue());
		} else if (obj instanceof Double) {
			out.writeByte(LUA_DOUBLE);
			if (debug) debug(obj.toString());
			out.writeDouble(((Double)obj).doubleValue());
		} else if (obj instanceof JavaFunction) {
			String func = (String)funcToIdMap.get(obj);
			if (func == null) throw new RuntimeException("Unregistered JavaFunction detected");
			if (debug) debug("javafunc("+func+")-" + obj.toString());
			out.writeByte(LUA_JAVAFUNC);
			out.writeUTF(func);
		} else if (obj instanceof LuaClosure) {
			String func = (String)funcToIdMap.get(obj);
			if (func != null) {
				out.writeByte(LUA_JAVAFUNC);
				out.writeUTF(func);
			} else {
				storeObject(obj, out);
			}
		} else {
			storeObject(obj, out);
		}
	}
	
	private void serializeRootTable (KahluaTable root, DataOutputStream out)
	throws IOException {
		out.writeByte(LUA_TABLE);
		KahluaTableIterator it = root.iterator();
		while (it.advance()) {
			Object key = it.getKey();
			Object value = it.getValue();
			// do not store internals
			if (!(key instanceof String)) continue;
			if ("_G".equals(key)) continue;
			if ("__classmetatables".equals(key)) continue;
			if (value instanceof LuaClosure && "stdlib.lua".equals(((LuaClosure)value).prototype.name)) continue;
			if ("string".equals(key)) continue;
			if ("math".equals(key)) continue;
			if ("table".equals(key)) continue;
			if ("coroutine".equals(key)) continue;
			if ("Wherigo".equals(key)) continue;
			
			out.writeByte(LUATABLE_PAIR);
			storeValue(key, out);
			storeValue(value, out);
		}
		out.writeByte(LUATABLE_END);
	}

	private void serializeLuaTable (KahluaTable table, DataOutputStream out)
	throws IOException {
		level++;
		KahluaTableIterator it = table.iterator();
		int ppos = path.length();
		while (it.advance()) {
			Object value = it.getValue();
			out.writeByte(LUATABLE_PAIR);
			if (debug) for (int i = 0; i < level; i++) debug("  ");
			path.append("."); path.append(it.getKey());

			storeValue(it.getKey(), out);
			if (debug) debug(" : ");
			storeValue(value, out);
			if (debug) debug("\n");
			path.setLength(ppos);
		}
		level--;
		out.writeByte(LUATABLE_END);
	}

	public Object restoreValue (DataInputStream in, Object target)
	throws IOException {
		byte type = in.readByte();
		switch (type) {
			case LUA_NIL:
				if (debug) debug("nil");
				return null;
			case LUA_DOUBLE:
				double d = in.readDouble();
				if (debug) debug(String.valueOf(d));
				return KahluaUtil.toDouble(d);
			case LUA_STRING:
				String s = in.readUTF();
				if (debug) debug("\"" + s + "\"");
				return s;
			case LUA_BOOLEAN:
				boolean b = in.readBoolean();
				if (debug) debug(String.valueOf(b));
				return KahluaUtil.toBoolean(b);
			case LUA_JAVAFUNC:
				String key = in.readUTF();
				Object func = idToFuncMap.get(key);
				if (func == null) throw new RuntimeException("Unable to restore function: " + key);
				if (debug) debug("javafunc("+key+")-"+func);
				return func;
			default:
				return restoreObject(in, type, target);
		}
	}

	private void restCache (Object o) {
		Integer i = new Integer(currentId++);
		objectStore.put(i, o);
		if (debug) debug("(ref"+i.intValue()+")");
	}

	private Object restoreObject (DataInputStream in, byte type, Object target)
	throws IOException {
		switch (type) {
			case LUA_TABLE:
				KahluaTable lti;
				if (target instanceof KahluaTable)
					lti = (KahluaTable)target;
				else
					lti = Engine.platform.newTable();
				restCache(lti);
				if (debug) debug("table:\n");
				return deserializeLuaTable(in, lti);
			case LUA_CLOSURE:
				if (debug) debug("closure: ");
				LuaClosure lc = deserializeLuaClosure(in);
				if (debug) debug(lc.toString());
				return lc;
			case LUA_OBJECT:
				byte cl = in.readByte();
				Class c;
				OWSerializable s = null;
				try {
					c = knownClasses[cl - 0x20];
					if (debug) debug("object "+c.getName()+"...\n");
					s = (OWSerializable)c.newInstance();
				} catch (Throwable e) {
					if (debug) debug("(failed to deserialize "+cl+")\n");
					Engine.log("REST: while trying to deserialize "+cl+":\n"+e.toString(), Engine.LOG_ERROR);
				}
				if (s != null) {
					restCache(s);
					s.deserialize(in);
				}
				return s;
			case LUA_REFERENCE:
				Integer what = new Integer(in.readInt());
				if (debug) debug("reference "+what.intValue());
				Object result = objectStore.get(what);
				if (result == null) {
					Engine.log("REST: not found reference "+what.toString()+" in object store", Engine.LOG_WARN);
					if (debug) debug(" (which happens to be null?)");
					return target;
				} else {
					if (debug) debug(" : "+result.toString());
				}
				return result;
			default:
				Engine.log("REST: found unknown type "+type, Engine.LOG_WARN);
				if (debug) debug("UFO");
				return null;
		}
	}

	int level = 0;

	private KahluaTable deserializeLuaTable (DataInputStream in, KahluaTable table)
	throws IOException {
		level++;
		while (true) {
			byte next = in.readByte();
			if (next == LUATABLE_END) break;
			if (debug) for (int i = 0; i < level; i++) debug("  ");
			Object key = restoreValue(in, null);
			if (debug) debug(" : ");
			Object value = restoreValue(in, table.rawget(key));
			if (debug) debug("\n");
			table.rawset(key, value);
		}
		level--;
		return table;
	}

	private void serializeLuaClosure (LuaClosure closure, DataOutputStream out)
	throws IOException {
		String key = (String)funcToIdMap.get(closure.prototype);
		if (key == null) {
			Engine.log("STOR: at "+path+": unregistered prototype: " + closure.prototype.name, Engine.LOG_ERROR);
			out.writeInt(-1);
			return;
		}
		out.writeUTF(key);
		for (int i = 0; i < closure.upvalues.length; i++) {
			UpValue u = closure.upvalues[i];
			if (u.value == null) {
				Engine.log("STOR: unclosed upvalue in "+closure.toString(), Engine.LOG_WARN);
				u.value = u.coroutine.objectStack[u.index];
			}
			storeValue(u.value, out);
		}
	}

	private LuaClosure deserializeLuaClosure (DataInputStream in)
	throws IOException {
		String key = in.readUTF();
		Prototype proto = (Prototype)idToFuncMap.get(key);
		if (proto == null) throw new RuntimeException("Unregistered prototype in savegame: " + key);
		LuaClosure closure = new LuaClosure(proto, Engine.environment);
		restCache(closure);
		for (int i = 0; i < closure.upvalues.length; i++) {
			UpValue u = new UpValue(Engine.vmThread.currentCoroutine, 1);
			u.value = restoreValue(in, null);
			closure.upvalues[i] = u;
		}
		return closure;
	}
}
