package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.desktop.common.ZonePointIcon;
import cz.matejcik.openwig.desktop.common.Navigator;
import cz.matejcik.openwig.desktop.common.ListItemAdapter;
import cz.matejcik.openwig.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;


/** Pretty list item that takes its data from an EventTable object.
 * <p>
 * This list item recognizes all kinds of EventTables and can even
 * provide default icons for the four kinds, and task state icons
 * for Tasks. It also draws direction arrows where appropriate.
 */
public class EventTableListItem implements ListItemAdapter {

	/** Icons for all three task states */
	private static final ImageIcon[] task_states;
	static {
		task_states = new ImageIcon[3];
		task_states[Task.DONE] = new ImageIcon(EventTableListItem.class.getResource("/icons/task-done.png"));
		task_states[Task.PENDING] = new ImageIcon(EventTableListItem.class.getResource("/icons/task-pending.png"));
		task_states[Task.FAILED] = new ImageIcon(EventTableListItem.class.getResource("/icons/task-failed.png"));
	}

	/** Default icon for Task */
	private static final ImageIcon TASK = new ImageIcon(EventTableListItem.class.getResource("/icons/tasks.png"));
	/** Default icon for Item */
	private static final ImageIcon ITEM = new ImageIcon(EventTableListItem.class.getResource("/icons/thing.png"));
	/** Default icon for Character */
	private static final ImageIcon CHARACTER = new ImageIcon(EventTableListItem.class.getResource("/icons/character.png"));
	/** Default icon for Zone */
	private static final ImageIcon ZONE = new ImageIcon(EventTableListItem.class.getResource("/icons/locations.png"));

	protected EventTable table;
	private ZonePointIcon icon = new ZonePointIcon();

	public EventTable getTable () {
		return table;
	}

	public EventTableListItem (EventTable table) {
		this.table = table;
	}

	public Icon getStatus () {
		if (table instanceof Task) {
			Task t = (Task)table;
			return task_states[t.state()];
		} else if (table instanceof Zone) {
			Zone z = (Zone)table;
			icon.setPoint(z.nearestPoint);
			return icon;
		} else if (table.isLocated()) {
			icon.setPoint(table.position);
			return icon;
		} else return null;
	}

	public String getName () {
		return table.name;
	}

	public String getSubtitle () {
		if (table instanceof Zone) return Navigator.getDistanceAndDirection(((Zone)table).nearestPoint);
		if (table.isLocated()) return Navigator.getDistanceAndDirection(table.position);
		else return "";
	}

	public Icon getIcon () {
		try {
			byte[] i = table.getIcon();
			return new ImageIcon(i);
		} catch (Exception e) {
			if (table instanceof Zone) {
				return ZONE;
			} else if (table instanceof Thing) {
				Thing t = (Thing)table;
				if (t.isCharacter()) return CHARACTER;
				else return ITEM;
			} else if (table instanceof Task) {
				return TASK;
			} else return null;
		}
	}

}
