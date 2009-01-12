package openwig;

import java.io.ByteArrayInputStream;
import javax.microedition.media.PlayerListener;
import se.krka.kahlua.vm.*;

public class Media extends EventTable implements PlayerListener {
	
	private static int media_no;

	public static void register (LuaState state) {
		media_no = 1;
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
		if ("AltText".equals(key)) {
			altText = (String)value;
		} else if ("Resources".equals(key)) {
			LuaTable lt = (LuaTable)value;
			int n = lt.len();
			for (int i = 1; i <= n; i++) {
				LuaTable res = (LuaTable)lt.rawget(new Double(i));
				String t = (String)res.rawget("Type");
				if ("fdl".equals(t)) continue;
				type = t.toLowerCase().intern();
			}
		} else super.setItem(key, value);
	}
	
	public String jarFilename () {
		return String.valueOf(id)+"."+(type==null ? "" : type);
	}
	
	public void play () {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(Engine.mediaFile(this));
			String mime = null;
			if ("wav".equals(type)) mime = "audio/x-wav";
			else if ("mp3".equals(type)) mime = "audio/mpeg";
			javax.microedition.media.Player p = javax.microedition.media.Manager.createPlayer(bis,mime);
			p.addPlayerListener(this);
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playerUpdate (javax.microedition.media.Player player, String event, Object data) {
		if (event == PlayerListener.END_OF_MEDIA ||
			event == PlayerListener.ERROR || event == PlayerListener.STOPPED) {
			player.close();
		}
	}
}
