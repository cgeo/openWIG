package gui;

import gwc.CartridgeFile;
import java.io.*;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import util.BackgroundRunner;
import util.Config;

public class Browser extends List implements Pushable, CommandListener {

	private Hashtable cache = new Hashtable(10);
	private String currentPath;
	private String root;
	private String selectedFile = null;

	private static Alert restoreQuestion = new Alert("question", "Load previously saved game?", null, AlertType.CONFIRMATION);
	private static Command restoreYes = new Command("Yes", Command.SCREEN, 1);
	private static Command restoreNo = new Command("No", Command.SCREEN, 2);
	static {
		restoreQuestion.addCommand(restoreYes);
		restoreQuestion.addCommand(restoreNo);
	}
	
	private static Image gwc = null;
	private static Image ows = null;
	static {
		try {
			gwc = Image.createImage("/icons/compass.png");
			ows = Image.createImage("/icons/task-pending.png");

		} catch (IOException e) { }
	}

	// those are used to hand over cf and lf from restore dialog.
	// do not use for anything else ever.
	private CartridgeFile tmpCartridgeFile;
	private OutputStream tmpLogFile;

	public Browser() {
		super("wait...", List.IMPLICIT);
		/*currentPath = Midlet.config.get(Config.LAST_DIRECTORY);
		if (currentPath == null) currentPath = "";*/

		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}

	public void push () {
		// XXX TODO not refresh when returning from details
		chdir(Midlet.config.get(Config.LAST_DIRECTORY));
		restoreQuestion.setCommandListener(this);
		Midlet.show(this);
	}

	private void setCurrentPath (String path) {
		Midlet.config.set(Config.LAST_DIRECTORY, path);
		currentPath = path;
		setTitle(path);
	}

	private void handleProblem (Throwable e) {
		if (e instanceof IOException) {
			Midlet.error(e.getMessage());
		} else if (e instanceof SecurityException) {
			Midlet.error("you need to allow me to access your files!");
		}
	}

	private class Chdir implements Runnable {
		public String where;
		public void run () {
			try {
				if (where.endsWith("/")) {
					FileConnection fc = (FileConnection)Connector.open("file:///" + where, Connector.READ);
					Enumeration list = fc.list();
					deleteAll();
					append("..", null);
					while (list.hasMoreElements()) {
						String fn = list.nextElement().toString();
						Image image = null;
						if (fn.toLowerCase().endsWith(".gwc"))
							image = gwc;
						else if (fn.toLowerCase().endsWith(".ows"))
							image = ows;
						append(fn, image);
					}
					setCurrentPath(where);
				}
			} catch (IllegalArgumentException e) {
				listRoot.run();
			} catch (IOException e) {
				Midlet.error(e.getMessage());
				listRoot.run();
			} catch (Throwable e) {
				handleProblem(e);
			}
		}
	}
	private Chdir chdir = new Chdir();

	private void chdir (String where) {
		if (where == null || where.length() == 0) listRoot();
		else {
			chdir.where = where;
			BackgroundRunner.performTask(chdir);
		}
	}

	private void descend (String dirname) {
		chdir(currentPath + dirname);

	}

	private void ascend () {
		if ("".equals(currentPath) || currentPath.lastIndexOf('/', currentPath.length() - 2) == -1) {
			listRoot();
		} else {
			String below = currentPath.substring(0, currentPath.lastIndexOf('/', currentPath.length() - 2)) + "/";
			chdir(below);
		}
	}

	private Runnable listRoot = new Runnable() {
		public void run () {
			Enumeration roots = FileSystemRegistry.listRoots();
			deleteAll();
			while (roots.hasMoreElements()) {
				String root = roots.nextElement().toString();
				append(root, null);
			}
			setCurrentPath("");
		}
	};

	private void listRoot () {
		BackgroundRunner.performTask(listRoot);
	}

	private class OpenFile implements Runnable {
		public String filename;
		public void run () {
			String file = "file:///" + currentPath + filename;
			try {
				CartridgeFile cf = CartridgeFile.read(file);
				OutputStream os = null;

				// open logfile
				if (Midlet.config.getInt(Config.ENABLE_LOGGING) > 0) try {
						FileConnection fc = (FileConnection)Connector.open(file.substring(0, file.length() - 3) + "gwl", Connector.READ_WRITE);
						if (!fc.exists()) fc.create();
						os = fc.openOutputStream(fc.fileSize());
					} catch (Exception e) {
						e.printStackTrace();
					}

				selectedFile = filename; // workaround for cf.getSavegame using it
				if (cf.getSavegame().exists()) {
					tmpCartridgeFile = cf;
					tmpLogFile = os;
					Midlet.display.setCurrent(restoreQuestion);
				} else {
					Midlet.push(Midlet.cartridgeDetails.reset(cf, os));
				}
			} catch (IOException e) {
				Midlet.error("Failed to load cartridge:\n" + e.getMessage());
			}
			selectedFile = filename;
		}
	}
	private OpenFile openFile = new OpenFile();

	private void openFile (String name) {
		openFile.filename = name;
		BackgroundRunner.performTask(openFile);
	}

	public FileConnection getSyncFile ()
	throws IOException {
		try {
			String filename = selectedFile.substring(0, selectedFile.length()-3) + "ows";
			FileConnection fc = (FileConnection)Connector.open("file:///" + currentPath + filename, Connector.READ_WRITE);
			//if (!fc.exists()) fc.create();
			return fc;
		} catch (SecurityException e) {
			Midlet.error("you need to allow me to access your files!");
			return null;
		}
	}

	synchronized public void commandAction(Command cmd, Displayable disp) {
		if (disp == restoreQuestion) {
			if (cmd == restoreYes) {
				Midlet.restoreCartridge(tmpCartridgeFile, tmpLogFile);
			} else {
				Midlet.push(Midlet.cartridgeDetails.reset(tmpCartridgeFile, tmpLogFile));
			}
		} else {
			if (cmd == Midlet.CMD_BACK) {
				Midlet.push(Midlet.baseMenu);
			} else if (cmd == SELECT_COMMAND) {
				String sel = getString(getSelectedIndex());
				setTitle("wait...");
				if ("..".equals(sel)) ascend();
				else if (sel.endsWith("/")) descend(sel);
				else openFile(sel);
			}
		}
	}
}
