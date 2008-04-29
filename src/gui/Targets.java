package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Targets extends ListOfStuff {

	private Action action;
	private Things parent;

	public Targets(String title, Action what, Things parent) {
		super(title);
		action = what;
		this.parent = parent;
	}

	protected void callStuff(Object what) {
		Midlet.pop(this);
		Thing t = (Thing) what;
		String eventName = "On"+action.getName();
		Engine.callEvent(action.getActor(), eventName, t);
	}

	protected boolean stillValid() {
		return parent == null || parent.isPresent(action.getActor());
	}

	protected Vector getValidStuff() {
		Vector current = Engine.instance.cartridge.currentThings();
		int size = current.size() + Engine.instance.player.things.size();
		Vector newtargets = new Vector(size);
		for (int i = 0; i < current.size(); i++) newtargets.addElement(current.elementAt(i));
		for (int i = 0; i < Engine.instance.player.things.size(); i++) newtargets.addElement(Engine.instance.player.things.elementAt(i));
		
		for (int i = 0; i < newtargets.size(); i++) {
			Thing t = (Thing)newtargets.elementAt(i);
			if (! t.isVisible() || ! action.isTarget(t)) {
				newtargets.removeElementAt(i--);
			}
		}
		
		return newtargets;
	}

	protected String getStuffName(Object what) {
		Thing t = (Thing)what;
		return t.name;
	}
}
