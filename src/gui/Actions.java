package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Actions extends List implements CommandListener, Pushable {

	private Thing thing;
	private Vector actions = new Vector();

	public Actions(String title, Thing what) {
		super(title, IMPLICIT);
		thing = what;
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_SELECT) {
			int index = getSelectedIndex();
			if (index >= 0 && index < actions.size()) {
				Action z = (Action) actions.elementAt(index);
				Thing source = thing;
				Thing target = null;
				if (z.hasParameter()) {
					source = z.getActor();
					target = thing;
				}
				Engine.callEvent(source, "On" + z.getName(), target);
			}
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop();
		}
	}

	private void addActions(Vector v) {
		for (int i = 0; i < v.size(); i++) {
			Action a = (Action) v.elementAt(i);
			if (a.isEnabled()) {
				String name;
				if (a.getActor() == thing)
					name = a.getName();
				else
					name = a.getActor().name + ": " + a.getName();
				append(name, null);
				actions.addElement(a);
			}
		}

	}

	public void prepare() {
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
