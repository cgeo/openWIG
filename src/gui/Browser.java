package gui;

import java.io.*;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import util.Config;

public class Browser extends List implements Pushable, Runnable, CommandListener {

	private Hashtable cache = new Hashtable(10);
	private String currentPath;

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
			FileConnection fc = (FileConnection)Connector.open("file:///"+currentPath, Connector.READ);
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
		deleteAll();
		Enumeration roots = FileSystemRegistry.listRoots();
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
			setTitle(currentPath);
			if (currentPath.length() == 0) listRoot();
			else chdir(currentPath);
			if (Midlet.display.getCurrent() != this) { stop(); break; }
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
				if (currentPath.lastIndexOf('/', currentPath.length()-2) == -1) currentPath = "";
				else currentPath = currentPath.substring(0, currentPath.lastIndexOf('/', currentPath.length()-2)) + "/";
			} else {
				currentPath += sel;
			}
			notify();
		}
	}
}
