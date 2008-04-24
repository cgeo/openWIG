package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Actions extends List implements CommandListener, Pushable {

	private Thing thing;
	private Things parent;
	private Vector actions = new Vector();

	public Actions(String title, Thing what, Things where) {
		super(title, IMPLICIT);
		thing = what;
		parent = where;
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_SELECT) {
			int index = getSelectedIndex();
			if (index >= 0 && index < actions.size()) {
				Action z = (Action) actions.elementAt(index);
				String eventName = "On"+z.getName();
				
				if (z.getActor() == thing) {
					if (z.hasParameter()) {
						Midlet.push(new Targets(thing.name+": "+z.text, z));
					} else {
						Engine.callEvent(thing, eventName, null);
					}
				} else if (z.hasParameter()) {
					Engine.callEvent(z.getActor(), eventName, thing);
				}
			}
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop(this);
		}
	}

	private void addActions(Vector v) {
		for (int i = 0; i < v.size(); i++) {
			Action a = (Action) v.elementAt(i);
			if (a.isEnabled()) {
				String name;
				if (a.getActor() == thing) {
					name = a.text;
					if (a.hasParameter()) {
						int targets = a.targetsInside(Engine.instance.cartridge.currentThings())
							+ a.visibleTargets(Engine.instance.player);
						if (targets < 1) continue;
					}
				} else {
					name = a.getActor().name + ": " + a.text;
				}
				append(name, null);
				actions.addElement(a);
			}
		}

	}

	public void prepare() {
		if (/*!thing.isVisible()*/parent != null && !parent.isPresent(thing)) {
			Midlet.pop(this);
			return;
		}
		
		int index = getSelectedIndex();
		deleteAll();
		actions.removeAllElements();
		addActions(thing.actions);
		addActions(thing.foreignActions);
		addActions(Engine.instance.cartridge.universalActions);

		int s = size();
		if (s > 0) {
			if (index >= s) {
				index = s - 1;
			}
			if (index < 0) {
				index = 0;
			}
			setSelectedIndex(index, true);
		}
	}
}
