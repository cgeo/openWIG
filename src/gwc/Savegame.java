package gwc;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import javax.microedition.io.file.FileConnection;
import openwig.LuaInterface;
import se.krka.kahlua.vm.LuaTable;

public class Savegame {

	private static final String SIGNATURE = "openWIG savegame\n";
	
	private FileConnection saveFile;

	public Savegame (FileConnection fc) {
		saveFile = fc;
	}

	public void store (LuaTable table)
	throws IOException {
		saveFile.truncate(0);
		DataOutputStream out = saveFile.openDataOutputStream();

		out.writeUTF(SIGNATURE);
		LuaInterface.serializeLuaTable(table, out);
	}

	public void restore (LuaTable table)
	throws IOException {
		DataInputStream dis = saveFile.openDataInputStream();
		String sig = dis.readUTF();
		if (!SIGNATURE.equals(sig)) {
			throw new IOException("Invalid savegame file: bad signature.");
		}
		try {
			LuaInterface.deserializeLuaTable(dis, table);
		} catch (IOException e) {
			throw new IOException("Problem loading game: "+e.getMessage());
		}
	}
}
