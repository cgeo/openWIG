package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Thing;
import openwig.Task;

public class Details extends Form implements CommandListener, Pushable {
	
	private static final Command CMD_ACTIONS = new Command("Actions", Command.SCREEN, 0);
	
	private StringItem description = new StringItem(null, null);
	private StringItem state = new StringItem(null, null);
	
	static String[] taskStates = { "nesplnìno", "hotovo", "neúspìch" };
	
	private EventTable thing;
	
	public Details (EventTable t) {
		super(null);
		thing = t;
		append(description);
		append(state);
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ACTIONS) {
			Midlet.push(new Actions(thing.name, (Thing)thing));
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop(this);
		}
	}

	public void prepare() {
		if (!thing.isVisible()) {
			Midlet.pop(this);
			return;
		}
		
		removeCommand(CMD_ACTIONS);		
		setTitle(thing.name);
		description.setText(thing.description);
		
		if (thing instanceof Thing) {
			Thing t = (Thing)thing;
			int actions = t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
			if (actions > 0) addCommand(CMD_ACTIONS);
			state.setLabel(null); state.setText(null);
		} else if (thing instanceof Task) {
			Task t = (Task)thing;
			state.setLabel("\nStav: ");
			state.setText(taskStates[t.state()]);
		}
	}
	
}
