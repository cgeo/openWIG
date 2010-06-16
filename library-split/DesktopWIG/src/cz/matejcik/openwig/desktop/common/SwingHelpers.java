package cz.matejcik.openwig.desktop.common;

import java.awt.Component;
import java.awt.Dimension;


/** Collection of useful functions for handling the rougher
 * and more obtuse edges of working with Swing Toolkit.
 * At the moment, only functions that make it easier to
 * set some meaningful dimensions to components.
 */
public class SwingHelpers {

	/** Sets preferred width of the component, leaving the height intact. */
	public static void setPreferredWidth (Component component, int width) {
		Dimension dim = component.getPreferredSize();
		component.setPreferredSize(new Dimension(width, dim.height));
	}

	/** Sets maximum width of the component, leaving the height intact. */
	public static void setMaximumWidth (Component component, int width) {
		Dimension dim = component.getMaximumSize();
		component.setMaximumSize(new Dimension(width, dim.height));
	}

	/** Sets maximum height of the component, leaving the width intact. */
	public static void setMaximumHeight (Component component, int height) {
		Dimension dim = component.getMaximumSize();
		component.setMaximumSize(new Dimension(dim.width, height));
	}

	/** Limits the component's maximum height to its current (preferred) height.
	 * This is useful e.g. when you want to put a ComboBox into auto-sizing layout
	 * such as BoxLayout and don't want to stretch it vertically.
	 */
	public static void limitMaximumHeight (Component component) {
		setMaximumHeight(component, component.getPreferredSize().height);
	}
}
