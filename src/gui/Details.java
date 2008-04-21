package gui;

import java.io.InputStream;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;
import openwig.Thing;
import openwig.Task;
import openwig.Zone;

public class Details extends Form implements CommandListener, Pushable, Runnable {
	
	private static final Command CMD_ACTIONS = new Command("Actions", Command.SCREEN, 0);
	
	private StringItem description = new StringItem(null, null);
	private StringItem state = new StringItem(null, null);
	private StringItem distance = new StringItem(null, null);
	private StringItem direction = new StringItem(null, null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	//private Image media = null;
	
	static String[] taskStates = { "nesplnìno", "hotovo", "neúspìch" };
	
	private EventTable thing;
	private Things parent;
	
	public Details (EventTable t, Things where) {
		super(null);
		thing = t;
		parent = where;
		append(image);
		append(description);
		append(state);
		append(distance); append(direction);
		setCommandListener(this);
		addCommand(Midlet.CMD_BACK);
		
		Media m = (Media)t.table.rawget("Media");
		if (m != null) {
			image.setAltText(m.altText);
			try {
				InputStream is = Engine.mediaFile(m);
				Image i = Image.createImage(is);
				image.setImage(i);
			} catch (Exception e) { }
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ACTIONS) {
			Midlet.push(new Actions(thing.name, (Thing)thing, parent));
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
		
		removeCommand(CMD_ACTIONS);		
		setTitle(thing.name);
		description.setText(thing.description);
		
		if (thing instanceof Thing) {
			Thing t = (Thing)thing;
			int actions = t.visibleActions() + Engine.instance.cartridge.visibleUniversalActions();
			if (actions > 0) addCommand(CMD_ACTIONS);
		} else if (thing instanceof Task) {
			Task t = (Task)thing;
			state.setLabel("\nStav: ");
			state.setText(taskStates[t.state()]);
		} else if (thing instanceof Zone) {
			updateNavi();
			start();
			//updateNavi();
		}
	}
	
	private void updateNavi () {
		Zone z = (Zone)thing;
		state.setLabel("\nStav: ");
		String ss = "(nic)";
		switch (z.contain) {
			case Zone.DISTANT: ss = "distant"; break;
			case Zone.PROXIMITY: ss = "proximity"; break;
			case Zone.INSIDE: ss = "uvnitø"; break;
		}
		state.setText(ss);
		
		distance.setLabel("\nVzdál.: ");
		long part = (long)(z.distance * 1000);
		double d = part/1000.0;
		distance.setText(Double.toString(d)+" m");
		direction.setLabel("\nSmìr: ");
		// uhodnuti smeru
		double x = z.nearestX - Midlet.latitude;
		double y = z.nearestY - Midlet.longitude;
		double xx = x*2, yy = y*2;
		String s;
		if (x > 0 && x > Math.abs(yy)) s = "sever";
		else if (y > 0 && x > 0 && x <= yy && y <= xx) s = "severovýchod";
		else if (y > 0 && y > Math.abs(xx)) s = "východ";
		else if (y > 0 && x < 0 && -x <= yy && y <= -xx) s = "jihovýchod";
		else if (x < 0 && -x > Math.abs(yy)) s = "jih";
		else if (y < 0 && x < 0 && x >= yy && y >= xx) s = "jihozápad";
		else if (y < 0 && -y > Math.abs(xx)) s = "západ";
		else s = "severozápad";
		direction.setText(s+"\nnejbližší bod: "+z.nearestX+","+z.nearestY);
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
			try { Thread.sleep(1000); } catch (Exception e) { }
			updateNavi();
		}
	}
	
}
