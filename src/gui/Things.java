package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import se.krka.kahlua.vm.LuaTable;

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
		Midlet.push(new Details(t, this));
	}

	protected boolean stillValid() {
		return true;
	}

	protected Vector getValidStuff() {
		LuaTable container;
		if (mode == INVENTORY) container = Engine.instance.player.inventory;
		else container = Engine.instance.cartridge.currentThings();
		Vector newthings = new Vector(container.len());
		Object key = null;
		while ((key = container.next(key)) != null) {
			Thing t = (Thing)container.rawget(key);
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
