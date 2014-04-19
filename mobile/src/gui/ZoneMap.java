package gui;

import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.ZonePoint;

public class ZoneMap extends Canvas implements Pushable, Runnable, CommandListener {
	
	private Zone zone = null;
	
	private Displayable parent;

	private int meritko = 25;
	private static final int[] meritka = { 5, 10, 15, 25, 50, 100, 250, 500 };
	private int mindex = 3;

	private double pixelX, pixelY;
	private double height, width;
	private double offX, offY;

	private static ZoneMap instance = null;
	public static ZoneMap getInstance() {
		if (instance == null) instance = new ZoneMap();
		return instance;
	}

	public ZoneMap reset (Displayable parent, Zone z) {
		this.parent = parent;
		zone = z;
		return this;
	}

	public void push() {
		if (zone != null && !zone.isVisible()) {
			Midlet.push(parent);
			return;
		}
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
		//repaint();
		Midlet.show(this);
	}

	public int yFromLat (double lat) {
		return (int)((-lat - offY) * pixelY);
	}

	public int xFromLon (double lon) {
		return (int)((lon - offX) * pixelX);
	}
	
	synchronized public void paint (Graphics graphics) {
		double lat = Midlet.gps.getLatitude();
		double lon = Midlet.gps.getLongitude();
		int x = xFromLon(lon);
		int y = yFromLat(lat);
		height = ZonePoint.m2lat(meritko);
		width = ZonePoint.m2lon(lat, meritko);
		offX = lon - width/2;
		offY = -lat - height/2;
		pixelX = getWidth() / width;
		pixelY = getHeight() / height;
		graphics.setColor(0xffffff);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		drawOnMap(graphics);

		graphics.setColor(255, 0, 0);
		drawCross(graphics, x, y);

		int fh = graphics.getFont().getHeight();
		graphics.setColor(0);
		graphics.drawString("Dist: "+ZonePoint.makeFriendlyDistance(zone.distance), 5, getHeight() - (2*fh), Graphics.LEFT | Graphics.TOP);
		graphics.drawString("Zoom: "+meritko+"m", 5, getHeight() - fh, Graphics.LEFT | Graphics.TOP);
	}

	private void drawCross (Graphics graphics, int x, int y) {
		graphics.drawLine(x - 3, y, x + 3, y);
		graphics.drawLine(x, y - 3, x, y + 3);
	}

	private void drawOnMap (Graphics graphics) {
		// first draw the zone
		graphics.setStrokeStyle(Graphics.SOLID);
		graphics.setColor(0);
		int ax = xFromLon(zone.points[zone.points.length-1].longitude);
		int ay = yFromLat(zone.points[zone.points.length-1].latitude);
		for (int i = 0; i < zone.points.length; i++) {
			int bx = xFromLon(zone.points[i].longitude);
			int by = yFromLat(zone.points[i].latitude);
			graphics.drawLine(ax, ay, bx, by);
			ax = bx; ay = by;
		}
		// then the bounding box
		graphics.setColor(128, 128, 255);
		int x = xFromLon(zone.bbLeft);
		int y = yFromLat(zone.bbTop);
		int w = xFromLon(zone.bbRight) - x;
		int h = yFromLat(zone.bbBottom) - y;
		graphics.drawRect(x, y, w, h);
		// proximity bounding box
		graphics.setStrokeStyle(Graphics.DOTTED);
		x = xFromLon(zone.pbbLeft);
		y = yFromLat(zone.pbbTop);
		w = xFromLon(zone.pbbRight) - x;
		h = yFromLat(zone.pbbBottom) - y;
		graphics.drawRect(x, y, w, h);
		// then nearest point
		x = xFromLon(zone.nearestPoint.longitude);
		y = yFromLat(zone.nearestPoint.latitude);
		graphics.setColor(0, 128, 0);
		drawCross(graphics, x,y);
	}

	protected void keyPressed (int code) {
		code = getGameAction(code);
		if (code == UP) mindex --;
		else if (code == DOWN) mindex ++;
		if (mindex < 0) mindex = 0;
		if (mindex >= meritka.length) mindex = meritka.length - 1;
		meritko = meritka[mindex];
		repaint();
	}

	private boolean running = false;
	synchronized private void start() {
		if (running) return;
		running = true;
		new Thread(this).start();
	}
	
	synchronized private void stop() {
		running = false;
	}
	
	protected void hideNotify() { stop(); }
	protected void showNotify() { start(); }
	
	public void run () {
		while (running) {
			try { Thread.sleep(200); } catch (InterruptedException e) { }
			repaint();
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		Midlet.push(parent);
	}
}
