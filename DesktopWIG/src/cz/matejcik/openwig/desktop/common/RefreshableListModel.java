package cz.matejcik.openwig.desktop.common;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractListModel;

/** List model that is operated like a regular collection,
 * but can fire the ContentsChanged event at will.
 * This is necessary for Wherigo, because we often have items
 * in lists that render differently on each pass.
 * @param <T> type of contained items
 */
public class RefreshableListModel<T> extends AbstractListModel {

	private ArrayList<T> contents = new ArrayList<T>();

	public int getSize () {
		return contents.size();
	}

	public Object getElementAt (int index) {
		return contents.get(index);
	}

	public void add (T thing) {
		contents.add(thing);
		fireIntervalAdded(this, contents.size()-1, contents.size()-1);
	}

	public void remove (int i) {
		contents.remove(i);
		fireIntervalRemoved(this, i, i);
	}

	public void clear () {
		if (!contents.isEmpty()) {
			int size = contents.size();
			contents.clear();
			fireIntervalRemoved(this, 0, size - 1);
		}
	}

	public void add (int i, T thing) {
		contents.add(i, thing);
		fireIntervalAdded(this, i, i);
	}

	public void addAll (Collection<? extends T> stuff) {
		contents.addAll(stuff);
		if (!contents.isEmpty()) fireIntervalAdded(this, 0, contents.size() - 1);
	}

	public void refresh () {
		fireContentsChanged(this, 0, contents.size() - 1);
	}
}
