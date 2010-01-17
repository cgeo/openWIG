package gui;

import java.util.Vector;
import openwig.Engine;
import openwig.Zone;

public class Zones extends ListOfStuff {
	
	public Zones () {
		super("Locations");
	}

	protected void callStuff(Object what) {
		Midlet.push(Midlet.details.reset((Zone)what, this));
	}

	protected boolean stillValid() {
		return true;
	}

	protected Vector getValidStuff() {
		Vector ret = new Vector();
		Vector v = Engine.instance.cartridge.zones;
		for (int i = 0; i < v.size(); i++) {
			Zone z = (Zone)v.elementAt(i);
			if (z.isVisible()) ret.addElement(z);
		}
		return ret;
	}

	protected String getStuffName(Object what) {
		return ((Zone)what).name;
	}
}
