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
	private Vector actions = new Vector();
	
	public Details (Thing t) {
		super(null);
		thing = t;
		append(description);
		setCommandListener(this);
		addCommand(CMD_ACTIONS);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ACTIONS) {
			Midlet.push(new Actions(getTitle(), thing));
		} else switch (cmd.getCommandType()) {
			case Command.SCREEN:
				// perform single action
				break;
			case Command.BACK:
				Midlet.pop();
				break;
		}
	}

	public void prepare() {
		String title = (String)thing.table.rawget("Name");
		String desc = (String)thing.table.rawget("Description");
		setTitle(title);
		description.setText(desc);
	}
	
}
