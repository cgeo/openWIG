package util;

import java.util.Vector;

public class BackgroundRunner extends Thread {

	private static BackgroundRunner instance;

	public BackgroundRunner () {
		start();
	}

	public static BackgroundRunner getInstance () {
		if (instance == null) instance = new BackgroundRunner();
		return instance;
	}
	
	private Vector queue = new Vector();
	private boolean end = false;
	private Runnable queueProcessedListener = null;

	public void setQueueListener (Runnable r) {
		queueProcessedListener = r;
	}

	public void run () {
		boolean events;
		while (!end) {
			events = false;
			while (!queue.isEmpty()) {
				events = true;
				Runnable c = (Runnable)queue.firstElement();
				queue.removeElementAt(0);
				try {
					c.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (events && queueProcessedListener != null) queueProcessedListener.run();
			synchronized (this) {
				if (!queue.isEmpty()) continue;
				if (end) return;
				try { wait(); } catch (InterruptedException e) { }
			}
		}
	}

	synchronized public void perform (Runnable c) {
		queue.addElement(c);
		notify();
	}

	public static void performTask (Runnable c) {
		getInstance().perform(c);
	}

	synchronized public void kill () {
		end = true;
		notify();
	}
}
