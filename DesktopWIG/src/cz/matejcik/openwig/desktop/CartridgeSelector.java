package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.desktop.common.CardPanel;
import cz.matejcik.openwig.desktop.common.FrameTimer;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/** Cartridge picker and launcher window.
 * <p>
 * Represents the window that is visible right after start-up. It has
 * a left-hand pane with a list of cartridges in the current directory
 * and a right-hand pane with details of the selected cartridge.
 * @see CartridgeDetails
 * @see CartridgeList
 */
public class CartridgeSelector extends JFrame implements ActionListener {

	/** contents of left pane */
	CartridgeList list = new CartridgeList(this);
	/** contents of right pane */
	CartridgeDetails details = new CartridgeDetails(list);

	/** timer to handle navigation updates
	 * @see overview
	 */
	FrameTimer refresher;

	/** switcher between "please select" label and actual details display */
	CardPanel detailsPanel = new CardPanel();

	private static final String CMD_CHANGEDIR = "changeDir";

	public CartridgeSelector () {
		// create menu
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem changeDirectory = new JMenuItem("Change directory...");
		changeDirectory.setActionCommand(CMD_CHANGEDIR);
		changeDirectory.addActionListener(this);
		file.add(changeDirectory);

		JMenuItem exit = new JMenuItem(Main.actionExit);
		file.add(exit);

		JMenuItem gps = new JMenuItem(Main.actionGPS);
		gps.setPreferredSize(gps.getMinimumSize());

		menu.add(file);
		menu.add(gps);

		setJMenuBar(menu);

		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.LINE_AXIS));
		c.add(list.getScrollable());
		
		c.add(detailsPanel);
		detailsPanel.setMaximumSize(details.getMaximumSize());

		detailsPanel.add(details, "details");
		detailsPanel.add(new JLabel("Please choose a cartridge from the list.", JLabel.CENTER), "label");
		detailsPanel.show("label");

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent we) {
				Main.actionExit.actionPerformed(null);
			}
		});

		setLocationRelativeTo(null);
		refresher = new FrameTimer(this, this, 500);
	}

	/** Ensures that details pane is visible and updates it with information for <code>cf</code> */
	public void showDetails (CartridgeFile cf) {
		detailsPanel.show("details");
		details.showDetails(cf);
	}

	public void actionPerformed (ActionEvent e) {
		String cmd = e.getActionCommand();
		if (e.getSource() == refresher.getTimer()) {
			list.updateNavigation();
			details.updateNavigation();
			return;
		} else if (cmd == CMD_CHANGEDIR) {
			JFileChooser chooser = new JFileChooser(list.getCurrentDirectory());
			chooser.setDialogTitle("Choose a directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				list.setCurrentDirectory(chooser.getSelectedFile());
				detailsPanel.show("label");
			}
		}
	}
}
