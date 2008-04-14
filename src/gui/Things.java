package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Container;
import openwig.Thing;

public class Things extends List implements CommandListener, Pushable {
	
	private Vector container;
	private Vector things = new Vector();
	
	public Things (String title, Vector what) {
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
				Midlet.pop(this);
				break;
		}
	}

	public void prepare() {
		int index = getSelectedIndex();
		deleteAll();
		things.removeAllElements();
		for (int i = 0; i < container.size(); i++) {
			Thing t = (Thing)container.elementAt(i);
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
