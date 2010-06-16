package gui;

import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import se.krka.kahlua.vm.LuaTable;
import util.Config;

public class Input implements CommandListener, ItemCommandListener, Cancellable, Pushable {
	
	private static Command CMD_ANSWER = new Command("Answer", Command.SCREEN, 0);
	private static Command CMD_SHOWFULL = new Command("Show", Command.ITEM, 1);

	private class ConcreteInput extends Form {
		private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
		private StringItem question = new StringItem(null, null);
		private Item answer;

		public ConcreteInput (Item answer) {
			super("Question");
			this.answer = answer;

			image.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
			question.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_LEFT);

			append(image);
			append(question);
			append(answer);

			addCommand(CMD_ANSWER);
			addCommand(Midlet.CMD_BACK);
			setCommandListener(Input.this);
		}

		public void setAltText (String text) {
			image.setAltText(text);
		}

		public void setImage (Image i) {
			image.setImage(i);
		}

		public void unsetImage () {
			image.setImage(Midlet.NULL_IMAGE);
			image.setImage(null);
		}

		public void setQuestionText (String text) {
			question.setText(text);
		}

		public void scrollUp () {
			Midlet.display.setCurrentItem(image);
		}
	}

	private ConcreteInput textInput, multipleChoiceInput;
	private ConcreteInput displayed;
	private TextField answer = new TextField("Answer: ", null, 500, TextField.ANY);
	private ChoiceGroup choice = new ChoiceGroup("Answer:", ChoiceGroup.EXCLUSIVE);
	
	private static final int TEXT = 0;
	private static final int MULTI = 1;
	private int mode = TEXT;
	
	private EventTable input;
	private Displayable parent;
	
	public Input () {
		choice.setFitPolicy(Choice.TEXT_WRAP_ON);
		if (Midlet.config.getInt(Config.CHOICE_SHOWFULL) > 0) {
			choice.addCommand(CMD_SHOWFULL);
			choice.setItemCommandListener(this);
		}

		textInput = new ConcreteInput(answer);
		multipleChoiceInput = new ConcreteInput(choice);
	}
	
	public Input reset (EventTable input, Displayable parent) {
		this.input = input;
		this.parent = parent;

		String type = (String)input.table.rawget("InputType");
		if ("Text".equals(type)) {
			answer.setString(null);
			mode = TEXT;
			displayed = textInput;
		} else if ("MultipleChoice".equals(type)) {
			// XXX class Input with this in interface would be more appropriate?
			choice.deleteAll();
			LuaTable choices = (LuaTable)input.table.rawget("Choices");
			int n = choices.len();
			for (int i = 1; i <= n; i++) {
				choice.append((String)choices.rawget(new Double(i)), null);
			}
			mode = MULTI;
			displayed = multipleChoiceInput;
		} else {
			throw new RuntimeException("input type " + type + " is not implemented");
		}
		
		Media m = (Media)input.table.rawget("Media");
		if (m != null) {
			displayed.setAltText(m.altText);
			try {
				byte[] is = Engine.mediaFile(m);
				Image i = Image.createImage(is, 0, is.length);
				displayed.setImage(i);
			} catch (Exception e) { }
		} else {
			displayed.unsetImage();
		}
		
		String text = Engine.removeHtml((String)input.table.rawget("Text"));
		displayed.setQuestionText(text);
		displayed.scrollUp();
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
			Midlet.display.setCurrent(a, displayed);
		}
	}

	public void push () {
		Midlet.show(displayed);
	}
}
