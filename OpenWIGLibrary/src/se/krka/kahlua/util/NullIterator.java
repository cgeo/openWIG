package se.krka.kahlua.util;

import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.LuaCallFrame;

public class NullIterator implements KahluaTableIterator {

	public boolean advance () { return false; }

	public Object getKey () { return null; }

	public Object getValue () { return null; }

	public int call (LuaCallFrame callFrame, int nArguments) { return 0; }

	
	private NullIterator () { }
	
	public static final NullIterator NULL_ITERATOR = new NullIterator();
	
	public static NullIterator getInstance() {
		return NULL_ITERATOR;
	}
}
