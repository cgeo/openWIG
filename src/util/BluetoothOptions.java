package util;

import javax.bluetooth.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import net.benhui.btgallery.bluelet.BLUElet;
import gui.Midlet;
import java.io.IOException;
import javax.microedition.lcdui.CommandListener;

public class BluetoothOptions implements CommandListener {

	private BLUElet bluelet;

	public void startInquiry() {
		bluelet = new BLUElet(Midlet.instance, this);
		bluelet.startApp();
		Midlet.push(bluelet.getUI());
		bluelet.startInquiry(DiscoveryAgent.GIAC, new UUID[]{new UUID(0x1101)});
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (bluelet != null && disp == bluelet.getUI()) {
			if (cmd == BLUElet.SELECTED) {
				// do nothing
			} else if (cmd == BLUElet.COMPLETED) {
				Midlet.pop(bluelet.getUI());				
				ServiceRecord serviceRecord = bluelet.getFirstDiscoveredService();
				bluelet = null;
				String name;
				try {
					name = serviceRecord.getHostDevice().getFriendlyName(false);
				} catch (IOException e) {
					name = "(name scan failed)";
				}
				String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
				Midlet.config.set(Config.GPS_BT_NAME, name);
				Midlet.config.set(Config.GPS_BT_URL, url);
				// XXX this breaks "Save/Back" logic
				Midlet.options.gpsDevice.setText(name);
			} else if (cmd == BLUElet.BACK) {
				Midlet.pop(bluelet.getUI());
				bluelet = null;
			}
		}
	}
}
