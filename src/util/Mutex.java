package util;

public class Mutex {
	private Object guard;
	private boolean locked = false;
	
	public Mutex (Object o) {
		guard = o;
	}
	
	public void lock () {
		while (true) synchronized (guard) {
			try {
				if (locked) {
					guard.wait();
				} else {
					locked = true;
					break;
				}
			} catch (InterruptedException e) { }
		}
	}
	
	public void unlock () {
		synchronized (guard) {
			locked = false;
			guard.notify();
		}
	}
}
