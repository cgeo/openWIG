package gui;

import gwc.CartridgeFile;
import java.io.OutputStream;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.ZonePoint;
import util.Config;

public class CartridgeDetails extends Form implements CommandListener, Pushable, Runnable {
	
	private static final Command CMD_START = new Command("Start", Command.SCREEN, 0);
	private static final Command CMD_NAVIGATE = new Command("Navigate", Command.SCREEN, 1);
	
	private StringItem name = new StringItem(null, null);
	private StringItem description = new StringItem(null, null);
	private StringItem distance = new StringItem("Distance: ", null);
	private ImageItem image = new ImageItem(null, null, ImageItem.LAYOUT_DEFAULT, null);
	
	private CartridgeFile cartridge;
	private OutputStream logfile;
	private FileConnection savegame;
	private ZonePoint startPoint;
	
	public CartridgeDetails () {
		super("");
		name.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		image.setLayout(Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_CENTER);
		description.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_LEFT);
		
		append(name);
		append(image);
		append(description);
		append(distance);
		
		setCommandListener(this);
		addCommand(CMD_START);
		addCommand(CMD_NAVIGATE);
		addCommand(Midlet.CMD_BACK);
	}
	
	public CartridgeDetails reset (CartridgeFile what, OutputStream log, FileConnection save) {
		setTitle(what.name);
		cartridge = what;
		logfile = log;
		savegame = save;
		startPoint = new ZonePoint(cartridge.latitude, cartridge.longitude, 0);
		
		name.setText(cartridge.name);
		description.setText(Engine.removeHtml(cartridge.description));
		
		try {
			byte[] is = cartridge.getFile(cartridge.splashId);
			Image i = Image.createImage(is, 0, is.length);
			image.setImage(i);
		} catch (Exception e) { }
		
		return this;
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == CMD_START) {
			stop();
			Midlet.loadCartridge(cartridge, logfile, savegame);
		} else if (cmd == CMD_NAVIGATE) {
			Midlet.push(Midlet.navigation.reset(this, startPoint));
		} else if (cmd == Midlet.CMD_BACK) {
			stop();
			Midlet.push(Midlet.browser);
		}
	}
	
	private void updateNavi () {
		distance.setText(startPoint.friendlyDistance(Midlet.gps.getLatitude(), Midlet.gps.getLongitude()));
	}
	
	private Thread thread = null;
	synchronized private void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	synchronized private void stop() {
		thread = null;
	}
	
	public void run () {
		while (thread == Thread.currentThread()) {
			try { Thread.sleep(Midlet.config.getInt(Config.REFRESH_INTERVAL) * 1000); } catch (Exception e) { }
			updateNavi();
		}
	}
	
	public void push () {
		updateNavi();
		start();
		Midlet.show(this);
		Midlet.display.setCurrentItem(name);
	}
}
