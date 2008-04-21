package gui;

import java.util.Vector;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.Zone;

public class Zones extends List implements CommandListener, Pushable {
	
	private Vector zones = new Vector();
		
	public Zones () {
		super("Zóny", IMPLICIT);
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.ITEM:
				int index = getSelectedIndex();
				if (index >= 0 && index < zones.size()) {
					Zone z = (Zone)zones.elementAt(index);
					/*String name = (String)z.table.rawget("Name");
					String description = (String)z.table.rawget("Description");
					Midlet.display.setCurrent(new Alert(name, description, null, AlertType.INFO), disp);*/
					Midlet.push(new Details(z, null));
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
		zones.removeAllElements();
		Vector v = Engine.instance.cartridge.zones;
		for (int i = 0; i < v.size(); i++) {
			Zone z = (Zone)v.elementAt(i);
			if (z.isVisible()) {
				zones.addElement(z);
				append((String)z.table.rawget("Name"), null);
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
