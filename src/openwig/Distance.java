package openwig;

import java.util.Hashtable;
import se.krka.kahlua.vm.*;
import se.krka.kahlua.stdlib.BaseLib;

public class Distance {
	public double value; // value in metres
		
	public static Hashtable conversions = new Hashtable(2);
	static {
		conversions.put("feet", new Double(0.3048));
		conversions.put("ft", new Double(0.3048));
		conversions.put("miles", new Double(1609.344));
		conversions.put("meters", new Double(1));
		conversions.put("kilometers", new Double(1000));
		conversions.put("nauticalmiles", new Double(1852));
	}
	
	private static class Method implements JavaFunction {

		private static final int LEN = 0;
		private static final int INDEX = 1;
		private static final int GETVALUE = 2;
		private int index;

		public Method(int index) {
			this.index = index;
		}

		private int len(LuaCallFrame frame, int n) {
			frame.push(LuaState.toDouble(3));
			return 1;
		}

		private int index(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 2, "not enough parameters");
			Distance z = (Distance) frame.get(0);
			String name = (String) frame.get(1);
			Object ret = null;
			if (name == "value")
				ret = LuaState.toDouble(z.value);
			else if (name == "GetValue")
				ret = Distance.getValueMethod;
			frame.push(ret);
			return 1;
		}

		private int getvalue(LuaCallFrame frame, int n) {
			BaseLib.luaAssert(n >= 2, "not enough parameters");
			Distance z = (Distance) frame.get(0);
			String unit = (String) frame.get(1);
			frame.push(LuaState.toDouble(z.getValue(unit)));
			return 1;
		}

		public int call(LuaCallFrame callFrame, int nArguments) {
			switch (index) {
				case LEN: return len(callFrame, nArguments);
				case INDEX: return index(callFrame, nArguments);
				case GETVALUE: return getvalue(callFrame, nArguments);
				default: return 0;
			}
		}
	}
	protected static LuaTable metatable;
	protected static JavaFunction getValueMethod = new Method(Method.GETVALUE);
	
	public static void register(LuaState state) {
		if (metatable == null) {
			metatable = new LuaTable();
			metatable.rawset("__metatable", "restricted");
			metatable.rawset("__len", new Method(Method.LEN));
			metatable.rawset("__index", new Method(Method.INDEX));
		}
		state.setUserdataMetatable(Distance.class, metatable);
	}	
		
	public Distance (double value, String unit)
	{
		setValue(value, unit);
	}
	
	public void setValue (double value, String unit) {
		if (conversions.containsKey(unit)) {
			this.value = value * ((Double)conversions.get(unit)).doubleValue();
		} else {
			this.value = value;
			// XXX exception
		}
	}
	
	public double getValue (String unit) {
		if (conversions.containsKey(unit)) {
			return value / ((Double)conversions.get(unit)).doubleValue();
		} else {
			return value;
			// XXX exception
		}		
	}
}
