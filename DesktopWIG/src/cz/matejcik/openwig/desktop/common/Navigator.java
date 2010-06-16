package cz.matejcik.openwig.desktop.common;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.platform.LocationService;
import cz.matejcik.openwig.desktop.gps.GPSManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Utility class to generate arrow icons, compass images
 * and distance labels.
 */
public class Navigator {

	private static final String[] wordsNS = { "", "north", "south" };
	private static final String[] wordsEW = { "", "east", "west" };

	/** Returns current GPS only if it is useful.
	 * @return <code>null</code> when the current GPS is offline or does not
	 * have a good enough fix, current GPS otherwise.
	 */
	private static LocationService getActiveGPS () {
		LocationService gps = GPSManager.getGPS();
		if (gps.getState() == LocationService.ONLINE ||
			(gps.getState() == LocationService.NO_FIX && gps.getLatitude() != 0))
			return gps;
		else
			return null;
	}

	/** Returns string describing distance and direction to <code>zp</code>.
	 * For example, "195km northeast".
	 * @param zp the ZonePoint to which the distance is taken
	 * @return human-readable string with distance and general direction
	 */
	public static String getDistanceAndDirection (ZonePoint zp) {
		LocationService gps = getActiveGPS();
		if (gps != null) {
			String dist = zp.friendlyDistance(gps.getLatitude(), gps.getLongitude());

			double dx = zp.latitude - gps.getLatitude();
			double dy = zp.longitude - gps.getLongitude();
			double adx = Math.abs(dx), ady = Math.abs(dy);
			int north = 0, east = 0;
			if (adx >= ady / 2 && dx != 0) {
				north = dx > 0 ? 1 : 2;
			}
			if (ady >= adx / 2 && dy != 0) {
				east = dy > 0 ? 1 : 2;
			}


			return dist + " " + wordsNS[north] + wordsEW[east];
		} else {
			return "";
		}
	}

	/** Returns string describing distance, without direction, to the specified <code>point</code>
	 * For example, "12.5km" or "697.3m"
	 * @param point the point to which the distance is calculated
	 * @return string with human-readable distance
	 */
	public static String getDistanceOnly (ZonePoint point) {
		LocationService gps = getActiveGPS();
		if (gps != null) {
			return point.friendlyDistance(gps.getLatitude(), gps.getLongitude());
		} else {
			return "";
		}
	}

	/** Paints an arrow rotated by <code>bearing</code> onto the specified <code>Graphics</code> */
	private static void paintArrow (Graphics2D g2, double bearing, int x, int y, int width, int height) {
		g2.setColor(Color.blue);

		int smaller = Math.min(width, height);
		int tiplen = smaller * 2 / 5;
		int sidelen = smaller / 5;
		int w2 = width/2 + x, h2 = height/2 + y;
		g2.rotate(-bearing, w2, h2);

		int[] xpoints = {x + smaller - 1, w2 - tiplen, w2 - tiplen + sidelen, w2 - tiplen};
		int[] ypoints = {h2, h2 - sidelen, h2, h2 + sidelen};
		g2.fillPolygon(xpoints, ypoints, 4);
		g2.setColor(Color.white);
		g2.drawPolygon(xpoints, ypoints, 4);
	}

	/** Paints an icon with arrow pointing to the specified <code>ZonePoint</code>
	 *
	 * @param g the Graphics object for painting
	 * @param x x coordinate of top left corner of the drawing region
	 * @param y y coordinate of top left corner of the drawing region
	 * @param zp the point to which the arrow will point
	 */
	public static void paintIcon (Graphics g, int x, int y, ZonePoint zp) {
		// clear!
		/*g.setColor(Color.white);
		g.fillRect(x, y, x + 32, y + 32);*/

		LocationService gps = getActiveGPS();
		if (gps == null) return;

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		double bearing = zp.bearing(gps.getLatitude(), gps.getLongitude());
		double heading = (gps.getHeading() / 180.0) * Math.PI;
		g2.rotate(heading, x + 16, y + 16);
		paintArrow(g2, bearing, x, y, 32, 32);
	}

	/** Paints a compass image with arrow pointing to the specified <code>ZonePoint</code>
	 *
	 * @param g the Graphics object for painting
	 * @param x x coordinate of top left corner of the drawing region
	 * @param y y coordinate of top left corner of the drawing region*
	 * @param width width of drawing region
	 * @param height height of drawing region
	 * @param zp the point to which the arrow will point
	 */
	public static void paintCompass (Graphics g, int x, int y, int width, int height, ZonePoint zp) {
		LocationService gps = getActiveGPS();
		if (gps == null) return;

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double bearing = zp.bearing(gps.getLatitude(), gps.getLongitude());

		double heading = (gps.getHeading() / 180.0) * Math.PI;

		int w2 = x + width / 2, h2 = y + height / 2;
		g2.rotate(heading, x + w2, y + h2);
		g2.setColor(Color.black);
		g2.drawArc(x, y, width - 1, height - 1, 0, 360);
		int h = g2.getFontMetrics().getHeight();
		int we = g2.getFontMetrics().charWidth('E');
		int wn = g2.getFontMetrics().charWidth('N');
		int ws = g2.getFontMetrics().charWidth('S');
		g2.drawString("N", w2 - wn / 2, y + h);
		g2.drawString("S", w2 - ws / 2, y + height - 3);
		g2.drawString("W", x + 2, h2 + h/2);
		g2.drawString("E", x + width - we - 3, h2 + h/2);

		paintArrow(g2, bearing, x, y, width, height);

	}
}
