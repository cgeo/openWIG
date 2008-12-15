package gui;

import gwc.CartridgeFile;
import java.io.OutputStream;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.ZonePoint;
import util.Config;

public class CartridgeDetails extends Form implements CommandListener, Runnable {
	
	private static final Command CMD_START = new Command("Start", Command.SCREEN, 0);
	private static final Command CMD_NAVIGATE = new Command("Navigate", Command.SCREEN, 1);
	
	private StringItem name = new StringItem(null, null);
	private StringItem description = new StringItem(null, null);
	private StringItem distance = new StringItem("Distance: ", null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	
	private CartridgeFile cartridge;
	private OutputStream logfile;
	private ZonePoint startPoint;
	
	public CartridgeDetails (CartridgeFile what, OutputStream log) {
		super(what.name);
		cartridge = what;
		logfile = log;
		startPoint = new ZonePoint(cartridge.latitude, cartridge.longitude, 0);
		name.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		image.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		description.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		
		name.setLabel(cartridge.name);
		description.setText(Engine.removeHtml(cartridge.description));
		
		append(name);
		append(image);
		append(description);
		append(distance);
		
		setCommandListener(this);
		addCommand(CMD_START);
		addCommand(CMD_NAVIGATE);
		addCommand(Midlet.CMD_BACK);
		
		try {
			byte[] is = cartridge.getFile(cartridge.splashId);
			Image i = Image.createImage(is, 0, is.length);
			image.setImage(i);
		} catch (Exception e) { }
		
		updateNavi();
		start();
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_START) {
			Midlet.loadCartridge(cartridge, logfile);
			stop();
		} else if (cmd == CMD_NAVIGATE) {
			Midlet.push(new Navigation(startPoint));
		} else if (cmd == Midlet.CMD_BACK) {
			stop();
			Midlet.pop(this);
			Midlet.browser.prepare();
		}
	}
	
	private void updateNavi () {
		distance.setText(startPoint.friendlyDistance(Midlet.gps.getLatitude(), Midlet.gps.getLongitude()));
	}
	
	private boolean running = false;
	synchronized private void start() {
		if (running) return;
		running = true;
		new Thread(this).start();
	}
	
	synchronized private void stop() {
		running = false;
	}
	
	public void run () {
		while (running) {
			try { Thread.sleep(Midlet.config.getInt(Config.REFRESH_INTERVAL) * 1000); } catch (Exception e) { }
			updateNavi();
		}
	}
	
}
