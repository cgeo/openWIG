package gui;

import javax.microedition.lcdui.*;
import openwig.EventTable;
import se.krka.kahlua.vm.LuaTable;

public class Input extends Form implements CommandListener, Cancellable {
	
	private static Command CMD_ANSWER = new Command("Odpovìdìt", Command.SCREEN, 0);
	
	private StringItem question;
	private TextField answer;
	
	private LuaTable input;
	
	public Input (LuaTable input) {
		super((String)input.rawget("Name"));
		this.input = input;
		question = new StringItem(null, (String)input.rawget("Text"));
		answer = new TextField("Odpovìï:", null, 500, TextField.ANY);
		append(question);
		append(answer);
		addCommand(CMD_ANSWER);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ANSWER) {
			EventTable.callEvent(input, "OnGetInput", answer.getString());
		}
		Midlet.popDialog(this);
	}

	public void cancel() {}
	
}
