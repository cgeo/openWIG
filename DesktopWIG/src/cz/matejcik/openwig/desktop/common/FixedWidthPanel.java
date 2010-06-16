package cz.matejcik.openwig.desktop.common;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;


/** Basic panel that keeps its width tied to its scrollable viewport. */
public class FixedWidthPanel extends JPanel implements Scrollable {

	public Dimension getPreferredScrollableViewportSize () {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement (Rectangle visibleRect, int orientation, int direction) {
		return 3;
	}

	public int getScrollableBlockIncrement (Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	public boolean getScrollableTracksViewportWidth () {
		return true;
	}

	public boolean getScrollableTracksViewportHeight () {
		return false;
	}
}
