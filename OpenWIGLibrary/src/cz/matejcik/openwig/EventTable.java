package cz.matejcik.openwig;

import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.*;

import java.io.*;

public class EventTable implements KahluaTable, Serializable {

	public KahluaTable table = Engine.platform.newTable();

	private KahluaTable metatable = Engine.platform.newTable();

	private static class TostringJavaFunc implements JavaFunction {

		public EventTable parent;

		public TostringJavaFunc (EventTable parent) {
			this.parent = parent;
		}

		public int call (LuaCallFrame callFrame, int nArguments) {
			callFrame.push(parent.luaTostring());
			return 1;
		}
	};

	protected String luaTostring () { return "a ZObject instance"; }

	public EventTable() {
		metatable.rawset("__tostring", new TostringJavaFunc(this));
	}

	public void serialize (DataOutputStream out) throws IOException {
		Engine.instance.savegame.storeValue(table, out);
	}

	public void deserialize (DataInputStream in) throws IOException {
		Engine.instance.savegame.restoreValue(in, this);
		//setTable(table);
	}
	
	public String name, description;
	public ZonePoint position = null;
	protected boolean visible = false;

	public Media media, icon;

	public byte[] getMedia () throws IOException {
		return Engine.mediaFile(media);
	}

	public byte[] getIcon () throws IOException {
		return Engine.mediaFile(icon);
	}

	public boolean isVisible() { return visible; }
	
	public void setPosition(ZonePoint location) {
		position = location;
		table.rawset("ObjectLocation", location);
	}

	public boolean isLocated() {
		return position != null;
	}

	protected void setItem(String key, Object value) {
		if ("Name".equals(key)) {
			name = KahluaUtil.rawTostring(value);
		} else if ("Description".equals(key)) {
			description = Engine.removeHtml(KahluaUtil.rawTostring(value));
		} else if ("Visible".equals(key)) {
			visible = KahluaUtil.boolEval(value);
		} else if ("ObjectLocation".equals(key)) {
			//setPosition(ZonePoint.copy((ZonePoint)value));
			// i know there was need to copy. but why? it is messing up deserialization
			position = (ZonePoint)value;
		} else if ("Media".equals(key)) {
			media = (Media)value;
		} else if ("Icon".equals(key)) {
			icon = (Media)value;
		}
	}

	protected Object getItem (String key) {
		if ("CurrentDistance".equals(key)) {
			if (isLocated()) return KahluaUtil.toDouble(position.distance(Engine.instance.player.position));
			else return KahluaUtil.toDouble(-1);
		} else if ("CurrentBearing".equals(key)) {
			if (isLocated())
				return KahluaUtil.toDouble(ZonePoint.angle2azimuth(position.bearing(Engine.instance.player.position)));
			else return KahluaUtil.toDouble(0);
		} else return table.rawget(key);
	}
	
	public void setTable (KahluaTable table) {
		KahluaTableIterator i = table.iterator();
		while (i.advance()) {
			rawset(i.getKey(), i.getValue());
			//if (n instanceof String) setItem((String)n, val);
		}
	}
	
	public void callEvent(String name, Object param) {
		try {
			Object o = table.rawget(name);
			if (o instanceof LuaClosure) {
				Engine.log("EVNT: " + toString() + "." + name + (param!=null ? " (" + param.toString() + ")" : ""), Engine.LOG_CALL);
				LuaClosure event = (LuaClosure) o;
				Engine.vmThread.call(event, this, param, null);
				Engine.log("EEND: " + toString() + "." + name, Engine.LOG_CALL);
			}
		} catch (Throwable t) {
			Engine.stacktrace(t);
		}
	}
	
	public boolean hasEvent(String name) {
		return (table.rawget(name)) instanceof LuaClosure;
	}
	
	public String toString()  {
		return (name == null ? "(unnamed)" : name);
	}

	public void rawset(Object key, Object value) {
		// TODO unify rawset/setItem
		if (key instanceof String) {
			setItem((String) key, value);
		}
		table.rawset(key, value);
		Engine.log("PROP: " + toString() + "." + key + " is set to " + (value == null ? "nil" : value.toString()), Engine.LOG_PROP);
	}

	public void setMetatable (KahluaTable metatable) { }

	public KahluaTable getMetatable () { return metatable; }

	public Object rawget (Object key) {
		// TODO unify rawget/getItem
		if (key instanceof String)
			return getItem((String)key);
		else
			return table.rawget(key);
	}

	public int len () { return table.len(); }
	
	public void rawset (int key, Object value) { /* NOPE */ }

	public Object rawget (int key) { /* NOPE */ return null; }

	public KahluaTableIterator iterator () { return table.iterator(); }

	public boolean isEmpty () { return table.isEmpty(); }

	public void wipe () { table.wipe(); }

}
