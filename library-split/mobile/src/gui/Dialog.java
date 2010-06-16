package gui;

import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;
import se.krka.kahlua.vm.*;

public class Dialog extends Form implements CommandListener, Cancellable {

	private StringItem content = new StringItem(null, null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	private String[] texts;
	private Media[] media;
	private int page = -1;
	
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

	public Dialog reset (String[] texts, Media[] media, String button1, String button2, LuaClosure callback, Displayable parent) {
		removeCommand(BTN1);
		removeCommand(BTN2);
		if (button1 == null) button1 = "OK";
		addCommand(BTN1 = new Command(button1, Command.SCREEN, 1));
		if (button2 != null) {
			addCommand(BTN2 = new Command(button2, Command.SCREEN, 2));
		}
		this.texts = texts;
		this.media = media;
		this.callback = callback;
		this.parent = parent;
		page = -1;
		nextPage();
		return this;
	}

	private void nextPage() {
		page++;
		if (page >= texts.length) {
			Midlet.push(parent);
			if (callback != null) Engine.invokeCallback(callback, "Button1");
			return;
		}
		Media m = media[page];
		if (m != null) try {
			byte[] is = Engine.mediaFile(m);
			image.setImage(Image.createImage(is, 0, is.length));
		} catch (Exception e) {
			image.setAltText(m.altText);
		} else {
			image.setImage(Midlet.NULL_IMAGE);
			image.setImage(null);
		}
		content.setText(texts[page]);

		Midlet.display.setCurrentItem(image);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == BTN1) {
			nextPage();
		} else if (cmd == BTN2) {
			Midlet.push(parent);
			if (callback != null) Engine.invokeCallback(callback, "Button2");
		}
	}

	public Displayable cancel() {
		if (callback != null) Engine.invokeCallback(callback, null);
		return parent;
	}
}
