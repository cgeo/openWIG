package cz.matejcik.openwig.desktop.common;

import javax.swing.Icon;

/** Generic interface for various data items
 * that can be displayed in a pretty WigList item.
 * @see ListItemRenderer
 */
public interface ListItemAdapter {
	/** Returns status icon (e.g. navigation arrow, task status) or null if not applicable. */
	Icon getStatus();
	/** Returns name of the item */
	String getName();
	/** Returns subtitle, e.g. distance to item, its location, list of contents, etc. */
	String getSubtitle();
	/** Returns icon for this item */
	Icon getIcon();
}
