package openwig;

public class ZonePoint {
	public double latitude;
	public double longitude;
	public double height;
	
	public ZonePoint (double lat, double lon, double h)
	{
		latitude = lat;
		longitude = lon;
		height = h;
	}
	
	public void diff (ZonePoint z) {
		latitude -= z.latitude;
		longitude -= z.longitude;
	}
}
