package gui;

import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Actions extends ListOfStuff {

	private Thing thing;
	
	public Actions () {
		super("");
		parent = Midlet.details;
	}

	public Actions reset(String title, Thing what) {
		setTitle(title);
		thing = what;
		return this;
	}

	protected void callStuff(Object what) {
		Action z = (Action)what;
		String eventName = "On" + z.getName();

		if (z.hasParameter()) {
			Midlet.push(Midlet.targets.reset(thing.name + ": " + z.text, z, thing));
		} else {
			Midlet.push(parent);
			Engine.callEvent(thing, eventName, null);
		}
	}

	protected boolean stillValid() {
		if (!thing.visibleToPlayer()) return false;
		return thing.visibleActions() > 0;
	}

	protected Vector getValidStuff() {
		int size = thing.actions.size();
		Vector newactions = new Vector(size);
		for (int i = 0; i < thing.actions.size(); i++) newactions.addElement(thing.actions.elementAt(i));
		
		for (int i = 0; i < newactions.size(); i++) {
			Action a = (Action) newactions.elementAt(i);
			if (!a.isEnabled() ||
				(a.hasParameter()
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
		return a.text;
	}
}
