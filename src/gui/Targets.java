package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;
import openwig.Container;

public class Targets extends List implements CommandListener, Pushable {

	private Action action;
	private Vector targets = new Vector();

	public Targets(String title, Action what) {
		super(title, IMPLICIT);
		action = what;
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_SELECT) {
			int index = getSelectedIndex();
			if (index >= 0 && index < targets.size()) {
				Midlet.pop(this);
				Thing t = (Thing) targets.elementAt(index);
				String eventName = "On"+action.getName();
				Engine.callEvent(action.getActor(), eventName, t);
			}
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop(this);
		}
	}

	private void addTargets(Vector v) {
		for (int i = 0; i < v.size(); i++) {
			Thing t = (Thing)v.elementAt(i);
			if (t.isVisible() && action.isTarget(t)) {
				append(t.name, null);
				targets.addElement(t);
			}
		}
	}

	public void prepare() {
		int index = getSelectedIndex();
		deleteAll();
		targets.removeAllElements();
		addTargets(Engine.instance.cartridge.currentThings());
		addTargets(Engine.instance.player.things);

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
