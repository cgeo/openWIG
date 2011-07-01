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
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				contents.add(thing);
				fireIntervalAdded(this, contents.size() - 1, contents.size() - 1);
			}
		});
	}

	public void remove (final int i) {
				SwingUtilities.invokeLater(new Runnable() {
			public void run () {

		contents.remove(i);
		fireIntervalRemoved(this, i, i);
					}
		});
	}

	public void clear () {
				SwingUtilities.invokeLater(new Runnable() {
			public void run () {
		if (!contents.isEmpty()) {
			int size = contents.size();
			contents.clear();
			fireIntervalRemoved(this, 0, size - 1);
		}
					}
		});
	}

	public void add (final int i, final T thing) {
				SwingUtilities.invokeLater(new Runnable() {
			public void run () {
		contents.add(i, thing);
		fireIntervalAdded(this, i, i);
					}
		});
	}

	public void addAll (final Collection<? extends T> stuff) {
				SwingUtilities.invokeLater(new Runnable() {
			public void run () {
		contents.addAll(stuff);
		if (!contents.isEmpty()) fireIntervalAdded(this, 0, contents.size() - 1);
					}
		});
	}

	public void refresh () {
				SwingUtilities.invokeLater(new Runnable() {
			public void run () {
		fireContentsChanged(this, 0, contents.size() - 1);
					}
		});
	}
}
