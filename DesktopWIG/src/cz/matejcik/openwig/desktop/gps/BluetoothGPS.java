package cz.matejcik.openwig.desktop.gps;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/** NMEAParser that knows Bluetooth and can connect to a given address. */
public class BluetoothGPS extends NMEAParser {
	
	private String bluetoothAddress = null;
	private StreamConnection bluetoothConnection;

	public BluetoothGPS () { }
	
	public BluetoothGPS (String address) {
		bluetoothAddress = address;
	}

	@Override
	public void connect () {
		if (bluetoothAddress == null) throw new IllegalStateException("no bluetooth device selected");
		super.connect();
	}

	/** called from NMEAParser's own thread, before reading starts */
	@Override
	protected void makeConnection () throws IOException {
		bluetoothConnection = (StreamConnection)Connector.open(bluetoothAddress);
		stream = bluetoothConnection.openInputStream();
		connected = true;
	}

	@Override
	public void disconnect () {
		super.disconnect();
		if (bluetoothConnection != null) try {
			bluetoothConnection.close();
		} catch (IOException e) { }
	}

	/**
	 * @return the bluetoothAddress
	 */
	public String getBluetoothAddress () {
		return bluetoothAddress;
	}

	/**
	 * @param bluetoothAddress the bluetoothAddress to set
	 */
	public void setBluetoothAddress (String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}
}
