package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.*;

import java.util.*;

import cz.matejcik.openwig.desktop.common.CardPanel;
import cz.matejcik.openwig.desktop.common.FrameTimer;
import cz.matejcik.openwig.platform.UI;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;


/** Main game window.
 * <p>
 * Contains the {@link MainMenu}, its four submenus (in a single card pane) and
 * details pane for the various objects. Makes sure that everything is properly
 * displayed in the right place at the right time, and holds methods to accomplish this.
 * <p>
 * This class also implements <code>UI</code>, for calls from OpenWIG Engine.
 * @see UI
 */
public class GameWindow extends JFrame implements UI {

	private MainMenu mainMenu = new MainMenu(this);

	/** List of zones.
	 * Its source reads data from <code>Engine.instance.cartridge.zones</code>
	 */
	protected EventTableList zones = new EventTableList(this, new EventTableList.Source() {
		public List<EventTable> newSet () {
			List<EventTable> ret = new ArrayList<EventTable>();
			Vector v = Engine.instance.cartridge.zones;
			for (Object o : v) {
				Zone z = (Zone)o;
				if (z.isVisible()) ret.add(z);
			}
			return ret;
		}
	});

	/** List of visible items in active zones.
	 * A.k.a. "you see". Its source reads data from <code>Engine.instance.cartridge.currentThings()</code>
	 */
	protected EventTableList yousee = new EventTableList(this, new EventTableList.Source() {
		public List<EventTable> newSet () {
			KahluaTable container = Engine.instance.cartridge.currentThings();
			List<EventTable> ret = new ArrayList<EventTable>(container.len());
			KahluaTableIterator it = container.iterator();
			while (it.advance()) {
				Thing t = (Thing)it.getValue();
				if (t.isVisible()) ret.add(t);
			}
			return ret;
		}
	});

	/** List of visible items in inventory.
	 * Its source reads data from <code>Engine.instance.player.inventory</code>
	 */
	protected EventTableList inventory = new EventTableList(this, new EventTableList.Source() {
		public List<EventTable> newSet () {
			KahluaTable container = Engine.instance.player.inventory;
			List<EventTable> ret = new ArrayList<EventTable>(container.len());
			KahluaTableIterator it = container.iterator();
			while (it.advance()) {
				Thing t = (Thing)it.getValue();
				if (t.isVisible()) ret.add(t);
			}
			return ret;
		}
	});

	/** List of tasks.
	 * Its source reads data from <code>Engine.instance.cartridge.tasks</code>
	 */
	protected EventTableList tasks = new EventTableList(this, new EventTableList.Source() {
		public List<EventTable> newSet () {
			List<EventTable> ret = new ArrayList<EventTable>(Engine.instance.cartridge.tasks.size());
			for (Object o : Engine.instance.cartridge.tasks) {
				Task t = (Task)o;
				if (t.isVisible()) ret.add(t);
			}
			return ret;
		}
	});

	/** CardPanel that can show submenus for Locations, You see, Inventory and Tasks */
	private CardPanel submenus = new CardPanel();
	/** Panel that flips between "please select" and actual details. */
	private CardPanel detailDisplay = new CardPanel();
	
	private EventTableDetails details = new EventTableDetails(this);
	private DialogWindow dialog = new DialogWindow(this);

	/** Timer for refreshing navigation displays. */
	private FrameTimer refresher = new FrameTimer(this, new ActionListener() {
		public void actionPerformed (ActionEvent e) {
			zones.updateNavigation();
			tasks.updateNavigation();
			yousee.updateNavigation();
			details.updateNavigation();
		}
	}, 500);

