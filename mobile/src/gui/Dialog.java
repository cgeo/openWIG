package gui;

import cz.matejcik.openwig.DialogObject;
import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Engine;
import se.krka.kahlua.vm.*;

public class Dialog extends Form implements CommandListener, Cancellable {

	private StringItem content = new StringItem(null, null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	private DialogObject dobj;
	
	private Command BTN1 = null;
	private Command BTN2 = null;
	
	private LuaClosure callback;
	private Displayable parent;
	
	public Dialog () {
		super("");
		image.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		content.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_LEFT);
		append(image);
		append(content);
		setCommandListener(this);
	}

	public Dialog reset (DialogObject dobj, String button1, String button2, Displayable parent) {
		removeCommand(BTN1);
		removeCommand(BTN2);
		if (button1 == null) button1 = "OK";
		addCommand(BTN1 = new Command(button1, Command.SCREEN, 1));
		if (button2 != null) {
			addCommand(BTN2 = new Command(button2, Command.SCREEN, 2));
		}
		this.dobj = dobj;
		this.parent = parent;
		
		if (dobj.media != null) try {
			byte[] is = Engine.mediaFile(dobj.media);
			image.setImage(Image.createImage(is, 0, is.length));
		} catch (Exception e) {
			image.setAltText(dobj.media.altText);
		} else {
			image.setImage(Midlet.NULL_IMAGE);
			image.setImage(null);
		}
		content.setText(dobj.text);

		Midlet.display.setCurrentItem(image);

		return this;
	}

	public void commandAction(Command cmd, Displayable disp) {
		String value = null;
		if (cmd == BTN1) {
			value = "Button1";
		} else if (cmd == BTN2) {
			value = "Button2";
		}
		
		Midlet.push(parent);
		dobj.doCallback(value);
	}

	public Displayable cancel() {
		dobj.doCallback(null);
		return parent;
	}
}
