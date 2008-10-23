package gui;

import javax.microedition.lcdui.*;
import util.Config;

public class Options extends Form implements Pushable, CommandListener,
	ItemCommandListener, ItemStateListener {
	
	// navigation refresh interval
	private TextField refreshInterval = new TextField("Refresh interval:", null, 100, TextField.NUMERIC);
	
	// gps type
	private ChoiceGroup gpsType = new ChoiceGroup("GPS type", Choice.EXCLUSIVE);
	private StringItem gpsDevice = new StringItem("Device:", "...");
	private StringItem gpsSelect = new StringItem(null, "Choose...", StringItem.HYPERLINK);
	
	// COM port list
	private List comPorts = null;
	
	private int gpstype;
	private String gpsBtUrl;
	private String gpsComPort;
	private boolean gpsDirty;
	
	private Command CMD_SAVE = new Command("Save", Command.SCREEN, 2);

	public Options () {
		super("Options");
		setItemStateListener(this);
		
		gpsType.append("Manual", null);
		gpsType.append("Serial port", null);
		gpsType.append("Bluetooth", null);
		gpsType.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		append(gpsType);
		
		append(gpsDevice);
		gpsSelect.setLayout(Item.LAYOUT_NEWLINE_AFTER);
		gpsSelect.setDefaultCommand(new Command("Choose", Command.ITEM, 1));
		gpsSelect.setItemCommandListener(this);
		append(gpsSelect);
		
		append(refreshInterval);
		
		setCommandListener(this);
		addCommand(CMD_SAVE);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Item it) {
		if (it == gpsSelect) {
			Midlet.display.setCurrent(new Alert("ahoj!!"), this);
		}
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
		gpsBtUrl = Midlet.config.get(Config.GPS_BT_URL);
		gpsComPort = Midlet.config.get(Config.GPS_SERIAL_PORT);
		gpsDirty = false;
		gpsType.setSelectedIndex(gpstype, true);
		itemStateChanged(gpsType);
		refreshInterval.setString(Midlet.config.get(Config.REFRESH_INTERVAL));
	}

	public void itemStateChanged(Item it) {
		if (it == gpsType) {
			switch (gpsType.getSelectedIndex()) {
				case Midlet.GPS_MANUAL: case Midlet.GPS_INTERNAL:
					hideDeviceSelection();
					break;
				case Midlet.GPS_BLUETOOTH:
					gpsDevice.setLabel("Device:");
					String dev = Midlet.config.get(Config.GPS_BT_NAME);
					gpsDevice.setText((dev == null) ? "(not selected)" : dev);
					showDeviceSelection();
					break;
				case Midlet.GPS_SERIAL:
					gpsDevice.setLabel("Port:");
					String port = Midlet.config.get(Config.GPS_SERIAL_PORT);
					gpsDevice.setText((port == null) ? "(not selected)" : port);
					showDeviceSelection();
					break;
			}
		}
	}
	
	private void hideDeviceSelection () {
		if (get(1) == gpsDevice) {
			delete(1);
			delete(1);
		}
	}
	
	private void showDeviceSelection () {
		if (get(1) != gpsDevice) {
			insert(1, gpsDevice);
			insert(2, gpsSelect);
		}
	}
}
