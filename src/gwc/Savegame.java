package gwc;

import java.io.*;
import java.util.Hashtable;
import javax.microedition.io.file.FileConnection;

import openwig.Engine;
import openwig.Serializable;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaPrototype;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.UpValue;

public class Savegame {

	private static final String SIGNATURE = "openWIG savegame\n";
	
	private FileConnection saveFile;

	public Savegame (FileConnection fc) {
		if (fc == null) throw new NullPointerException("savefile must not be null");
		saveFile = fc;
	}

	public boolean exists () throws IOException {
		return saveFile.exists();
	}

	public void store (LuaTable table)
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
			resetObjectStore();
			//serializeLuaTable(table, out);
			storeValue(table, out);
			Engine.log("STOR: store successful", Engine.LOG_CALL);
		} finally {
			try { out.close(); } catch (Exception e) { }
		}
	}

	private void resetObjectStore () {
		objectStore = new LuaTableImpl(256);
		// XXX why did i choose to use LuaTable over Hashtable?
		currentId = 0;
		level = 0;
	}

	public void restore (LuaTable table)
	throws IOException {
		DataInputStream dis = saveFile.openDataInputStream();
		String sig = dis.readUTF();
		if (!SIGNATURE.equals(sig)) {
			throw new IOException("Invalid savegame file: bad signature.");
		}
		try {
			resetObjectStore();
			//deserializeLuaTable(dis, table);
			restoreValue(dis, table);
		} catch (IOException e) {
			throw new IOException("Problem loading game: "+e.getMessage());
		} finally {
			dis.close();
		}
	}

	private LuaTable objectStore;
	private int currentId;

	private Hashtable idToJavafuncMap = new Hashtable(128);
	private Hashtable javafuncToIdMap = new Hashtable(128);
	private int currentJavafunc = 0;

	public void buildJavafuncMap (LuaTable environment) {
		LuaTable[] packages = new LuaTable[] {
			environment,
			(LuaTable)environment.rawget("string"),
			(LuaTable)environment.rawget("math"),
			(LuaTable)environment.rawget("coroutine"),
			(LuaTable)environment.rawget("os"),
			(LuaTable)environment.rawget("table")
		};
		for (int i = 0; i < packages.length; i++) {
			LuaTable table = packages[i];
			Object next = null;
			while ((next = table.next(next)) != null) {
				Object jf = table.rawget(next);
				if (jf instanceof JavaFunction) addJavafunc((JavaFunction)jf);
			}
		}
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

	public void addJavafunc (JavaFunction javafunc) {
		Integer id = new Integer(currentJavafunc++);
		idToJavafuncMap.put(id, javafunc);
		javafuncToIdMap.put(javafunc, id);
	}

	private int findJavafuncId (JavaFunction javafunc) {
		Integer id = (Integer)javafuncToIdMap.get(javafunc);
		if (id != null) return id.intValue();
		else throw new RuntimeException("javafunc not found in map!");
	}

	private JavaFunction findJavafuncObject (int id) {
		JavaFunction jf = (JavaFunction)idToJavafuncMap.get(new Integer(id));
		return jf;
	}

	private void storeObject (Object obj, DataOutputStream out)
	throws IOException {
		if (obj == null) {
			out.writeByte(LUA_NIL);
			return;
		}
		Double i = (Double)objectStore.rawget(obj);
		if (i != null) {
			out.writeByte(LUA_REFERENCE);
			//#ifdef debugSavegame
			System.out.print("reference "+i.intValue()+" ("+obj.toString()+")");
			//#endif
			out.writeInt(i.intValue());
		} else {
			i = new Double(currentId++);
			objectStore.rawset(obj, i);
			//#ifdef debugSavegame
			System.out.print("(ref"+i.intValue()+")");
			//#endif
			if (obj instanceof Serializable) {
				out.writeByte(LUA_OBJECT);
				out.writeUTF(obj.getClass().getName());
				//#ifdef debugSavegame
				System.out.print(obj.getClass().getName() + " (" + obj.toString()+")");
				//#endif
				((Serializable)obj).serialize(out);
			} else if (obj instanceof LuaTable) {
				out.writeByte(LUA_TABLE);
				//#ifdef debugSavegame
				System.out.print("table("+obj.toString()+"):\n");
				//#endif
				serializeLuaTable((LuaTable)obj, out);
			} else if (obj instanceof LuaClosure) {
				out.writeByte(LUA_CLOSURE);
				//#ifdef debugSavegame
				System.out.print("closure("+obj.toString()+")");
				//#endif
				serializeLuaClosure((LuaClosure)obj, out);
			} else {
				// we're busted
				out.writeByte(LUA_NIL);
				//#ifdef debugSavegame
				System.out.print("UFO");
				//#endif
				Engine.log("STOR: unable to store object of type "+obj.getClass().getName(), Engine.LOG_WARN);
			}
		}
	}

	public void storeValue (Object obj, DataOutputStream out)
	throws IOException {
		if (obj == null) {
			//#ifdef debugSavegame
			System.out.print("nil");
			//#endif
			out.writeByte(LUA_NIL);
		} else if (obj instanceof String) {
			out.writeByte(LUA_STRING);
			//#ifdef debugSavegame
			System.out.print("\""+obj.toString()+"\"");
			//#endif
			out.writeUTF((String)obj);
		} else if (obj instanceof Boolean) {
			//#ifdef debugSavegame
			System.out.print(obj.toString());
			//#endif
			out.writeByte(LUA_BOOLEAN);
			out.writeBoolean(((Boolean)obj).booleanValue());
		} else if (obj instanceof Double) {
			out.writeByte(LUA_DOUBLE);
			//#ifdef debugSavegame
			System.out.print(obj.toString());
			//#endif
			out.writeDouble(((Double)obj).doubleValue());
		} else if (obj instanceof JavaFunction) {
			int i = findJavafuncId((JavaFunction)obj);
			//#ifdef debugSavegame
			System.out.print("javafunc("+i+")-"+obj.toString());
			//#endif
			out.writeByte(LUA_JAVAFUNC);
			out.writeInt(i);
		} else {
			storeObject(obj, out);
		}
	}

	public void serializeLuaTable (LuaTable table, DataOutputStream out)
	throws IOException {
		level++;
		Object next = null;
		while ((next = table.next(next)) != null) {
			Object value = table.rawget(next);
			out.writeByte(LUATABLE_PAIR);
			//#ifdef debugSavegame
			for (int i = 0; i < level; i++) System.out.print("  ");
			//#endif

			storeValue(next, out);
			//#ifdef debugSavegame
			System.out.print(" : ");
			//#endif
			storeValue(value, out);
			//#ifdef debugSavegame
			System.out.println();
			//#endif
		}
		level--;
		out.writeByte(LUATABLE_END);
	}

	public Object restoreValue (DataInputStream in, Object target)
	throws IOException {
		byte type = in.readByte();
		switch (type) {
			case LUA_NIL:
				//#ifdef debugSavegame
				System.out.print("nil");
				//#endif
				return null;
			case LUA_DOUBLE:
				double d = in.readDouble();
				//#ifdef debugSavegame
				System.out.print(d);
				//#endif
				return LuaState.toDouble(d);
			case LUA_STRING:
				String s = in.readUTF();
				//#ifdef debugSavegame
				System.out.print("\"" + s + "\"");
				//#endif
				return s;
			case LUA_BOOLEAN:
				boolean b = in.readBoolean();
				//#ifdef debugSavegame
				System.out.print(b);
				//#endif
				return LuaState.toBoolean(b);
			case LUA_JAVAFUNC:
				int i = in.readInt();
				JavaFunction jf = findJavafuncObject(i);
				//#ifdef debugSavegame
				System.out.print("javafunc("+i+")-"+jf);
				//#endif
				return jf;
			default:
				return restoreObject(in, type, target);
		}
	}

	private void restCache (Object o) {
		Double i = new Double(currentId++);
		objectStore.rawset(i, o);
		//#ifdef debugSavegame
		System.out.print("(ref"+i.intValue()+")");
		//#endif
	}

	private Object restoreObject (DataInputStream in, byte type, Object target)
	throws IOException {
		switch (type) {
			case LUA_TABLE:
				LuaTable lti;
				if (target instanceof LuaTable)
					lti = (LuaTable)target;
				else
					lti = new LuaTableImpl();
				restCache(lti);
				//#ifdef debugSavegame
				System.out.print("table:\n");
				//#endif
				return deserializeLuaTable(in, lti);
			case LUA_CLOSURE:
				LuaClosure lc = deserializeLuaClosure(in);
				//#ifdef debugSavegame
				System.out.print("closure: "+lc.toString());
				//#endif
				return lc;
			case LUA_OBJECT:
				String cls = in.readUTF();
				Serializable s = null;
				try {
					//#ifdef debugSavegame
					System.out.print("object of type "+cls+"\n");
					//#endif
					Class c = Class.forName(cls);
					if (Serializable.class.isAssignableFrom(c)) {
						s = (Serializable)c.newInstance();
					}
				} catch (Throwable e) {
					Engine.log("REST: while trying to deserialize "+cls+":\n"+e.toString(), Engine.LOG_ERROR);
				}
				if (s != null) {
					restCache(s);
					s.deserialize(in);
				}
				return s;
			case LUA_REFERENCE:
				Double what = new Double(in.readInt());
				//#ifdef debugSavegame
				System.out.print("reference "+what.intValue());
				//#endif
				Object result = objectStore.rawget(what);
				if (result == null) {
					Engine.log("REST: not found reference "+what.toString()+" in object store", Engine.LOG_WARN);
					//#ifdef debugSavegame
					System.out.print(" (which happens to be null?)");
					//#endif
					return target;
				} else {
					//#ifdef debugSavegame
					System.out.print(" : "+result.toString());
					//#endif
				}
				return result;
			default:
				Engine.log("REST: found unknown type "+type, Engine.LOG_WARN);
				//#ifdef debugSavegame
				System.out.print("UFO");
				//#endif
				return null;
		}
	}

	int level = 0;

	public LuaTable deserializeLuaTable (DataInputStream in, LuaTable table)
	throws IOException {
		level++;
		while (true) {
			byte next = in.readByte();
			if (next == LUATABLE_END) break;
			//#ifdef debugSavegame
			for (int i = 0; i < level; i++) System.out.print("  ");
			//#endif
			Object key = restoreValue(in, null);
			//#ifdef debugSavegame
			System.out.print(" : ");
			//#endif
			Object value = restoreValue(in, table.rawget(key));
			//#ifdef debugSavegame
			System.out.println();
			//#endif
			table.rawset(key, value);
		}
		level--;
		return table;
	}

	private void serializeLuaClosure (LuaClosure closure, DataOutputStream out)
	throws IOException {
		closure.prototype.dump(out);
		for (int i = 0; i < closure.upvalues.length; i++) {
			UpValue u = closure.upvalues[i];
			if (u.value == null) {
				Engine.log("STOR: unclosed upvalue in "+closure.toString(), Engine.LOG_WARN);
				u.value = u.thread.objectStack[u.index];
			}
			storeValue(u.value, out);
		}
	}

	private LuaClosure deserializeLuaClosure (DataInputStream in)
	throws IOException {
		LuaClosure closure = LuaPrototype.loadByteCode(in, Engine.state.getEnvironment());
		restCache(closure);
		for (int i = 0; i < closure.upvalues.length; i++) {
			UpValue u = new UpValue();
			u.value = restoreValue(in, null);
			closure.upvalues[i] = u;
		}
		return closure;
	}
}
