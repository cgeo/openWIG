/*
Copyright (c) 2009 jan matejek <ja@matejcik.cz>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package se.krka.kahlua.vm;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Enumeration;

import se.krka.kahlua.stdlib.BaseLib;

public final class LuaHashtableImpl implements LuaTable {

	private boolean weakValues = false;
	// TODO implement weakKeys
	private Hashtable hashtable;
	private boolean dirty = true;
	private Hashtable enumeration;
	private Object firstkey = new Object() { };

	public LuaHashtableImpl () {
		hashtable = new Hashtable();
		enumeration = new Hashtable();
	}

	private final Object unref (Object o) {
		if (!canBeWeakObject(o)) {
			return o;
		}

		// Assertion: o instanceof WeakReference
		return ((WeakReference)o).get();
	}

	private final Object ref (Object o) {
		if (!canBeWeakObject(o)) {
			return o;
		}

		return new WeakReference(o);
	}

	private boolean canBeWeakObject (Object o) {
		return !(o == null || o instanceof String || o instanceof Double || o instanceof Boolean);
	}

	private LuaTable metatable;

	public final void rawset (Object key, Object value) {
		checkKey(key);
		if (value != null && weakValues) value = ref(value);
		if (value == null) hashtable.remove(key);
		else hashtable.put(key, value);
		dirty = true;
	}

	public final Object rawget (Object key) {
		checkKey(key);
		if (key instanceof Double) {
			BaseLib.luaAssert(!((Double)key).isNaN(), "table index is NaN");
		}
		Object value = hashtable.get(key);
		if (value != null && weakValues) {	
			value = unref(value);
			if (value == null) hashtable.remove(key);
		}
		return value;
	}

	public static void checkKey (Object key) {
		BaseLib.luaAssert(key != null, "table index is nil");
	}

	public final Object next (Object key) {
		if (dirty) rebuildEnumeration();
		if (key == null) key = firstkey;
		return enumeration.get(key);
	}

	private final void rebuildEnumeration() {
		enumeration.clear();
		Enumeration e = hashtable.keys();
		Object key = firstkey;
		while (e.hasMoreElements()) {
			Object nextkey = e.nextElement();
			enumeration.put(key, nextkey);
		}
	}

	public final int len () {
		int high = hashtable.size();
		int low = 0;
		while (low < high) {
			int middle = (high + low + 1) >> 1;
			Object value = rawget(LuaState.toDouble(middle));
			if (value == null) {
				high = middle - 1;
			} else {
				low = middle;
			}
		}
		return low;
	}

	private void updateWeakSettings (boolean k, boolean v) {
		// weak keys are ignored
		if (v != weakValues) {
			weakValues = v;
			Enumeration keys = hashtable.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = hashtable.get(key);
				if (weakValues) value = ref(value);
				else value = unref(value);
				hashtable.put(key, value);
			}
		}
	}

	public LuaTable getMetatable () {
		return metatable;
	}

	public void setMetatable (LuaTable metatable) {
		this.metatable = metatable;
		boolean weakKeys = false, weakValues = false;
		if (metatable != null) {
			Object modeObj = metatable.rawget(BaseLib.MODE_KEY);
			if (modeObj != null && modeObj instanceof String) {
				String mode = (String)modeObj;
				weakKeys = (mode.indexOf('k') >= 0);
				weakValues = (mode.indexOf('v') >= 0);
			}
		}
		updateWeakSettings(weakKeys, weakValues);
	}
}
