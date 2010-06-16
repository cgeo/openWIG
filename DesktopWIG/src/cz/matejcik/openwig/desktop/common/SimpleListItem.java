package cz.matejcik.openwig.desktop.common;

import javax.swing.Icon;


/** Simplest implementation of pretty list item.
 * It holds its own data and does not rely on other objects.
 */
public class SimpleListItem implements ListItemAdapter {

	private Icon status, icon;
	private String name, subtitle;

	public SimpleListItem (String name, String subtitle, Icon icon, Icon status) {
		this.status = status;
		this.icon = icon;
		this.name = name;
		this.subtitle = subtitle;
	}

	public Icon getStatus () {
		return status;
	}

	public String getName () {
		return name;
	}

	public String getSubtitle () {
		return subtitle;
	}

	public Icon getIcon () {
		return icon;
	}

	public void setStatus (Icon status) {
		this.status = status;
	}

	public void setIcon (Icon icon) {
		this.icon = icon;
	}

	public void setName (String name) {
		this.name = name;
	}

	public void setSubtitle (String subtitle) {
		this.subtitle = subtitle;
	}

}
