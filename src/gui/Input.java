package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.EventTable;
import openwig.Media;
import se.krka.kahlua.vm.LuaTable;
import util.Config;

public class Input extends Form implements CommandListener, ItemCommandListener, Cancellable {
	
	private static Command CMD_ANSWER = new Command("Answer", Command.SCREEN, 0);
	private static Command CMD_SHOWFULL = new Command("Show", Command.ITEM, 1);

	private TextField answer = new TextField("Answer: ", null, 500, TextField.ANY);
	private ChoiceGroup choice = new ChoiceGroup("Answer:", ChoiceGroup.EXCLUSIVE);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	private StringItem question = new StringItem(null, null);
	
	private static final int TEXT = 0;
	private static final int MULTI = 1;
	private int mode = TEXT;
	
	private EventTable input;
	private Displayable parent;
	
	public Input () {
		super("Question");

		image.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		question.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_LEFT);

		append(image);
		append(question);
		append(answer);
		
		choice.setFitPolicy(Choice.TEXT_WRAP_ON);
		if (Midlet.config.getInt(Config.CHOICE_SHOWFULL) > 0) {
			choice.addCommand(CMD_SHOWFULL);
			choice.setItemCommandListener(this);
		}
		
		addCommand(CMD_ANSWER);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);		
	}
	
	public Input reset (EventTable input, Displayable parent) {
		this.input = input;
		this.parent = parent;
		
		Media m = (Media)input.table.rawget("Media");
		if (m != null) {
			image.setAltText(m.altText);
			try {
				byte[] is = Engine.mediaFile(m);
				Image i = Image.createImage(is, 0, is.length);
				image.setImage(i);
			} catch (Exception e) { }
		}
		
		String text = Engine.removeHtml((String)input.table.rawget("Text"));
		question.setText(text);
		
		String type = (String)input.table.rawget("InputType");
		if ("Text".equals(type)) {
			answer.setString(null);
			if (mode != TEXT) set(2, answer);
			mode = TEXT;
		} else if ("MultipleChoice".equals(type)) {
			// XXX class Input with this in interface would be more appropriate?
			choice.deleteAll();
			LuaTable choices = (LuaTable)input.table.rawget("Choices");
			int n = choices.len();
			for (int i = 1; i <= n; i++) {
				choice.append((String)choices.rawget(new Double(i)), null);
			}
			if (mode != MULTI) set(2, choice);
			mode = MULTI;
		}
		Midlet.display.setCurrentItem(image);
		return this;
	}

	public void commandAction(Command cmd, Displayable disp) {
		Midlet.push(parent);		
		if (cmd == CMD_ANSWER) {
			if (mode == TEXT) {
				Engine.callEvent(input, "OnGetInput", answer.getString());
			} else if (mode == MULTI) {
				Engine.callEvent(input, "OnGetInput", choice.getString(choice.getSelectedIndex()));
			} else {
				Engine.callEvent(input, "OnGetInput", null);
			}
		}
	}

	public Displayable cancel() {
		Engine.callEvent(input, "OnGetInput", null);
		return parent;
	}

	public void commandAction(Command cmd, Item it) {
		if (it == choice && cmd == CMD_SHOWFULL) {
			Alert a = new Alert(null, choice.getString(choice.getSelectedIndex()), null, AlertType.INFO);
			a.setTimeout(Alert.FOREVER);
			Midlet.display.setCurrent(a,this);
		}
	}
	
}
