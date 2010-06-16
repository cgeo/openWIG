package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.desktop.gps.GPSManager;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/** Main class.
 * <p>
 * Apart from holding the <code>main()</code> method,
 * this class also contains a bunch of default values,
 * reference to a <code>GameWindow</code> and a <code>CartridgeSelector</code>,
 * and some Actions.
 */
public class Main {

	/** default width of generic pane */
	public static final int ITEM_WIDTH = 270;
	/** default height of everything - designed for 800x484 displays */
	public static final int ITEM_HEIGHT = 480;
	/** default width of list pane */
	public static final int LIST_ITEM_WIDTH = ITEM_WIDTH;
	/** default size of item, converted to Dimension for convenience */
	public static final Dimension ITEM_DIMENSION = new Dimension(ITEM_WIDTH, ITEM_HEIGHT);

	/** Action to display GPS selection dialog. */
	public static Action actionGPS = new AbstractAction("GPS") {
		public void actionPerformed (ActionEvent e) {
			GPSManager.launch();
		}
	};

	/** Action to disconnect everything and cleanly exit. */
	public static Action actionExit = new AbstractAction("Exit") {
		public void actionPerformed (ActionEvent e) {
			Engine.kill();
			GPSManager.kill();
			gui.dispose();
			selector.dispose();
		}
	};

	public static GameWindow gui = new GameWindow();
	public static CartridgeSelector selector = new CartridgeSelector();

	public static void main (String[] args) {
		// TODO implement loading cartridges from command line
		selector.setVisible(true);
	}
}
