package cz.matejcik.openwig.desktop.common;

import java.awt.Window;
import java.awt.event.*;
import javax.swing.Timer;

/** A timer object that only runs when its window is visible on screen.
 * It starts the timer when the window appears on screen and stops it
 * whenever it disappears.
 */
public class FrameTimer implements ComponentListener, WindowListener {

	/** the actual Swing Timer */
	Timer timer;

	/** Creates a new timer and installs window event listeners
	 * to track when it should run.
	 * @param frame the window which should be tracked
	 * @param listener <code>ActionListener</code> that handles timer ticks
	 * @param interval timer interval
	 */
	public FrameTimer (Window frame, ActionListener listener, int interval) {
		timer = new Timer(interval, listener);
		frame.addComponentListener(this);
		frame.addWindowListener(this);
		if (frame.isShowing()) timer.start();
	}

	public void componentResized (ComponentEvent e) { }

	public void componentMoved (ComponentEvent e) { }

	public void componentShown (ComponentEvent e) {
		timer.start();
	}

	public void componentHidden (ComponentEvent e) {
		timer.stop();
	}

	public Timer getTimer () {
		return timer;
	}

	public void windowOpened (WindowEvent e) {
		timer.start();
	}

	public void windowClosing (WindowEvent e) { }

	public void windowClosed (WindowEvent e) {
		timer.stop();
	}

	public void windowIconified (WindowEvent e) {
		timer.stop();
	}

	public void windowDeiconified (WindowEvent e) {
		timer.start();
	}

	public void windowActivated (WindowEvent e) { }

	public void windowDeactivated (WindowEvent e) { }
}
