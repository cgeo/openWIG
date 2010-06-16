package cz.matejcik.openwig.desktop.gps;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkGPS extends NMEAParser {

	private Socket socket = null;
	private int port = 0;

	@Override
	public void connect () {
		if (port == 0) throw new IllegalStateException("invalid port number");
		super.connect();
	}

	/** called from NMEAParser's own thread, before reading starts */
	@Override
	protected void makeConnection () throws IOException {
		socket = new Socket(InetAddress.getLocalHost(), port);
		stream = socket.getInputStream();
		connected = true;
	}

	@Override
	public void disconnect () {
		super.disconnect();
		if (socket != null) try {
			socket.close();
		} catch (IOException e) { }
		socket = null;
	}

	/**
	 * @return the port
	 */
	public int getPort () {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort (int port) {
		this.port = port;
	}


}
