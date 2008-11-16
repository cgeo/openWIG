package gui;

import javax.microedition.lcdui.*;
import util.BluetoothOptions;
import util.Config;

public class Options extends Form implements Pushable, CommandListener,
	ItemCommandListener, ItemStateListener {
	
	// navigation refresh interval
	private TextField refreshInterval = new TextField("Refresh interval:", null, 100, TextField.NUMERIC);
	
	// gps type
	private ChoiceGroup gpsType = new ChoiceGroup("GPS type", Choice.EXCLUSIVE);
	public StringItem gpsDevice = new StringItem("Device:", "...");
	private StringItem gpsSelect = new StringItem(null, "Choose...", StringItem.HYPERLINK);
	
	// checkboxes
	private ChoiceGroup checkboxes = new ChoiceGroup(null, Choice.MULTIPLE);
	private static final int CHK_LOGGING = 0;
	private static final int CHK_SHOWFULL = 1;
	
	// COM port list
	private List comPorts = null;
	
	private int gpstype;
	private String gpsBtUrl;
	private String gpsComPort;
	private boolean gpsDirty;
	
	private BluetoothOptions btOptions = null;
	
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
		
		checkboxes.append("Enable logging", null);
		checkboxes.append("\"Show Full\" command", null);
		append(checkboxes);
		
		comPorts = new List("Serial ports", List.IMPLICIT);
		comPorts.setCommandListener(this);
		comPorts.setSelectCommand(Midlet.CMD_SELECT);
		comPorts.addCommand(Midlet.CMD_BACK);
		
		setCommandListener(this);
		addCommand(CMD_SAVE);
		addCommand(Midlet.CMD_BACK);
		
		try {
			btOptions = new BluetoothOptions();
		} catch (NoClassDefFoundError e) {
			btOptions = null;
		}
	}

	public void commandAction(Command cmd, Item it) {
		if (it == gpsSelect) {
			switch (gpsType.getSelectedIndex()) {
				
				case Midlet.GPS_SERIAL:
					String ports = System.getProperty("microedition.commports");
					if (ports == null || ports.length() == 0) {
						Midlet.display.setCurrent(new Alert("error","No serial ports found!",null,AlertType.ERROR));
					} else {
						comPorts.deleteAll();
						int pos = 0, ppos = 0;
						while ((pos = ports.indexOf(',', ppos)) != -1) {
							comPorts.append(ports.substring(ppos, pos),null);
							ppos = pos + 1;
						}
						comPorts.append(ports.substring(ppos), null);
						Midlet.push(comPorts);
					}
					break;
					
				case Midlet.GPS_BLUETOOTH:
					if (btOptions != null) {
						btOptions.startInquiry();
					} else {
						Midlet.display.setCurrent(new Alert("error","Your device won't let me use bluetooth!",null,AlertType.ERROR));
					}
					break;
			}
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_SAVE) {
				int newtype = gpsType.getSelectedIndex();
				// TODO check valid parameters!
				if (gpstype != newtype) gpsDirty = true;
				Midlet.config.set(Config.GPS_TYPE, String.valueOf(newtype));
				Midlet.config.set(Config.REFRESH_INTERVAL, refreshInterval.getString());
				boolean[] flags = new boolean[checkboxes.size()];
				checkboxes.getSelectedFlags(flags);
				Midlet.config.set(Config.ENABLE_LOGGING, flags[CHK_LOGGING] ? "1" : "0");
				Midlet.config.set(Config.CHOICE_SHOWFULL, flags[CHK_SHOWFULL] ? "1" : "0");
				if (gpsDirty) {
					// TODO offer reconnection
				}
				Midlet.resetGps();
			}
			Midlet.pop(this);
			
		} else if (disp == comPorts) {
			if (cmd == Midlet.CMD_SELECT) {
				String com = comPorts.getString(comPorts.getSelectedIndex());
				Midlet.config.set(Config.GPS_SERIAL_PORT, com);
				gpsDevice.setText(com);
			}
			Midlet.pop(comPorts);
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
		checkboxes.setSelectedIndex(CHK_LOGGING, Midlet.config.getInt(Config.ENABLE_LOGGING) > 0);
		checkboxes.setSelectedIndex(CHK_SHOWFULL, Midlet.config.getInt(Config.CHOICE_SHOWFULL) > 0);
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
