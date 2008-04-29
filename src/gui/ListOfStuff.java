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
	
	synchronized public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_SELECT) {
			int index = getSelectedIndex();
			if (index >= 0 && index < stuff.size()) {
				callStuff(stuff.elementAt(index));
			}
		} else if (cmd == Midlet.CMD_BACK) {
			Midlet.pop(this);
		}
	}

	synchronized public void prepare() {
		if (! stillValid()) {
			Midlet.pop(this);
			return;
		}
		
		Vector newstuff = getValidStuff();
		
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
		
		// **** the old method - delete all, put it back
		/*int index = getSelectedIndex();
		deleteAll();
		stuff = newstuff;
		for (int i = 0; i < stuff.size(); i++) {
			append(getStuffName(stuff.elementAt(i)),getStuffIcon(stuff.elementAt(i)));
		}
		if (index >= 0 && index < stuff.size()) setSelectedIndex(index, true);*/
		
		// **** the fancy new method - do it slowly & gracefully
		/*int sp = 0;
		while (sp < stuff.size() && sp < newstuff.size()) {
			int i = newstuff.indexOf(stuff.elementAt(sp),sp);
			if (i == -1) {
				stuff.removeElementAt(sp);
				delete(sp);
			} else if (i == sp) {
				Object s = newstuff.elementAt(sp);
				set(sp, getStuffName(s), getStuffIcon(s));
				sp++;
			} else {
				int index = getSelectedIndex();
				if (index <= sp) index += (i - sp);
				while (i > sp) {
					Object s = newstuff.elementAt(sp);
					stuff.insertElementAt(s, sp);
					insert(sp, getStuffName(s), getStuffIcon(s));
					sp++;
				}
				setSelectedIndex(index, true);
			}
		}
		while (sp < stuff.size()) {
			stuff.removeElementAt(sp);
			delete(sp);
		}
		while (sp < newstuff.size()) {
			Object s = newstuff.elementAt(sp);
			stuff.addElement(s);
			append(getStuffName(s), getStuffIcon(s));
			sp++;
		}*/
	}
}
