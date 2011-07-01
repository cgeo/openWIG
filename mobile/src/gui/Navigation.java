package gui;

import javax.microedition.lcdui.*;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.ZonePoint;

public class Navigation extends Canvas implements Pushable, Runnable, CommandListener {
	
	private ZonePoint target;
	private Zone zone = null;
	
	private double heading, angle;
	private double theading, tangle, hstep = 0, astep = 0;
	private String distance, azimuth;
	private int[] pointX = {0, 0, 0, 0}; // 0 - tip; 1 - left wing;
	private int[] pointY = {0, 0, 0, 0}; // 2 - centre; 3 - right wing
	private int diameter, half, smaller;
	private int centerX, centerY;
	private int northX, northY;
	
	private static final int STEPS = 4;
	
	private Displayable parent;
	
	private static Command cmd_map = new Command("Map", Command.SCREEN, 3);

	public Navigation () {
		addCommand(Midlet.CMD_BACK);
		addCommand(MainMenu.CMD_GPS);
		setCommandListener(this);
	}
	
	public Navigation reset (Displayable parent, ZonePoint point) {
		this.parent = parent;
		target = point;
		zone = null;
		removeCommand(cmd_map);
		return this;
	}
	
	public Navigation reset (Displayable parent, Zone z) {
		this.parent = parent;
		zone = z;
		target = z.nearestPoint;
		addCommand(cmd_map);
		return this;
	}

	synchronized protected void paint(Graphics screen) {
		if (centerX == 0) sizeChanged(getWidth(), getHeight());
		screen.setColor(0x00ffffff);
		screen.fillRect(0, 0, getWidth(), getHeight());
		
		screen.setColor(0x00ff0000);
		// arrow:
		int[] x = pointX; int[] y = pointY;
		screen.fillTriangle(x[0],y[0],x[1],y[1],x[3],y[3]);
		screen.setColor(0x00ffffff);
		screen.fillTriangle(x[2],y[2],x[1],y[1],x[3],y[3]);
		screen.setColor(0x00000000);
		screen.fillArc(centerX-3, centerY-3, 7, 7, 0, 360);
/*		for (int i = 0; i < pointX.length; i++) {
			screen.drawLine(pointX[i], pointY[i], pointX[(i+1)%pointX.length], pointY[(i+1)%pointX.length]);
		}*/
		
		screen.setColor(0x000000ff);
		// NSEW:
		screen.drawChar('N', centerX + northX, centerY - northY, Graphics.HCENTER | Graphics.BASELINE);
		screen.drawChar('S', centerX - northX, centerY + northY, Graphics.HCENTER | Graphics.BASELINE);
		screen.drawChar('E', centerX + northY, centerY + northX, Graphics.HCENTER | Graphics.BASELINE);
		screen.drawChar('W', centerX - northY, centerY - northX, Graphics.HCENTER | Graphics.BASELINE);
		
		screen.setColor(0x00000000);
		// labels:
		int h = screen.getFont().getHeight();
		screen.drawString("Dist: "+distance, 5, getHeight() - (2*h), Graphics.LEFT | Graphics.TOP);
		screen.drawString("Azim: "+azimuth, 5, getHeight() - h, Graphics.LEFT | Graphics.TOP);
	}

	public void push() {
		if (zone != null && !zone.isVisible()) {
			Midlet.push(parent);
			return;
		}
		updateNavi();
		Midlet.show(this);
	}
	
	protected void sizeChanged (int w, int h) {
		int fh = Font.getDefaultFont().getHeight();
		centerX = w/2;
		centerY = (h - 3*fh)/2;
		diameter = Math.min(centerX, centerY) - 5;
		centerY += fh;
		half = diameter/2;
		smaller = diameter/5;
		updateNavi();
	}
	
	private double normalize (double sum) {
		while (sum > Math.PI) sum -= Math.PI * 2;
		while (sum <= -Math.PI) sum += Math.PI * 2;
		return sum;
	}
	
	synchronized private boolean updateNavi() {
		// first calculate distance
		String ndistance;
		if (zone != null) {
			if (zone.contain == Zone.INSIDE) ndistance = "inside";
			else ndistance = ZonePoint.makeFriendlyDistance(zone.distance);
		}
		else ndistance = target.friendlyDistance(Midlet.gps.getLatitude(), Midlet.gps.getLongitude());
		
		// now we have a point to navigate to. get our heading:
		double nheading = ZonePoint.azimuth2angle(-Midlet.gps.getHeading());
		if (nheading != theading) {
			theading = nheading;
			hstep = normalize(nheading - heading) / STEPS;
		}
		double bearing = target.bearing(Midlet.gps.getLatitude(), Midlet.gps.getLongitude());
		double nangle = bearing + theading - ZonePoint.PI_2;
		if (nangle != tangle) {
			tangle = nangle;
			astep = normalize(nangle - angle) / STEPS;
		}
		
		if (theading == heading && tangle == angle && ndistance.equals(distance)) return false;
		distance = ndistance;
		
		double a = Math.abs(normalize(heading - theading));
		if (hstep != 0 && a >= Math.abs(hstep)) {
			heading = normalize(heading + hstep);
		} else {
			heading = theading;
			hstep = 0;
		}
		a = Math.abs(normalize(angle - tangle));
		if (astep != 0 && a >= Math.abs(astep)) {
			angle = normalize(angle + astep);
		} else {
			angle = tangle;
			astep = 0;
		}

		long part = (long)(ZonePoint.angle2azimuth(bearing) * 100);
		double d = part/100;
		azimuth = Double.toString(d);
		
		double tsin = Math.sin(angle);
		double tcos = Math.cos(angle);
		
		int tsin10 = (int)(tsin*smaller), tcos10 = (int)(tcos*smaller);
		pointX[0] = centerX + (int)(tcos * diameter);
		pointX[2] = centerX - (int)(tcos * half);
		pointY[0] = centerY - (int)(tsin * diameter);
		pointY[2] = centerY + (int)(tsin * half);
		pointX[1] = pointX[2] + tsin10;
		pointX[3] = pointX[2] - tsin10;
		pointY[1] = pointY[2] + tcos10;
		pointY[3] = pointY[2] - tcos10;
		
		pointX[2] = centerX - tcos10; pointY[2] = centerY + tsin10;
		
		northX = (int)(Math.cos(heading) * diameter);
		northY = (int)(Math.sin(heading) * diameter);
		
		return true;
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
			if (updateNavi()) repaint();
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if (cmd == Midlet.CMD_BACK) {
			Midlet.push(parent);
		} else if (cmd == MainMenu.CMD_GPS) {
			Midlet.push(Midlet.coordinates.reset(this));
		} else if (cmd == cmd_map) {
			Midlet.push(ZoneMap.getInstance().reset(parent, zone));
		}
	}
}
