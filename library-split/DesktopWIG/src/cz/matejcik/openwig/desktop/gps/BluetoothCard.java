package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.desktop.common.SwingHelpers;
import cz.matejcik.openwig.platform.LocationService;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.prefs.Preferences;
import javax.bluetooth.*;
import javax.swing.*;

/** Bluetooth configuration panel */
public class BluetoothCard implements GPSManager.Card, ActionListener, NMEAParser.Listener {
	
	private JComboBox devices;
	private ArrayList<String> names;
	private ArrayList<String> addresses;
	private JButton btnRescan, btnConnect, btnDisconnect;
	private JCheckBox autoconnect = new JCheckBox("connect at startup");

	private BluetoothGPS gps = new BluetoothGPS();
	private boolean btSearching = false;

	private Component panel;

	public BluetoothCard () {
		gps.setListener(this);
		panel = makeBluetoothPanel();
		loadSettings();
	}

	private Box makeBluetoothPanel () {
		Box box = Box.createVerticalBox();

		Box line = Box.createHorizontalBox();
		box.add(line);

		devices = new JComboBox(new String[] {"(none)"});
		devices.addActionListener(this);
		
		SwingHelpers.setPreferredWidth(devices, 300);
		SwingHelpers.limitMaximumHeight(devices);
		line.add(devices);
		line.add(Box.createRigidArea(new Dimension(5,5)));
		btnRescan = new JButton("Search");
		btnRescan.addActionListener(this);
		line.add(btnRescan);

		JPanel p = new JPanel();
		box.add(p);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(this);
		p.add(btnConnect);
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setEnabled(false);
		btnDisconnect.addActionListener(this);
		p.add(btnDisconnect);

		box.add(autoconnect);

		return box;
	}

	/** Synchronizes selection in device combo box with configuration
	 * of our <code>LocationService</code> object.
	 */
	private void updateBluetoothSelection () {
		if (btSearching) return;
		if (addresses.isEmpty()) {
			gps.setBluetoothAddress(null);
		} else {
			int i = devices.getSelectedIndex();
			if (i < 0) i = 0;
			String address = addresses.get(i);
			gps.setBluetoothAddress(address);
		}
	}

	/** Updates the device combo box with newly discovered devices. */
	private Runnable updateBluetoothList = new Runnable() {
		public void run () {
			devices.removeAllItems();
			if (names.isEmpty()) {
				devices.addItem("(none)");
			} else for (String name : names) {
				devices.addItem(name);
				btnConnect.setEnabled(true);
			}
			btnRescan.setEnabled(true);
			btSearching = false;
		}
	};

	private static final UUID[] BT_SERIAL_PORT = { new UUID(0x1101) };

	/** Initiates bluetooth scan.
	 * Searches for devices that support the Serial Port profile,
	 * identifies their names and prepares data that can be fed into
	 * the devices combo.
	 */
	private void scanBluetoothDevices () {
		try {
			btSearching = true;
			LocalDevice device = LocalDevice.getLocalDevice();
			final DiscoveryAgent agent = device.getDiscoveryAgent();

			devices.removeAllItems();
			devices.addItem("(searching...)");
			btnRescan.setEnabled(false);
			btnConnect.setEnabled(false);

			System.err.println("starting inquiry...");
			agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
				private ArrayList<RemoteDevice> devices = new ArrayList<RemoteDevice>();
				private Iterator<RemoteDevice> iterator;
				private RemoteDevice latestDevice;
				private String latestServiceString;

				// this happens first
				public void deviceDiscovered (RemoteDevice rd, DeviceClass dc) {
					devices.add(rd);
				}

				// this happens third, after we start searching for services
				public void servicesDiscovered (int transId, ServiceRecord[] srs) {
					System.err.println("services discovered");
					if (srs.length > 0) latestServiceString = srs[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
				}

				// this happens fourth, when service scan for a particular device is complete
				// so fire the next one
				public void serviceSearchCompleted (int transId, int status) {
					System.err.println("service search completed: "+status);
					if (status == SERVICE_SEARCH_COMPLETED && latestServiceString != null) {
						try {
							names.add(latestDevice.getFriendlyName(false));
						} catch (IOException e) {
							names.add(latestDevice.getBluetoothAddress());
						}
						addresses.add(latestServiceString);
					}
					processNextDevice();
				}

				// this happens second; prepare iterator and fire service search requests one after other
				public void inquiryCompleted (int status) {
					System.err.println("inquiry completed: "+status);
					if (status == DiscoveryListener.INQUIRY_COMPLETED) {
						iterator = devices.iterator();
						names = new ArrayList<String>(devices.size());
						addresses = new ArrayList<String>(devices.size());
						processNextDevice();
					} else {
						JOptionPane.showMessageDialog(GPSManager.getInstance(), "Scanning failed (i don't know why). Maybe try again?",
							"Bluetooth Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				// fire the service search request
				private void processNextDevice() {
					if (iterator.hasNext()) {
						System.err.print("processing next device: ");
						try {
							RemoteDevice rd = iterator.next();
							System.err.println(rd);
							latestDevice = rd;
							latestServiceString = null;
							agent.searchServices(null, BT_SERIAL_PORT, rd, this);
						} catch (Exception e) {
							e.printStackTrace();
							processNextDevice();
						}
					} else {
						System.err.println("processing finished");
						SwingUtilities.invokeLater(updateBluetoothList);
					}
				}
			});
		} catch (BluetoothStateException e) {
			btSearching = false;
			JOptionPane.showMessageDialog(GPSManager.getInstance(), "Scanning failed: "+e.getLocalizedMessage(),
				"Bluetooth Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public Component getCard () {
		return panel;
	}

	public String getLabel () {
		return "Bluetooth";
	}

	public String getKey () {
		return "bluetooth";
	}

	public void loadSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		boolean connect = prefs.getBoolean("autoconnect", false);
		autoconnect.setSelected(connect);
		String name = prefs.get("name", null);
		if (name != null) {
			// assume that address is there
			String address = prefs.get("address", null);
			names = new ArrayList<String>();
			names.add(name);
			addresses = new ArrayList<String>();
			addresses.add(address);
			updateBluetoothList.run();
		}

	}

	public void saveSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		if (!addresses.isEmpty()) {
			int i = devices.getSelectedIndex();
			prefs.put("name", names.get(i));
			prefs.put("address", addresses.get(i));
		}
		prefs.putBoolean("autoconnect", autoconnect.isSelected());
	}

	public LocationService getGPS () {
		return gps;
	}

	public boolean autoconnect () {
		return autoconnect.isSelected();
	}

	public void actionPerformed (ActionEvent e) {
		Object src = e.getSource();
		if (src == btnRescan) {
			scanBluetoothDevices();
		} else if (src == devices) {
			updateBluetoothSelection();
		} else if (src == btnConnect) {
			updateBluetoothSelection();
			gps.connect();
		} else if (src == btnDisconnect) {
			gps.disconnect();
		}
	}

	public void statusChanged (NMEAParser source, int event) {
		if (event == NMEAParser.Listener.CONNECTING) {
			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
		} else if (event == NMEAParser.Listener.DISCONNECTED) {
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
		}
	}
}
