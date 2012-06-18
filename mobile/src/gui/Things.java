package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Thing;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;

public class Things extends ListOfStuff {
	
	public static final int INVENTORY = 0;
	public static final int SURROUNDINGS = 1;
	private int mode;
	
	private static Image compass;
	static {
		try {
			compass = Image.createImage(Things.class.getResourceAsStream("/icons/compass.png"));
		} catch (Exception e) { }
	}
	
	public Things (String title, int mode) {
		super(title);
		this.mode = mode;
	}
	
	public boolean isPresent (Thing t) {
		return stuff.contains(t);
	}

	protected void callStuff(Object what) {
		Thing t = (Thing)what;
		if (t.hasEvent("OnClick")) {
			Engine.callEvent(t, "OnClick", null);
		} else {
			Midlet.push(Midlet.details.reset(t, this));
		}
	}

	protected boolean stillValid() {
		return true;
	}

	protected Vector getValidStuff() {
		KahluaTable container;
		if (mode == INVENTORY) container = Engine.instance.player.inventory;
		else container = Engine.instance.cartridge.currentThings();
		Vector newthings = new Vector(container.len());
		KahluaTableIterator it = container.iterator();
		while (it.advance()) {
			Thing t = (Thing)it.getValue();
			if (t.isVisible()) newthings.addElement(t);
		}
		return newthings;
	}

	protected String getStuffName(Object what) {
		return ((Thing)what).name;
	}
	
	protected Image getStuffIcon(Object what) {
		if (((Thing)what).isLocated()) return compass;
		else return null;
	}
}
