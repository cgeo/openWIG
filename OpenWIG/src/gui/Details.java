package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;
import openwig.Thing;
import openwig.Task;
import openwig.Zone;
import openwig.ZonePoint;
import util.Config;

public class Details extends Form implements CommandListener, Pushable, Runnable {
	
	private static final Command CMD_ACTIONS = new Command("Actions", Command.SCREEN, 0);
	private static final Command CMD_NAVIGATE = new Command("Navigate", Command.SCREEN, 1);
	
	private StringItem name = new StringItem(null, null);
	private StringItem description = new StringItem(null, null);
	private StringItem state = new StringItem("State: ", null);
	private StringItem distance = new StringItem("Distance: ", null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_CENTER, null);
	
	private static final String[] taskStates = { "pending", "finished", "failed" };
	
	private EventTable thing;
	private Displayable parent;
	
	public Details () {
		super("");
		
		name.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		image.setLayout(Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		description.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_LEFT);
		state.setLayout(Item.LAYOUT_NEWLINE_AFTER);
				
		append(name);
		append(image);
		append(description);
		append(state);
		append(distance);
		
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
	}
	
	public Details reset (EventTable t, Displayable where) {
		setTitle(t.name);
		thing = t;

                if (where instanceof ListOfStuff) {
                    parent = where;
                } else {
                    parent = Midlet.mainMenu;
                }
		
		if (t instanceof Zone) {
			state.setLabel("State: ");
			distance.setLabel("Distance: ");
		} else if (t instanceof Task) {
			state.setLabel("State: ");
			distance.setLabel(null);
			distance.setText(null);
		} else {
			state.setLabel(null);
			distance.setLabel(null);
			state.setText(null);
			distance.setText(null);
		}

		return this;
	}

	public void commandAction(Command cmd, Displayable disp) {
		stop();
		if (cmd == CMD_ACTIONS) {
			Midlet.push(Midlet.actions.reset(thing.name, (Thing)thing));
		} else if (cmd == CMD_NAVIGATE) {
			if (thing instanceof Zone) Midlet.push(Midlet.navigation.reset(this, (Zone)thing));
			else if (thing.isLocated()) Midlet.push(Midlet.navigation.reset(this, thing.position));
		} else {
			pop();
		}
	}
	
	private void pop () {
		stop();
		if (parent != null)
			Midlet.push(parent);
		else if (thing instanceof Zone)
			Midlet.push(Midlet.zones);
		else if (thing instanceof Task)
			Midlet.push(Midlet.tasks);
		else
			Midlet.push(Midlet.mainMenu);
	}
	
	public boolean stillValid () {
		if (thing instanceof Thing) return ((Thing)thing).visibleToPlayer();
		return thing.isVisible();
	}

	public void push () {
		if (!stillValid()) {
			pop();
			return;
		}
		
		setTitle(thing.name);
		name.setText(thing.name);
		description.setText(thing.description);
				
		Media m = (Media)thing.table.rawget("Media");
		if (m != null) {
			image.setAltText(m.altText);
			try {
				byte[] is = Engine.mediaFile(m);
				Image i = Image.createImage(is, 0, is.length);
				image.setImage(i);
			} catch (Exception e) { }
		} else {
			image.setImage(null);
		}
		
		removeCommand(CMD_ACTIONS);
		if (thing instanceof Thing) {
			Thing t = (Thing)thing;
			int actions = t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
			if (actions > 0) addCommand(CMD_ACTIONS);
		} else if (thing instanceof Task) {
			Task t = (Task)thing;
			state.setText(taskStates[t.state()]);
		} else if (thing instanceof Zone) {
			updateNavi();
			start();
		}
		
		if (thing.isLocated()) addCommand(CMD_NAVIGATE);
		else removeCommand(CMD_NAVIGATE);
		
		Midlet.show(this);
		Midlet.display.setCurrentItem(name);
	}
	
	private void updateNavi () {
		if (!(thing instanceof Zone)) return;
		Zone z = (Zone)thing;
		String ss = "(nothing)";
		switch (z.contain) {
			case Zone.DISTANT: ss = "distant"; break;
			case Zone.PROXIMITY: ss = "near"; break;
			case Zone.INSIDE: ss = "inside"; break;
		}
		state.setText(ss);
		
		if (z.contain == Zone.INSIDE) { 
			distance.setText("inside");
		} else {
			distance.setText(ZonePoint.makeFriendlyDistance(z.distance));
		}
	}
	
	private Thread thread = null;
	synchronized private void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	synchronized private void stop() {
		thread = null;
	}
	
	public void run () {
		while (thread == Thread.currentThread()) {
			if (!(thing instanceof Zone)) break;
			updateNavi();
			try { Thread.sleep(Midlet.config.getInt(Config.REFRESH_INTERVAL) * 1000); } catch (Exception e) { }
		}
	}
	
}
