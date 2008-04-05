package gui;

import javax.microedition.lcdui.*;

public class Dialog extends Form implements CommandListener {

	private StringItem content = new StringItem(null, null);
	
	private boolean kill = false;
	
	public Dialog () {
		super("dialog");
		append(content);
		setCommandListener(this);
		addCommand(Midlet.CMD_NEXT);
		addCommand(Midlet.CMD_CANCEL);
	}
	
	public void run (String[] texts) {
		for (int i = 0; i < texts.length; i++) synchronized (this) {
			if (kill) break;
			if (i == texts.length - 1) {
				removeCommand(Midlet.CMD_NEXT);
				addCommand(Midlet.CMD_OK);
			}
			content.setText(texts[i]);
			try { wait(); } catch (InterruptedException e) { }
		}
	}

	synchronized public void commandAction(Command cmd, Displayable disp) {
		if (disp != this) return;
		
		switch (cmd.getCommandType()) {
			case Command.OK:
				notify();
				break;
			case Command.CANCEL: case Command.BACK:
				kill();
				break;
			default:
				return;
		}
	}
	
	synchronized public void kill () {
		kill = true;
		notify();
	}
}
