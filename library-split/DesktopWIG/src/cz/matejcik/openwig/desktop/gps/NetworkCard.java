package cz.matejcik.openwig.desktop.gps;

import cz.matejcik.openwig.desktop.common.SwingHelpers;
import cz.matejcik.openwig.platform.LocationService;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.*;

/** Bluetooth configuration panel */
public class NetworkCard implements GPSManager.Card, ActionListener, NMEAParser.Listener {
	
	private JButton btnConnect, btnDisconnect;
	private JCheckBox autoconnect = new JCheckBox("connect at startup");
	private JTextField txtPort = new JTextField();

	private NetworkGPS gps = new NetworkGPS();

	private Component panel;

	public NetworkCard () {
		gps.setListener(this);
		panel = makeNetworkPanel();
		loadSettings();
	}

	private Box makeNetworkPanel () {
		Box box = Box.createVerticalBox();

		Box line = Box.createHorizontalBox();
		box.add(line);
		
		line.add(new JLabel("TCP Port:"));
		SwingHelpers.setPreferredWidth(txtPort, 300);
		line.add(txtPort);
		line.add(Box.createRigidArea(new Dimension(5,5)));

		JPanel p = new JPanel();
		box.add(p);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(true);
		btnConnect.addActionListener(this);
		p.add(btnConnect);
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setEnabled(false);
		btnDisconnect.addActionListener(this);
		p.add(btnDisconnect);

		box.add(autoconnect);

		return box;
	}

	public Component getCard () {
		return panel;
	}

	public String getLabel () {
		return "TCP Socket";
	}

	public String getKey () {
		return "network";
	}

	public void loadSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		boolean connect = prefs.getBoolean("autoconnect", false);
		autoconnect.setSelected(connect);
		String port = prefs.get("port", null);
		txtPort.setText(port);
//		updateListener();
	}

	public void saveSettings () {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		prefs.putBoolean("autoconnect", autoconnect.isSelected());
		prefs.put("port", txtPort.getText());
	}

	public LocationService getGPS () {
		return gps;
	}

	public boolean autoconnect () {
		return autoconnect.isSelected();
	}

	public void actionPerformed (ActionEvent e) {
		Object src = e.getSource();
		if (src == btnConnect) {
			try {
				updateListener();
				gps.connect();
			} catch (NumberFormatException f) {
				JOptionPane.showMessageDialog(panel, "invalid port number", "error", JOptionPane.ERROR_MESSAGE);
			}
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

	private void updateListener() throws NumberFormatException {
		String txt = txtPort.getText();
		int port = Integer.parseInt(txt);
		gps.setPort(port);
	}
}
