package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;

public class Things extends ListOfStuff {
	
	public static final int INVENTORY = 0;
	public static final int SURROUNDINGS = 1;
	private int mode;
	
	public Things (String title, int mode) {
		super(title);
		this.mode = mode;
	}
	
	synchronized public boolean isPresent (Thing t) {
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
		Vector container;
		if (mode == INVENTORY) container = Engine.instance.player.things;
		else container = Engine.instance.cartridge.currentThings();
		Vector newthings = new Vector(container.size());
		for (int i = 0; i < container.size(); i++) {
			Thing t = (Thing)container.elementAt(i);
			if (t.isVisible()) newthings.addElement(t);
		}
		return newthings;
	}

	protected String getStuffName(Object what) {
		return ((Thing)what).name;
	}
}
