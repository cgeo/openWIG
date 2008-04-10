package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
import se.krka.kahlua.vm.*;

public class Dialog extends Form implements CommandListener, Cancellable {

	private StringItem content = new StringItem(null, null);
	private String[] texts;
	private int page = -1;
	
	private static Command CMD_OK = new Command("OK", Command.SCREEN, 1);
	private static Command CMD_CANCEL = new Command("Zrušit", Command.BACK, 2);
	
	private LuaClosure callback;

	public Dialog(String[] texts, String button1, String button2, LuaClosure callback) {
		super("dialog");
		append(content);
		setCommandListener(this);
		if (button1 != null) {
			addCommand(new Command(button1, Command.SCREEN, 1));
		} else {
			addCommand(CMD_OK);
		}
		if (button2 != null) {
			addCommand(new Command(button2, Command.BACK, 2));
		} else {
			addCommand(CMD_CANCEL);
		}
		this.texts = texts;
		this.callback = callback;
		nextPage();
	}

	private void nextPage() {
		page++;
		if (page >= texts.length) {
			if (callback != null) Engine.invokeCallback(callback, Boolean.TRUE);
			callback = null;
			Midlet.popDialog(this);
			return;
		}
		content.setText(texts[page]);
	}

	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.SCREEN:
				nextPage();
				break;
			case Command.CANCEL: case Command.BACK:
				Midlet.popDialog(this);
			default:
				return;
		}
	}

	public void cancel() {
		if (callback != null) Engine.invokeCallback(callback, null);
	}
}
