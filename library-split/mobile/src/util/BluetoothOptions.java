package util;

import javax.bluetooth.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import net.benhui.btgallery.bluelet.BLUElet;
import gui.Midlet;
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
				Midlet.push(Midlet.options);
				int ret = bluelet.getServiceDiscoveryReturnCode();
				String name = bluelet.getSelectedDeviceName();
				String url;
				if (ret != DiscoveryListener.SERVICE_SEARCH_COMPLETED) {
					if (ret == DiscoveryListener.SERVICE_SEARCH_ERROR) {
						// assume the searcher or device is broken
						url = "btspp://" + bluelet.getSelectedDevice().getBluetoothAddress() + ":1";
					} else {
						String err;
						switch (ret) {
							case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
								err = "could not connect to device";
								break;
							case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS:
								err = "device doesn't seem to be a GPS";
								break;
							default:
								err = "unknown error " + ret +"\nPlease report this as a bug.";
						}
						Midlet.error(err);
						bluelet = null;
						return;
					}
				} else {
					ServiceRecord serviceRecord = bluelet.getFirstDiscoveredService();
					bluelet = null;
					url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
				}
				Midlet.config.set(Config.GPS_BT_NAME, name);
				Midlet.config.set(Config.GPS_BT_URL, url);
				// XXX this breaks "Save/Back" logic
				Midlet.options.gpsDevice.setText(name);
			} else if (cmd == BLUElet.BACK) {
				Midlet.push(Midlet.options);
				bluelet = null;
			}
		} else {
			Midlet.push(Midlet.options);
		}
	}
}
