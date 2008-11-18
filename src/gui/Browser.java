package gui;

import gwc.CartridgeFile;
import java.io.*;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import util.Config;

public class Browser extends List implements Pushable, Runnable, CommandListener {

	private Hashtable cache = new Hashtable(10);
	private String currentPath;
	private String chdir = null;
	private boolean up = false;
	private String openFile = null;

	public Browser() {
		super("file browser", List.IMPLICIT);
		currentPath = Midlet.config.get(Config.LAST_DIRECTORY);
		if (currentPath == null) currentPath = "";
		
		setSelectCommand(Midlet.CMD_SELECT);
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
	}

	public void prepare() {
		start();
	}

	public void chdir(String where) 
	throws IOException {
		if (where.endsWith("/")) { // directory test without (possibly) protected function call
			FileConnection fc = (FileConnection) Connector.open("file:///" + where, Connector.READ);
			Enumeration list = fc.list();
			deleteAll();
			append("..", null);
			while (list.hasMoreElements()) {
				append(list.nextElement().toString(), null);
			}
			Midlet.config.set(Config.LAST_DIRECTORY, where);
		}
	}

	public void listRoot() {
		Enumeration roots = FileSystemRegistry.listRoots();
		deleteAll();
		while (roots.hasMoreElements()) {
			String root = roots.nextElement().toString();
			append(root, null);
		}
	}
	private Thread thread = null;

	private void start() {
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		thread = null;
	}

	synchronized public void run() {
		// init path
		try {
			if (currentPath.length() == 0) {
				listRoot();
			} else try {
				chdir(currentPath);
			} catch (IOException e) {
				currentPath = "";
				listRoot();
			}
		} catch (SecurityException e) {
			Midlet.error("you need to allow me to access your files!");
		}
		
		while (thread != null) {
			try {
				if (up) {
					if ("".equals(currentPath) || currentPath.lastIndexOf('/', currentPath.length() - 2) == -1) {
						listRoot();
						currentPath = "";
					} else {
						String below = currentPath.substring(0, currentPath.lastIndexOf('/', currentPath.length() - 2)) + "/";
						chdir(below);
						currentPath = below;
					}
					up = false;
				}

				if (chdir != null) {
					String newpath = currentPath + chdir;
					chdir(newpath);
					currentPath = newpath;
					chdir = null;
				}

				if (openFile != null) {
					String file = "file:///" + currentPath + openFile;
					try {
						CartridgeFile cf = CartridgeFile.read(file);
						OutputStream os = null;
						if (Midlet.config.getInt(Config.ENABLE_LOGGING) > 0) try {
							FileConnection fc = (FileConnection)Connector.open(file.substring(0, file.length()-3) + "gwl", Connector.READ_WRITE);
							if (!fc.exists()) fc.create();
							os = fc.openOutputStream(fc.fileSize());
						} catch (Exception e) { e.printStackTrace(); }
						Midlet.push(new CartridgeDetails(cf, os));
						stop();
					} catch (IOException e) {
						Midlet.error("Failed to load cartridge:\n" + e.getMessage());
					}
					openFile = null;
				}
			} catch (IOException e) {
				// presumably loading a default path that no longer exists
				// do nothing (we ate the ioexceptions before anyway
			} catch (SecurityException e) {
				Midlet.error("you need to allow me to access your files!");
			}
			
			setTitle(currentPath);

			try { wait(); } catch (InterruptedException e) { }
		}
	}

	synchronized public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_BACK) {
			stop();
			notify();
			Midlet.pop(this);
		} else if (cmd == Midlet.CMD_SELECT) {
			String sel = getString(getSelectedIndex());
			if ("..".equals(sel)) {
				up = true;
			} else if (sel.endsWith("/")) {
				chdir = sel;
			} else {
				openFile = sel;
			}
			setTitle("wait...");
			notify();
		}
	}
}
