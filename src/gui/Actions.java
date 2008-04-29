package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Actions extends ListOfStuff {

	private Thing thing;
	private Things parent;

	public Actions(String title, Thing what, Things where) {
		super(title);
		thing = what;
		parent = where;
	}

	protected void callStuff(Object what) {
		Action z = (Action)what;
		String eventName = "On" + z.getName();

		if (z.getActor() == thing) {
			if (z.hasParameter()) {
				Midlet.push(new Targets(thing.name + ": " + z.text, z, parent));
			} else {
				Engine.callEvent(thing, eventName, null);
			}
		} else if (z.hasParameter()) {
			Engine.callEvent(z.getActor(), eventName, thing);
		}
	}

	protected boolean stillValid() {
		return parent == null || parent.isPresent(thing);
	}

	protected Vector getValidStuff() {
		System.out.println("getting valid stuff for thing "+thing.name);
		int size = thing.actions.size() + thing.foreignActions.size();
		Vector newactions = new Vector(size);
		for (int i = 0; i < thing.actions.size(); i++) newactions.addElement(thing.actions.elementAt(i));
		for (int i = 0; i < thing.foreignActions.size(); i++) newactions.addElement(thing.foreignActions.elementAt(i));
		
		for (int i = 0; i < newactions.size(); i++) {
			Action a = (Action) newactions.elementAt(i);
			if (!a.isEnabled() ||
				(a.getActor() == thing
				&& a.hasParameter()
				&& (a.targetsInside(Engine.instance.cartridge.currentThings()) + a.visibleTargets(Engine.instance.player)) < 1)
			) {
				newactions.removeElementAt(i--);
				continue;
			}
		}
		return newactions;
	}

	protected String getStuffName(Object what) {
		Action a = (Action) what;
		if (a.getActor() == thing) {
			return a.text;
		} else {
			return a.getActor().name + ": " + a.text;
		}
	}
}
