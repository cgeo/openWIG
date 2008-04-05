package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Container;
import openwig.Thing;

public class Things extends List implements CommandListener, Pushable {
	
	private Container container;
	private Vector things = new Vector();
	
	public Things (String title, Container what) {
		super(title, IMPLICIT);
		container = what;		
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.ITEM:
				int index = getSelectedIndex();
				if (index >= 0 && index < things.size()) {
					Thing z = (Thing)things.elementAt(index);
					Midlet.push(new Details(z));
				}
				break;
			case Command.BACK:
				Midlet.pop();
				break;
		}
	}

	public void prepare() {
		int index = getSelectedIndex();
		deleteAll();
		things.removeAllElements();
		Vector v = container.things();
		for (int i = 0; i < v.size(); i++) {
			Thing t = (Thing)v.elementAt(i);
			if (t.isVisible()) {
				things.addElement(t);
				append(t.name, null);
			}
		}
		int s = size();
		if (s > 0) {
			if (index >= s) index = s-1;
			if (index < 0) index = 0;
			setSelectedIndex(index, true);
		}
	}
	
}
