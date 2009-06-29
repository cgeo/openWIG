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
	
	// option labels
	private static final String NAME_MANUAL = "Manual";
	private static final String NAME_BLUETOOTH = "Bluetooth";
	private static final String NAME_SERIAL = "Serial port";
	private static final String NAME_INTERNAL = "Internal";
	private static final String NAME_SOCKET = "TCP Socket";
	
	// COM port list
	private List comPorts = null;
	
	// TCP port number
	private TextBox tcpPort = null;
	
	private int gpstype;
	private String gpsBtUrl;
	private String gpsComPort;
	private boolean gpsDirty;
	
	private BluetoothOptions btOptions = null;
	
	private Command CMD_SAVE = new Command("Save", Command.SCREEN, 2);

	public Options () {
		super("Options");
		setItemStateListener(this);
		
		gpsType.append(NAME_MANUAL, null);
		
		try {
			btOptions = new BluetoothOptions();
			gpsType.append(NAME_BLUETOOTH, null);
		} catch (NoClassDefFoundError e) {
			btOptions = null;
		}
		
		String ports = System.getProperty("microedition.commports");
		if (ports != null && ports.length() > 0)
			gpsType.append(NAME_SERIAL, null);
		
		if (System.getProperty("microedition.location.version") != null)
			gpsType.append(NAME_INTERNAL, null);
		
		gpsType.append(NAME_SOCKET, null);
		
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
		
		tcpPort = new TextBox("Port number", null, 100, TextField.NUMERIC);
		tcpPort.setCommandListener(this);
		tcpPort.addCommand(CMD_SAVE);
		tcpPort.addCommand(Midlet.CMD_BACK);
		
		setCommandListener(this);
		addCommand(CMD_SAVE);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Item it) {
		if (it == gpsSelect) {
			switch (selectionToGpsType()) {
				
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
					
				case Midlet.GPS_SOCKET:
					Midlet.push(tcpPort);
					break;
			}
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			if (cmd == CMD_SAVE) {
				int newtype = selectionToGpsType();
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
			Midlet.push(Midlet.baseMenu);
			
		} else if (disp == comPorts) {
			if (cmd == Midlet.CMD_SELECT) {
				String com = comPorts.getString(comPorts.getSelectedIndex());
				Midlet.config.set(Config.GPS_SERIAL_PORT, com);
				gpsDevice.setText(com);
			}
			Midlet.push(this);

		} else if (disp == tcpPort) {
			if (cmd == CMD_SAVE) {
				Midlet.config.set(Config.GPS_TCP_PORT, tcpPort.getString());
				gpsDevice.setText(tcpPort.getString());
			}
			Midlet.push(this);
		}
	}
	
	public void push () {
		Midlet.show(this);
	}
	
	public Options reset () {
		refresh();
		return this;
	}

	private void refresh () {
		gpstype = Midlet.config.getInt(Config.GPS_TYPE);
		gpsBtUrl = Midlet.config.get(Config.GPS_BT_URL);
		gpsComPort = Midlet.config.get(Config.GPS_SERIAL_PORT);
		gpsDirty = false;
		setSelection(gpstype);
		itemStateChanged(gpsType);
		refreshInterval.setString(Midlet.config.get(Config.REFRESH_INTERVAL));
		checkboxes.setSelectedIndex(CHK_LOGGING, Midlet.config.getInt(Config.ENABLE_LOGGING) > 0);
		checkboxes.setSelectedIndex(CHK_SHOWFULL, Midlet.config.getInt(Config.CHOICE_SHOWFULL) > 0);
	}

	public void itemStateChanged(Item it) {
		if (it == gpsType) {
			switch (selectionToGpsType()) {
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
				case Midlet.GPS_SOCKET:
					gpsDevice.setLabel("Local port:");
					String addr = Midlet.config.get(Config.GPS_TCP_PORT);
					gpsDevice.setText((addr == null) ? "(not selected)" : addr);
					tcpPort.setString(addr);
					showDeviceSelection();
					break;
			}
		}
	}
	
	private int selectionToGpsType() {
		String s = gpsType.getString(gpsType.getSelectedIndex()).intern();
		if (s == NAME_MANUAL) return Midlet.GPS_MANUAL;
		if (s == NAME_SERIAL) return Midlet.GPS_SERIAL;
		if (s == NAME_BLUETOOTH) return Midlet.GPS_BLUETOOTH;
		if (s == NAME_INTERNAL) return Midlet.GPS_INTERNAL;
		if (s == NAME_SOCKET) return Midlet.GPS_SOCKET;
		return -1;
	}
	
	private void setSelection (int type) {
		String s = null;
		switch (type) {
			case Midlet.GPS_MANUAL:
				s = NAME_MANUAL; break;
			case Midlet.GPS_SERIAL:
				s = NAME_SERIAL; break;
			case Midlet.GPS_BLUETOOTH:
				s = NAME_BLUETOOTH; break;
			case Midlet.GPS_INTERNAL:
				s = NAME_INTERNAL; break;
			case Midlet.GPS_SOCKET:
				s = NAME_SOCKET; break;
		}
		if (s == null) return;
		for (int i = 0; i < gpsType.size(); i++) {
			if (s.equals(gpsType.getString(i))) {
				gpsType.setSelectedIndex(i, true);
				return;
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