	public GameWindow () {
		setTitle("OpenWIG Desktop Edition");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent e) {
				confirmExit();
			}
		});

		Container panel = getContentPane();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		panel.add(mainMenu.getScrollable());
		panel.add(submenus);
		panel.add(detailDisplay);

		submenus.add(zones.getScrollable(), "zones");
		submenus.add(inventory.getScrollable(), "inventory");
		submenus.add(yousee.getScrollable(), "yousee");
		submenus.add(tasks.getScrollable(), "tasks");

		detailDisplay.add(details, "details");
		detailDisplay.add(new JLabel("Choose from the left submenu.", JLabel.CENTER), "empty");

		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		menu.add(file);
		menu.add(new JMenuItem(Main.actionGPS));

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Engine.requestSync();
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				confirmExit();
			}
		});
		file.add(save);
		file.add(exit);

		setJMenuBar(menu);

		pack();
		setLocationRelativeTo(null);
	}

	/** If true, the {@link #unblock()} method kills the window next time Engine
	 * completes a save.
	 * This is used in {@link #confirmExit()} - because Engine saves asynchronously,
	 * it must somehow delay the kill until after the save is done. And we are pretty
	 * confident that Engine will call <code>blockForSave()</code> and <code>unblock()</code>
	 * in response to our request.
	 */
	boolean closeAfterSave = false;

	/** Displays a yes/no/cancel dialog for saving the game on exit, and acts accordingly.
	 * Called from exit commands.
	 * The player has three options: "yes" to save and exit, "no" to exit without saving
	 * and "cancel" to cancel the exit command.
	 */
	private void confirmExit () {
		int ret = JOptionPane.showConfirmDialog(this, "Save game before exiting?");
		if (ret == 0) {
			closeAfterSave = true;
			Engine.requestSync();
		} else if (ret == 1) {
			kill();
		}
	}

	/** Kills the window.
	 * Actually, it requests the Engine to die, hoping that as its dying wish, Engine will
	 * call <code>ui.end()</code>, which will in turn close this window safely.
	 * Engine tends to do that.
	 */
	private void kill () {
		/*if (savingDlg != null) savingDlg.dispose();
		dialog.dispose();*/
		Engine.kill();
	}

	/** Displays the detail pane and updates it with info from <code>details</code> */
	public void showDetails (EventTable details) {
		detailDisplay.show("details");
		this.details.showDetails(details);
	}

	/** Hides the detail pane and displays "please select" label instead.
	 * Called from the detail pane itself, in case that it finds out that the displayed
	 * object is no longer accessible to player.
	 */
	public void hideDetails () {
		detailDisplay.show("empty");
	}

	/** Shows the submenu specified by the key.
	 *
	 * @param key one of four possible values: <code>"zones"</code>, <code>"tasks"</code>, <code>"yousee"</code>, <code>"inventory"</code>
	 */
	public void showSubmenu (String key) {
		submenus.show(key);
	}

	@Override
	public void refresh () {
		zones.prepareRefresh();
		yousee.prepareRefresh();
		inventory.prepareRefresh();
		tasks.prepareRefresh();
		mainMenu.prepareRefresh();
		
		SwingUtilities.invokeLater(new Runnable() { public void run () {
			zones.refresh();
			yousee.refresh();
			inventory.refresh();
			tasks.refresh();
			mainMenu.refresh();
			details.refresh();
		}});
	}

	@Override
	public void start () {
		setVisible(true);
		Main.selector.setVisible(false);
	}

	@Override
	public void end () {
		setVisible(false);
		dispose();
		Main.selector.setVisible(true);
	}

	@Override
	public void showError (String msg) {
		JOptionPane.showMessageDialog(rootPane, msg, "error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void debugMsg (String msg) {
		System.err.print(msg);
	}

	/** not implemented, because nobody uses it anyway and it would only clutter the UI */
	@Override
	public void setStatusText (String text) {
	}

	/** Actually perform the {@link #showScreen()} action.
	 * Display the specified submenu, and in case of Details,
	 * figure out which submenu should be visible.
	 */
	protected void doShowScreen (int screenId, EventTable details) {
		dialog.close();
		switch (screenId) {
			case UI.MAINSCREEN:
				// nop
				break;
			case UI.LOCATIONSCREEN:
				showSubmenu("zones");
				break;
			case UI.ITEMSCREEN:
				showSubmenu("yousee");
				break;
			case UI.INVENTORYSCREEN:
				showSubmenu("inventory");
				break;
			case UI.TASKSCREEN:
				showSubmenu("tasks");
				break;
			case UI.DETAILSCREEN:
				// figure out which submenu
				if (details instanceof Zone)
					showSubmenu("zones");
				else if (details instanceof Task)
					showSubmenu("tasks");
				else if (details instanceof Thing && ((Thing)details).container == Engine.instance.player) {
					showSubmenu("inventory");
				} else {
					showSubmenu("yousee");
				}
				showDetails(details);
				break;
/*			case UI.COORDSCREEN:
				GPSManager.launch();
				break;*/
		}
	}

	public void showScreen (final int screenId, final EventTable details) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				doShowScreen(screenId, details);
			}
		});
	}

	/** not implemented because of external dependencies for mp3 and ogg playback */
	public void playSound (byte[] data, String mime) {
		// TODO - install JMF and find out how to bundle it
	}

	/** modal do-not-touch-anything-until-i-disappear dialog that simply says "saving, please wait" */
	private JDialog savingDlg;

	/** Displays a blocking dialog. */
	public void blockForSaving () {
		if (savingDlg == null) {
			savingDlg = new JDialog(this);
			savingDlg.setModal(true);
			savingDlg.setResizable(false);
			savingDlg.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			savingDlg.getContentPane().add(new JLabel("Saving, please wait..."));
			savingDlg.pack();
			savingDlg.setLocationRelativeTo(this);
		}
		System.out.println("blocking");
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				savingDlg.setVisible(true);
			}
		});
	}

	/** Hides the blocking dialog. If <code>closeAfterSave</code> is true, kills the window. */
	public void unblock () {
		System.out.println("unblocking");
		if (savingDlg != null) {
			SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				savingDlg.setVisible(false);
			}
			});
		}
		if (closeAfterSave) kill();
	}
	
	public String getDeviceID () {
		return "dsktpwig";
	}

	@Override
	public void uiMessage(DialogObject dobj) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				dialog.showDialog(dobj, "OK", null);
			}
		});
	}

	@Override
	public void uiConfirm(DialogObject dobj, String button) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				dialog.showDialog(dobj, button, null);
			}
		});
	}

	@Override
	public void uiInput(DialogObject dobj) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				dialog.showInput(dobj, null);
			}
		});
	}

	@Override
	public void uiChoice(DialogObject dobj, String[] choices) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				dialog.showInput(dobj, null);
			}
		});
	}

	@Override
	public void uiNotify(DialogObject dobj) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				dialog.showDialog(dobj, "OK", null);
			}
		});
	}

	@Override
	public void uiCancel(Runnable callback) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.close();
				callback.run();
			}
		});
	}

	@Override
	public void uiWait(Runnable callback) {
		throw new RuntimeException("not implemented");
		// not implemented because not implemented
	}
}
