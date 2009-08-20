package gui;

import java.util.Vector;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.Zone;

public class Zones extends List implements CommandListener, Pushable {
	
	private Vector zones = new Vector();
		
	public Zones () {
		super("Locations", IMPLICIT);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.ITEM:
				int index = getSelectedIndex();
				if (index >= 0 && index < zones.size()) {
					Zone z = (Zone)zones.elementAt(index);
					Midlet.push(Midlet.details.reset(z, this));
				}
				break;
			case Command.BACK:
				Midlet.push(Midlet.mainMenu);
				break;
		}
	}

	public void push () {
		int index = getSelectedIndex();
		deleteAll();
		zones.removeAllElements();
		Vector v = Engine.instance.cartridge.zones;
		for (int i = 0; i < v.size(); i++) {
			Zone z = (Zone)v.elementAt(i);
			if (z.isVisible()) {
				zones.addElement(z);
				append(z.name, null);
			}
		}
		int s = size();
		if (s > 0) {
			if (index >= s) index = s-1;
			if (index < 0) index = 0;
			setSelectedIndex(index, true);
		}
		Midlet.show(this);
	}
	
}
