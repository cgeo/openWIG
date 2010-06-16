package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.desktop.common.WigList;
import cz.matejcik.openwig.desktop.common.RefreshableListModel;
import cz.matejcik.openwig.desktop.gps.GPSManager;
import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.j2se.J2SEFileHandle;
import cz.matejcik.openwig.j2se.J2SESeekableFile;
import cz.matejcik.openwig.platform.*;

import java.io.*;
import java.util.prefs.Preferences;


/** List of cartridges available for selection
 * <p>
 * This class can list cartridges in the specified directory
 * in a pretty display, show their detailed information
 * and start or resume saved games.
 */
public class CartridgeList extends WigList {

	/** directory which is displayed */
	private File currentDirectory = new File (System.getProperty("user.dir"));

	private RefreshableListModel<CartridgeListItem> model = new RefreshableListModel<CartridgeListItem>();

	private CartridgeSelector parent;

	Preferences prefs = Preferences.userNodeForPackage(this.getClass());

	/** creates a new CartridgeList */
	public CartridgeList (CartridgeSelector parent) {
		this.parent = parent;
		setModel(model);
		String path = prefs.get("path", System.getProperty("user.dir"));
		setCurrentDirectory(new File(path));
	}

	/** Refreshes list of cartridges in the current directory.
	 *
	 * @see #setCurrentDirectory(java.io.File)
	 */
	private void refreshList () {
		model.clear();
		for (File file : currentDirectory.listFiles()) {
			try {
				if (!file.isFile() || !file.getName().endsWith(".gwc")) continue;
				SeekableFile sf = new J2SESeekableFile(new RandomAccessFile(file, "r"));
				String path = file.getPath();
				String savefile = path.substring(0, path.length() - 4) + ".ows";
				FileHandle save = new J2SEFileHandle(new File(savefile));
				CartridgeFile cf = CartridgeFile.read(sf, save);
				model.add(new CartridgeListItem(file, cf));
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		prefs.put("path", currentDirectory.getAbsolutePath());
	}

	@Override
	public void onClick (int id, Object item) {
		CartridgeFile cf = ((CartridgeListItem)item).getCartridge();
		parent.showDetails(cf);
	}

	/** Generates log file name for the cartridge specified by <code>f</code>
	 * and returns OutputStream for the calculated filename.
	 * @param f cartridge file
	 * @return <code>OutputStream</code> suitable for <code>Engine.newInstance</code>
	 * @see #startSelected()
	 * @see #resumeSelected()
	 * @see Engine#newInstance(cz.matejcik.openwig.formats.CartridgeFile, java.io.OutputStream, cz.matejcik.openwig.platform.UI, cz.matejcik.openwig.platform.LocationService)
	 */
	private static OutputStream getLogFile (File f) {
		String logfile = f.getAbsolutePath();
		logfile = logfile.substring(0, logfile.length() - 4) + ".gwl";
		File lf = new File(logfile);
		OutputStream os = null;
		try {
			os = new FileOutputStream(lf, true);
		} catch (IOException e) { }
		return os;
	}

	/** Starts the cartridge that is currently selected in the list */
	public void startSelected () {
		CartridgeListItem ci = (CartridgeListItem)model.getElementAt(getSelectedIndex());
		OutputStream log = getLogFile(ci.getFile());
		try {
			Engine.newInstance(ci.getCartridge(), log, Main.gui, GPSManager.getGPS()).start();
		} catch (IOException e) {
			// TODO
		}
	}

	/** Resumes from savegame of currently selected cartridge */
	public void resumeSelected () {
		CartridgeListItem ci = (CartridgeListItem)model.getElementAt(getSelectedIndex());
		OutputStream log = getLogFile(ci.getFile());
		try {
			Engine.newInstance(ci.getCartridge(), log, Main.gui, GPSManager.getGPS()).restore();
		} catch (IOException e) {
			// TODO
		}
	}

	@Override
	public void updateNavigation () {
		model.refresh();
	}

	/**
	 * @return the currentDirectory
	 */
	public File getCurrentDirectory () {
		return currentDirectory;
	}

	/** Sets current directory and refreshes display
	 * @param currentDirectory the currentDirectory to set
	 */
	public void setCurrentDirectory (File currentDirectory) {
		if (!currentDirectory.exists()) return;
		this.currentDirectory = currentDirectory;
		refreshList();
	}
}
