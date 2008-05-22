package gui;

import javax.microedition.lcdui.*;
import openwig.Zone;
import openwig.ZonePoint;

public class Navigation extends Canvas implements Pushable, Runnable, CommandListener {
	
	private ZonePoint target;
	private Zone zone = null;
	
	private double heading, angle;
	private String distance, azimuth;
	private int[] pointX = {0, 0, 0, 0}; // 0 - tip; 1 - left wing;
	private int[] pointY = {0, 0, 0, 0}; // 2 - centre; 3 - right wing
	private int diameter, half, smaller;
	private int centerX, centerY;
	private int northX, northY;
	
	public Navigation (ZonePoint point) {
		target = point;
		prepare();
	}
	
	public Navigation (Zone z) {
		zone = z;
		target = new ZonePoint(0,0,0);
		prepare();
	}

	synchronized protected void paint(Graphics screen) {
		screen.setColor(0x00ffffff);
		screen.fillRect(0, 0, getWidth(), getHeight());
		
		screen.setColor(0x00ff0000);
		// arrow:
		for (int i = 0; i < pointX.length; i++) {
			screen.drawLine(pointX[i], pointY[i], pointX[(i+1)%pointX.length], pointY[(i+1)%pointX.length]);
		}
		
		screen.setColor(0x000000ff);
		// NSEW:
		screen.drawChar('N', centerX + northX, centerY - northY, screen.HCENTER | screen.BASELINE);
		screen.drawChar('S', centerX - northX, centerY + northY, screen.HCENTER | screen.BASELINE);
		screen.drawChar('E', centerX + northY, centerY + northX, screen.HCENTER | screen.BASELINE);
		screen.drawChar('W', centerX - northY, centerY - northX, screen.HCENTER | screen.BASELINE);
		
		screen.setColor(0x00000000);
		// labels:
		int h = screen.getFont().getHeight();
		screen.drawString("Dist: "+distance, 5, getHeight() - (3*h), screen.LEFT | screen.TOP);
		screen.drawString("Azim: "+azimuth, 5, getHeight() - (2*h), screen.LEFT | screen.TOP);
	}

	public void prepare() {
		if (zone != null && !zone.isVisible()) {
			Midlet.pop(this);
			return;
		}
		addCommand(Midlet.CMD_BACK);
		setCommandListener(this);
		updateNavi();
	}
	
	protected void sizeChanged (int w, int h) {
		System.out.println("size changed to "+w+","+h);
		int fh = Font.getDefaultFont().getHeight() * 3;
		centerX = w/2;
		centerY = (h-fh)/2;
		diameter = Math.min(centerX, centerY) - 5;
		half = diameter/2;
		smaller = diameter/5;
		updateNavi();
	}
	
	synchronized private void updateNavi() {
		if (zone != null) {
			target.latitude = zone.nearestX;
			target.longitude = zone.nearestY;
		}
		
		// now we have a point to navigate to. get our heading:
		heading = ZonePoint.azimuth2angle(-Midlet.heading);
		double bearing = target.bearing(Midlet.latitude, Midlet.longitude);
		angle = bearing + heading - ZonePoint.PI_2;
		
		double dist;
		if (zone != null) {
			if (zone.ncontain == Zone.INSIDE) dist = 0;
			else dist = zone.distance;
		}
		else dist = target.distance(Midlet.latitude, Midlet.longitude);

		long part = (long)(dist * 1000);
		double d = part/1000.0;
		distance = Double.toString(d)+" m";
		part = (long)(ZonePoint.angle2azimuth(bearing) * 100);
		d = part/100;
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
		
		pointX[2] = centerX; pointY[2] = centerY;
		
		northX = (int)(Math.cos(heading) * diameter);
		northY = (int)(Math.sin(heading) * diameter);
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
			try { Thread.sleep(1000); } catch (Exception e) { }
			updateNavi();
			repaint();
		}
	}

	public void commandAction(Command arg0, Displayable arg1) {
		Midlet.pop(this);
	}
}
