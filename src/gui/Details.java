package gui;

import java.util.Vector;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.Thing;
import openwig.Action;

public class Details extends Form implements CommandListener, Pushable {
	
	private static final Command CMD_ACTIONS = new Command("Actions", Command.SCREEN, 0);
	
	private StringItem description = new StringItem(null, null);
	private Thing thing;
	
	public Details (Thing t) {
		super(null);
		thing = t;
		append(description);
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ACTIONS) {
			Midlet.push(new Actions(thing.name, thing));
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop();
		}
	}

	public void prepare() {
		String title = thing.name;
		String desc = (String)thing.table.rawget("Description");
		setTitle(title);
		description.setText(desc);
		
		int actions = thing.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
		if (actions > 0) addCommand(CMD_ACTIONS);
		else removeCommand(CMD_ACTIONS);
	}
	
}
