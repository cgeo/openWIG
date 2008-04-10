package gui;

import javax.microedition.lcdui.*;
import openwig.EventTable;
import se.krka.kahlua.vm.LuaTable;

public class Input extends Form implements CommandListener, Cancellable {
	
	private static Command CMD_ANSWER = new Command("Odpovìdìt", Command.SCREEN, 0);
	
	private StringItem question;
	private TextField answer = null;
	private ChoiceGroup choice = null;
	
	private static final int TEXT = 0;
	private static final int MULTI = 1;
	private int mode = TEXT;
	
	private LuaTable input;
	
	public Input (LuaTable input) {
		super((String)input.rawget("Name"));
		this.input = input;
		question = new StringItem(null, (String)input.rawget("Text"));
		append(question);
		String type = (String)input.rawget("InputType");
		if (type == "Text") {
			answer = new TextField("Odpovìï:", null, 500, TextField.ANY);
			append(answer);
			mode = TEXT;
		} else if (type == "MultipleChoice") {
			choice = new ChoiceGroup("Odpovìï:", ChoiceGroup.EXCLUSIVE);
			LuaTable choices = (LuaTable)input.rawget("Choices");
			int n = choices.len();
			for (int i = 1; i <= n; i++) {
				choice.append((String)choices.rawget(new Double(i)), null);
			}
			append(choice);
			mode = MULTI;
		}
		addCommand(CMD_ANSWER);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_ANSWER) {
			if (mode == TEXT) {
				EventTable.callEvent(input, "OnGetInput", answer.getString());
			} else if (mode == MULTI) {
				EventTable.callEvent(input, "OnGetInput", choice.getString(choice.getSelectedIndex()));
			} else {
				EventTable.callEvent(input, "OnGetInput", null);
			}
		}
		Midlet.popDialog(this);
	}

	public void cancel() {}
	
}
