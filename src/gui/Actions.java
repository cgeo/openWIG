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
				Engine.callEvent(thing, "On"+z.getName(), null);
			}
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop();
		}
	}

	public void prepare() {
		int index = getSelectedIndex();
		deleteAll();
		actions.removeAllElements();
		Vector v = thing.actions;
		for (int i = 0; i < v.size(); i++) {
			Action a = (Action) v.elementAt(i);
			if (a.isEnabled() && !a.hasParameter()) {
				actions.addElement(a);
				append((String) a.getName(), null);
			}
		}
		int s = size();
		if (s > 0) {
			if (index >= s) index = s - 1;
			if (index < 0) index = 0;
			setSelectedIndex(index, true);
		}
	}
}
