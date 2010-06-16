package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.desktop.common.RefreshableListModel;
import cz.matejcik.openwig.desktop.common.WigList;
import cz.matejcik.openwig.desktop.common.SimpleListItem;
import javax.swing.ImageIcon;

/** Pretty main menu.
 * <p>
 * Contains links to Locations, You see, Inventory and Tasks
 * submenus, updated with numbers of items in the respective
 * submenus.
 */
public class MainMenu extends WigList {

	private static final int LOCATIONS = 0;
	private static final int YOUSEE = 1;
	private static final int INVENTORY = 2;
	private static final int TASKS = 3;
	
	private SimpleListItem locations, yousee, inventory, tasks;
	private RefreshableListModel<SimpleListItem> model = new RefreshableListModel<SimpleListItem>();

	private GameWindow parent;

	/** create a MainMenu instance.
	 *
	 * This constructor builds its items and pushes them into list's model.
	 */
	public MainMenu (GameWindow parent) {
		this.parent = parent;

		ImageIcon icoZones = new ImageIcon(getClass().getResource("/icons/locations.png"));
		ImageIcon icoYousee = new ImageIcon(getClass().getResource("/icons/yousee.png"));
		ImageIcon icoInventory = new ImageIcon(getClass().getResource("/icons/inventory.png"));
		ImageIcon icoTasks = new ImageIcon(getClass().getResource("/icons/tasks.png"));

		locations = new SimpleListItem("Locations", "", icoZones, null);
		yousee = new SimpleListItem("You see", "", icoYousee, null);
		inventory = new SimpleListItem("Inventory", "", icoInventory, null);
		tasks = new SimpleListItem("Tasks", "", icoTasks, null);

		model.add(locations);
		model.add(yousee);
		model.add(inventory);
		model.add(tasks);

		setModel(model);
	}

	/** Update links with item counts and lists from
	 * each submenu.
	 * Called as a part of UI refresh routine.
	 * @see cz.matejcik.openwig.platform.UI#refresh()
	 */
	public void refresh () {
		int ln = parent.zones.length();
		int yn = parent.yousee.length();
		int in = parent.inventory.length();
		int tn = parent.tasks.length();
		locations.setName("Locations (" + ln + ")");
		locations.setSubtitle(parent.zones.getContents());
		yousee.setName("You see (" + yn + ")");
		yousee.setSubtitle(parent.yousee.getContents());
		inventory.setName("Inventory (" + in + ")");
		inventory.setSubtitle(parent.inventory.getContents());
		tasks.setName("Tasks (" + tn + ")");
		tasks.setSubtitle(parent.tasks.getContents());
		model.refresh();
	}

	@Override
	protected void onClick (int id, Object item) {
		switch (id) {
			case LOCATIONS:
				parent.showSubmenu("zones");
				break;
			case YOUSEE:
				parent.showSubmenu("yousee");
				break;
			case INVENTORY:
				parent.showSubmenu("inventory");
				break;
			case TASKS:
				parent.showSubmenu("tasks");
				break;
		}
	}
}
