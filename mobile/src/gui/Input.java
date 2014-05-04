package gui;

import cz.matejcik.openwig.DialogObject;
import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Engine;
import util.Config;

public class Input implements CommandListener, ItemCommandListener, Cancellable, Pushable {
	
	private static Command CMD_ANSWER = new Command("Answer", Command.SCREEN, 0);
	private static Command CMD_SHOWFULL = new Command("Show", Command.ITEM, 1);

	private class ConcreteInput extends Form {
		private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
		private StringItem question = new StringItem(null, null);

		public ConcreteInput (Item answer) {
			super("Question");

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
	
	private Displayable parent;
	private DialogObject dobj;
	
	public Input () {
		choice.setFitPolicy(Choice.TEXT_WRAP_ON);
		if (Midlet.config.getInt(Config.CHOICE_SHOWFULL) > 0) {
			choice.addCommand(CMD_SHOWFULL);
			choice.setItemCommandListener(this);
		}

		textInput = new ConcreteInput(answer);
		multipleChoiceInput = new ConcreteInput(choice);
	}
	
	public Input resetForChoice (DialogObject dobj, String[] choices, Displayable parent)
	{
		mode = MULTI;
		choice.deleteAll();
		for (int i = 0; i < choices.length; i++)
			choice.append(choices[i], null);
		displayed = multipleChoiceInput;
		
		return finishReset (dobj, parent);
	}
	
	public Input resetForText (DialogObject dobj, Displayable parent) {
		answer.setString(null);
		displayed = textInput;
		mode = TEXT;
		
		return finishReset (dobj, parent);
	}
	
	private Input finishReset (DialogObject dobj, Displayable parent) {
		this.dobj = dobj;
		this.parent = parent;

		if (dobj.media != null) {
			displayed.setAltText(dobj.media.altText);
			try {
				byte[] is = Engine.mediaFile(dobj.media);
				Image i = Image.createImage(is, 0, is.length);
				displayed.setImage(i);
			} catch (Exception e) { }
		} else {
			displayed.unsetImage();
		}
		
		displayed.setQuestionText(dobj.text);
		displayed.scrollUp();
		return this;
	}

	public void commandAction(Command cmd, Displayable disp) {
		Midlet.push(parent);		
		if (cmd == CMD_ANSWER) {
			if (mode == TEXT) {
				dobj.doCallback(answer.getString());
			} else if (mode == MULTI) {
				dobj.doCallback(new Integer(choice.getSelectedIndex()));
			} else {
				dobj.doCallback(null);
			}
		}
	}

	public Displayable cancel() {
		dobj.doCallback(null);
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
