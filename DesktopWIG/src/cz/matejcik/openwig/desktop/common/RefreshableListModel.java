package cz.matejcik.openwig.desktop.common;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;

/** List model that is operated like a regular collection,
 * but can fire the ContentsChanged event at will.
 * This is necessary for Wherigo, because we often have items
 * in lists that render differently on each pass.
 * @param <T> type of contained items
 */
public class RefreshableListModel<T> extends AbstractListModel {

	private ArrayList<T> contents = new ArrayList<T>();
	private ArrayList<T> newcontents = new ArrayList<T>();

	/* Contract methods */
	@Override
	public int getSize () {
		return contents.size();
	}

	@Override
	public Object getElementAt (int index) {
		return contents.get(index);
	}

	/* manipulation methods */
	public void add (final T thing) {
		newcontents.add(thing);
	}

	public void remove (final int i) {
		newcontents.remove(i);
	}

	public void clear () {
		newcontents.clear();
	}
	
	public void update (final Collection<? extends T> stuff) {
		newcontents.clear();
		newcontents.addAll(stuff);
	}

	public void add (final int i, final T thing) {
		newcontents.add(i, thing);
	}

	public void addAll (final Collection<? extends T> stuff) {
		newcontents.addAll(stuff);
	}

	/** Refresh on-display collection
	 * 
	 * copies <code>newcontents</code> to <code>contents</code>, generating
	 * content change events as it goes
	 */
	synchronized public void refresh () {
		int bigger = Math.max(contents.size(), newcontents.size());
		int smaller = Math.min(contents.size(), newcontents.size());
		int orig = contents.size();
		contents.clear();
		contents.addAll(newcontents);

		if (smaller > 0) fireContentsChanged(this, 0, smaller - 1);
		if (bigger == smaller) return;
		if (orig == smaller) fireIntervalAdded(this, smaller, bigger - 1);
		else fireIntervalRemoved(this, smaller, bigger - 1);
	}
	
	public void refreshLater () {
		SwingUtilities.invokeLater(new Runnable() { public void run () {
			refresh();
		}});
	}
}
