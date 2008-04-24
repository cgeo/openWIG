package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
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
	
	private EventTable input;
	
	public Input (EventTable input) {
		super(input.name);
		this.input = input;
		question = new StringItem(null, (String)input.table.rawget("Text"));
		append(question);
		String type = (String)input.table.rawget("InputType");
		if (type == "Text") {
			answer = new TextField("Odpovìï:", null, 500, TextField.ANY);
			append(answer);
			mode = TEXT;
		} else if (type == "MultipleChoice") {
			choice = new ChoiceGroup("Odpovìï:", ChoiceGroup.EXCLUSIVE);
			// XXX class Input with this in interface would be more appropriate?
			LuaTable choices = (LuaTable)input.table.rawget("Choices");
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
				Engine.callEvent(input, "OnGetInput", answer.getString().intern());
			} else if (mode == MULTI) {
				Engine.callEvent(input, "OnGetInput", choice.getString(choice.getSelectedIndex()).intern());
			} else {
				Engine.callEvent(input, "OnGetInput", null);
			}
		}
		Midlet.popDialog(this);
	}

	public void cancel() {}
	
}
