package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Thing;

public class Things extends List implements CommandListener, Pushable {
	
	private Vector things = new Vector();
	
	public static final int INVENTORY = 0;
	public static final int SURROUNDINGS = 1;
	private int mode;
	
	public Things (String title, int mode) {
		super(title, IMPLICIT);
		this.mode = mode;
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
					Midlet.push(new Details(z, this));
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
		Vector container;
		if (mode == INVENTORY) container = Engine.instance.player.things;
		else container = Engine.instance.cartridge.currentThings();
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
	
	public boolean isPresent (Thing t) {
		return things.contains(t);
	}
}
