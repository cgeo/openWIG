package gui;

import java.util.Vector;
import javax.microedition.lcdui.*;

abstract public class ListOfStuff extends List implements Pushable, CommandListener {
	
	Vector stuff = new Vector();
	
	public ListOfStuff (String title) {
		super(title, List.IMPLICIT);
		setSelectCommand(Midlet.CMD_SELECT);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}
	
	abstract protected void callStuff (Object what);
	abstract protected boolean stillValid ();
	abstract protected Vector getValidStuff();
	abstract protected String getStuffName (Object what);
	protected Image getStuffIcon (Object what) { return null; }
	
	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_SELECT) {
			Object s = null;
			synchronized (this) {
				int index = getSelectedIndex();
				if (index >= 0 && index < stuff.size()) {
					s = stuff.elementAt(index);
				}
			}
			if (s != null) callStuff(s);
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop(this);
		}
	}

	public void prepare() {
		if (! stillValid()) {
			Midlet.pop(this);
			return;
		}
		
		Vector newstuff = getValidStuff();
		synchronized (this) {
			// first, validate the stuff already in there
			for (int i = 0; i < stuff.size(); i++) {
				Object s = stuff.elementAt(i);
				int in = newstuff.indexOf(s);
				if (in == -1) {
					stuff.removeElementAt(i);
					delete(i);
					i--;
				} else {
					set(i, getStuffName(s), getStuffIcon(s));
					newstuff.setElementAt(null, in);
				}
			}
			// then, add the rest
			for (int i = 0; i < newstuff.size(); i++) {
				Object s = newstuff.elementAt(i);
				if (s != null) {
					append(getStuffName(s), getStuffIcon(s));
					stuff.addElement(s);
				}
			}
		}
	}
}
