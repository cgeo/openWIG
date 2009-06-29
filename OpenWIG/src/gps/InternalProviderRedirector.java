package gps;

public class InternalProviderRedirector {
	public static LocationService getInstance() throws Exception
	{ return new InternalProvider(); }

}
