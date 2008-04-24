package openwig;

import se.krka.kahlua.vm.*;

public class Media extends EventTable {
	
	private static int media_no = 1;

	public static void register (LuaState state) {
		EventTable.register(state);
		state.setUserdataMetatable(Media.class, metatable);
	}
	
	public int id;
	public String altText = null;
	public String type = null;
	
	public Media() {
		id = media_no++;
	}
	
	protected void setItem (String key, Object value) {	
		if (key == "AltText") {
			altText = (String)value;
		} else if (key == "Resources") {
			LuaTable lt = (LuaTable)value;
			int n = lt.len();
			for (int i = 1; i <= n; i++) {
				LuaTable res = (LuaTable)lt.rawget(new Double(i));
				type = (String)res.rawget("Type");
			}
		} else super.setItem(key, value);
	}
	
	public String jarFilename () {
		return String.valueOf(id)+"."+(type==null ? "" : type);
	}
}
