package gui;

import javax.microedition.lcdui.*;
import util.Config;

public class Options extends Form implements Pushable, CommandListener, ItemCommandListener {
	
	// navigation refresh interval
	private TextField refreshInterval = new TextField("Refresh interval:", null, 100, TextField.NUMERIC);
	
	// gps type
	private ChoiceGroup gpsType = new ChoiceGroup("GPS type", Choice.EXCLUSIVE);
	private StringItem gpsSelect = new StringItem(null, "Choose GPS device...", StringItem.HYPERLINK);
	
	// COM port list
	private List comPorts = null;
	
	private int gpstype;
	private String gpsUrl;
	private boolean gpsDirty;
	
	private Command CMD_SAVE = new Command("Save", Command.SCREEN, 1);

	public Options () {
		super("Options");
		gpsType.append("Manual", null);
		gpsType.append("Serial port", null);
		gpsType.append("Bluetooth", null);
		gpsType.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		append(gpsType);
		
		gpsSelect.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		gpsSelect.setDefaultCommand(new Command("Choose", Command.ITEM, 0));
		gpsSelect.setItemCommandListener(this);
		append(gpsSelect);
		
		append(refreshInterval);
		
		setCommandListener(this);
		addCommand(CMD_SAVE);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Item it) {
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_SAVE) {
				int newtype = gpsType.getSelectedIndex();
				if (gpstype != newtype) gpsDirty = true;
				Midlet.config.set(Config.GPS_TYPE, String.valueOf(newtype));
				Midlet.config.set(Config.REFRESH_INTERVAL, refreshInterval.getString());
				if (gpsDirty) {
					// alert
					return;
				}
			}
			Midlet.pop(this);
		}
	}

	public void prepare() {
		gpstype = Midlet.config.getInt(Config.GPS_TYPE);
		gpsUrl = Midlet.config.get(Config.GPS_URL);
		gpsDirty = false;
		gpsType.setSelectedIndex(gpstype, true);
		refreshInterval.setString(Midlet.config.get(Config.REFRESH_INTERVAL));
	}

}
