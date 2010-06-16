package cz.matejcik.openwig.desktop.common;

import cz.matejcik.openwig.desktop.Main;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


/** Pretty list for use in Wherigo UI
 * <p>
 * This list keeps its width fixed on a reasonable value
 * and resizes with its ScrollPane, preventing horizontal scrolling.
 * <p>
 * It also generates <code>onClick</code> events for items that really
 * react to mouse click and not only to selection changes.
 */
abstract public class WigList extends JList {

	/** Creates a WigList instance.
	 * Sets cell renderer to our pretty {@link ListItemRenderer},
	 * limits selection to one item and installs a mouse listener
	 * to detect clicks on items.
	 */
	public WigList () {
		setCellRenderer(new ListItemRenderer());
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MouseListener ml = new MouseAdapter() {
			@Override
			public void mousePressed (MouseEvent e) {
				int idx = locationToIndex(e.getPoint());
				if (idx != -1) {
					onClick(idx, getModel().getElementAt(idx));
				}
			}
		};
		addMouseListener(ml);
	}

	/** Returns the list wrapped in a <code>JScrollPane</code> so that
	 * it can be directly inserted where we need it.
	 * @return a <code>JScrollPane</code> that wraps this list
	 */
	public Component getScrollable () {
		JScrollPane pane = new JScrollPane(this);
		pane.setPreferredSize(Main.ITEM_DIMENSION);
		return pane;
	}

	@Override
	public boolean getScrollableTracksViewportWidth () {
		return true;
	}

	/** A specified item was clicked.
	 *
	 * Subclasses should override this to provide click functionality.
	 * @param id id of item in list
	 * @param item item object itself
	 */
	abstract protected void onClick (int id, Object item);

	/** Updates navigation display.
	 * Interested subclasses should override this to do something
	 * sensible - usually refresh their model.
	 */
	public void updateNavigation () { };
}
