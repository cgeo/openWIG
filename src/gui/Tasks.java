package gui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import openwig.Engine;
import openwig.Task;

public class Tasks extends List implements CommandListener, Pushable {
	
	private Vector things = new Vector();
	
	private static Image[] stateIcons;
	
	static {
		stateIcons = new Image[3];
		try {
			stateIcons[Task.PENDING] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-pending.png"));
			stateIcons[Task.DONE] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-done.png"));
			stateIcons[Task.FAILED] = Image.createImage(Tasks.class.getResourceAsStream("/icons/task-failed.png"));
		} catch (Exception e) { }
	}
	
	public Tasks () {
		super("Úkoly", IMPLICIT);
		addCommand(Midlet.CMD_BACK);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.ITEM:
				int index = getSelectedIndex();
				if (index >= 0 && index < things.size()) {
					Task z = (Task)things.elementAt(index);
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
		Vector v = Engine.instance.cartridge.tasks;
		for (int i = 0; i < v.size(); i++) {
			Task t = (Task)v.elementAt(i);
			if (t.isVisible()) {
				things.addElement(t);
				append(t.name, stateIcons[t.state()]);
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
