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
	private boolean up = true;
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

	public void chdir(String where) {
		System.out.println("chdir "+where);
		try {
			FileConnection fc = (FileConnection)Connector.open("file:///"+where, Connector.READ);
			if (fc.isDirectory()) {
				Enumeration list = fc.list();
				deleteAll();
				append("..", null);
				while (list.hasMoreElements()) {
					append(list.nextElement().toString(), null);
				}
			}
		} catch (IOException e) {
			
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
						Midlet.push(new CartridgeDetails(cf));
						stop();
					} catch (IOException e) {
						Midlet.error("Failed to load cartridge:\n" + e.getMessage());
					}
					openFile = null;
				}
			} catch (SecurityException e) {
				Midlet.error("you need to allow me to access your files!");
			}
			
			setTitle(currentPath);

			System.out.println("tick");
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
			notify();
		}
	}
}
