package cz.matejcik.openwig.testmockups;

import cz.matejcik.openwig.*;

public class TestEngine extends Engine {

	public static void initialize () {
		Engine.instance = new TestEngine();
		Engine.instance.savegame = new TestSavegame();
	}

	public static void kill () {
		Engine.instance.savegame = null;
		Engine.instance = null;
	}
}
