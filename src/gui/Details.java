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
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	
	private static final String[] taskStates = { "pending", "finished", "failed" };
	
	private EventTable thing;
	private Things parent;
	
	public Details (EventTable t, Things where) {
		super(t.name);
		thing = t;
		parent = where;
		append(name);
		append(image);
		append(description);
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
		
		if (t instanceof Task) {
			append(state);
		} else if (t instanceof Zone) {
			append(state);
			append(distance);
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ACTIONS) {
			Midlet.push(new Actions(thing.name, (Thing)thing, parent));
		} else if (cmd == CMD_NAVIGATE) {
			if (thing instanceof Zone) Midlet.push(new Navigation((Zone)thing));
			else if (thing.isLocated()) Midlet.push(new Navigation(thing.position));
		} else if (cmd == Midlet.CMD_BACK) {
			stop();
			Midlet.pop(this);
		}
	}

	public void prepare() {
		if (!thing.isVisible() || (thing instanceof Thing && parent != null && !parent.isPresent((Thing)thing))) {
			stop();
			Midlet.pop(this);
			return;
		}
		
		setTitle(thing.name);
		name.setLabel(thing.name);
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
		
		if (thing instanceof Thing) {
			Thing t = (Thing)thing;
			int actions = t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
			if (actions > 0) addCommand(CMD_ACTIONS);
			else removeCommand(CMD_ACTIONS);
		} else if (thing instanceof Task) {
			Task t = (Task)thing;
			state.setText(taskStates[t.state()]);
		} else if (thing instanceof Zone) {
			updateNavi();
			start();
			//updateNavi();
		}
		
		if (thing.isLocated()) addCommand(CMD_NAVIGATE);
		else removeCommand(CMD_NAVIGATE);
	}
	
	private void updateNavi () {
		Zone z = (Zone)thing;
		String ss = "(nothing)";
		switch (z.contain) {
			case Zone.DISTANT: ss = "distant"; break;
			case Zone.PROXIMITY: ss = "near"; break;
			case Zone.INSIDE: ss = "inside"; break;
		}
		state.setText(ss);
		
		if (z.ncontain == Zone.INSIDE) { 
			distance.setText("inside");
		} else {
			distance.setText(ZonePoint.makeFriendlyDistance(z.distance));
		}
	}
	
	private boolean running = false;
	synchronized private void start() {
		if (running) return;
		running = true;
		new Thread(this).start();
	}
	
	synchronized private void stop() {
		running = false;
	}
	
	public void run () {
		while (running) {
			try { Thread.sleep(Midlet.config.getInt(Config.REFRESH_INTERVAL) * 1000); } catch (Exception e) { }
			updateNavi();
		}
	}
	
}
