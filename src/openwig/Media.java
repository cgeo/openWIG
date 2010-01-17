package openwig;

import java.io.*;
import java.util.Vector;
import javax.microedition.media.PlayerListener;
import se.krka.kahlua.vm.*;

public class Media extends EventTable implements PlayerListener {
	
	private static int media_no;

	private static class Resource {
		public String filename;
		public String type;
		public int id;

		public Resource (String filename, String type) {
			this.filename = filename;
			this.type = type;
			this.id = media_no++;
		}

		public Resource (LuaTable source) {
			type = ((String)source.rawget("Type")).toLowerCase();
			filename = (String)source.rawget("Filename");
			this.id = media_no++;
			System.out.println("id " + id + " is " + filename);
		}

		public Resource (int id) {
			this.id = id;
		}

		public byte[] getData () throws IOException {
			return Engine.instance.gwcfile.getFile(id);
		}
	}

	private Vector resources = new Vector();
	private Resource preferred = null;

	public static void reset () {
		media_no = 1;
	}
	
	public String altText = null;

	private boolean findPreferred () throws IOException {
		if (preferred != null) return true;
		for (int i = 0; i < resources.size(); i++) {
			Resource r = (Resource)resources.elementAt(i);
			if (Engine.instance.gwcfile.isPresent(r.id)) {
				preferred = r;
				return true;
			}
		}
		return false;
	}

	public void serialize (DataOutputStream out) throws IOException {
		if (findPreferred()) out.writeInt(preferred.id);
		else out.writeInt(0);
		super.serialize(out);
	}

	public void deserialize (DataInputStream in) throws IOException {
		media_no--; // deserialize must be called directly after construction
		int id = in.readInt();
		if (id >= media_no) media_no = id + 1;
		preferred = new Resource(id);
		super.deserialize(in);
	}

	protected String luaTostring () { return "a ZMedia instance"; }
	
	protected void setItem (String key, Object value) {	
		if ("AltText".equals(key)) {
			altText = (String)value;
		} else if ("Resources".equals(key)) {
			LuaTable lt = (LuaTable)value;
			int n = lt.len();
			for (int i = 1; i <= n; i++) {
				resources.addElement(new Resource((LuaTable)lt.rawget(new Double(i))));
			}
		} else super.setItem(key, value);
	}
	
	public void play () {
		try {
			if (!findPreferred()) return;
		} catch (IOException e) { return; }
		javax.microedition.media.Player p = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(preferred.getData());
			String mime = null;
			if ("wav".equals(preferred.type)) mime = "audio/x-wav";
			else if ("mp3".equals(preferred.type)) mime = "audio/mpeg";
			p = javax.microedition.media.Manager.createPlayer(bis,mime);
			p.addPlayerListener(this);
			p.start();
		} catch (Exception e) {
			if (p != null) p.close();
			e.printStackTrace();
		}
	}

	public byte[] getData () {
		try {
			if (findPreferred()) return preferred.getData();
		} finally {
			return null;
		}
	}

	public void playerUpdate (javax.microedition.media.Player player, String event, Object data) {
		if (event == PlayerListener.END_OF_MEDIA ||
			event == PlayerListener.ERROR || event == PlayerListener.STOPPED) {
			player.close();
		}
	}
}
