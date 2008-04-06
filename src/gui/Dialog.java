package gui;

import javax.microedition.lcdui.*;

public class Dialog extends Form implements CommandListener {

	private StringItem content = new StringItem(null, null);
	private String[] texts;
	private int page = -1;

	public Dialog(String[] texts) {
		super("dialog");
		append(content);
		setCommandListener(this);
		addCommand(Midlet.CMD_NEXT);
		addCommand(Midlet.CMD_CANCEL);
		this.texts = texts;
		nextPage();
	}

	private void nextPage() {
		page++;
		if (page >= texts.length) {
			Midlet.popDialog(this);
			return;
		} else if (page == texts.length - 1) {
			removeCommand(Midlet.CMD_NEXT);
			addCommand(Midlet.CMD_OK);
		}
		content.setText(texts[page]);
	}

	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.OK:
				nextPage();
				break;
			case Command.CANCEL: case Command.BACK:
				Midlet.popDialog(this);
			default:
				return;
		}
	}
}
