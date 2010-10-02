package saveanalyzer;

import cz.matejcik.openwig.Timer;
import cz.matejcik.openwig.j2se.J2SEFileHandle;
import java.io.*;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTableImpl;

public class Main {

	private static class CartridgeFile extends cz.matejcik.openwig.formats.CartridgeFile {
		public CartridgeFile (AnalyzingSavegame s) {
			savegame = s;
		}
	}

	private static class Engine extends cz.matejcik.openwig.Engine {
		public Engine (AnalyzingSavegame s) throws IOException {
			super(new CartridgeFile(s), null);
			instance = this;
			state = new LuaState();
		}
	}

	public static void main (String[] args) {
		try {
			Engine e = new Engine(new AnalyzingSavegame(new J2SEFileHandle(new File(args[0]))));
			LuaTableImpl t = new LuaTableImpl();
			e.savegame.restore(t);
			System.out.println("restore proceeded");
		} catch (Exception e) {
			System.out.println("failed:");
			e.printStackTrace();
		} finally {
			Timer.kill();
		}
	}
}
