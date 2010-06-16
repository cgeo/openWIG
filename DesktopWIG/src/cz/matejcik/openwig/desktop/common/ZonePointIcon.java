package cz.matejcik.openwig.desktop.common;

import cz.matejcik.openwig.ZonePoint;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/** An Icon that draws a small arrow pointing towards a specified <code>ZonePoint</code> */
public class ZonePointIcon implements Icon {

	protected ZonePoint point;
	protected int width = 32, height = 32;

	public ZonePointIcon () { }

	public ZonePointIcon (ZonePoint point) {
		this.point = point;
	}

	public void paintIcon (Component c, Graphics g, int x, int y) {
		Navigator.paintIcon(g, x, y, getPoint());
	}

	public int getIconWidth () {
		return width;
	}

	public int getIconHeight () {
		return width;
	}

	public void setSize (int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the point
	 */
	public ZonePoint getPoint () {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint (ZonePoint point) {
		this.point = point;
	}
}
