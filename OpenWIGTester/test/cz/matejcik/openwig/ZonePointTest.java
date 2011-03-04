/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.matejcik.openwig;

import cz.matejcik.openwig.testmockups.TestEngine;
import java.io.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author matejcik
 */
public class ZonePointTest {
	/* TODO repeat random tests N times with different randoms */

	@BeforeClass
	public static void setUpClass () throws Exception {
		TestEngine.initialize();
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
		TestEngine.kill();
	}

	private static final double FUZZ = 1.0e-5;
	
	private ZonePoint randomZonePoint ()
	{
		return new ZonePoint(Math.random() * 360 - 180, Math.random() * 360 - 180, Math.random() * 2000);
	}

	private void assertEqualZP (ZonePoint a, ZonePoint b, double tolerance)
	{
		assertEquals(a.latitude, b.latitude, tolerance);
		assertEquals(a.longitude, b.longitude, tolerance);
		assertEquals(a.altitude.value, b.altitude.value, tolerance);
	}

	@Test
	public void testCopy () {
		assertNull(ZonePoint.copy(null));

		ZonePoint z = randomZonePoint();
		ZonePoint c = ZonePoint.copy(z);
		assertNotSame(z, c);
		assertNotSame(z.altitude, c.altitude);
		assertEqualZP(z, c, 0);
	}

	@Test
	public void testTranslate () {
		ZonePoint z = randomZonePoint();
		ZonePoint t = z.translate(Math.random() * 360, 0);
		assertNotSame(z, t);
		assertEqualZP(z, t, 0);

		double dist = Math.random() * 2000;
		double angle = Math.random() * 360;

		t = z.translate(angle, dist);
		ZonePoint zz = t.translate(angle - 180, dist);
		assertEqualZP(z, zz, FUZZ);

		/* TODO test known translation values */
	}

	@Test
	public void testSync () {
		ZonePoint z = new ZonePoint(Math.random(), Math.random(), 0);
		ZonePoint y = new ZonePoint(Math.random(), Math.random(), 0);
		z.sync(y);
		assertEqualZP(y, z, 0);
	}

	@Test
	public void testLat2m () {
		System.out.println("lat2m");
		double degrees = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.lat2m(degrees);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLon2m () {
		System.out.println("lon2m");
		double latitude = 0.0;
		double degrees = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.lon2m(latitude, degrees);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testM2lat () {
		System.out.println("m2lat");
		double metres = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.m2lat(metres);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testM2lon () {
		System.out.println("m2lon");
		double latitude = 0.0;
		double metres = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.m2lon(latitude, metres);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDistance_double_double () {
		System.out.println("distance");
		double lat = 0.0;
		double lon = 0.0;
		ZonePoint instance = new ZonePoint();
		double expResult = 0.0;
		double result = instance.distance(lat, lon);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDistance_ZonePoint () {
		System.out.println("distance");
		ZonePoint z = null;
		ZonePoint instance = new ZonePoint();
		double expResult = 0.0;
		double result = instance.distance(z);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDistance_4args () {
		System.out.println("distance");
		double lat1 = 0.0;
		double lon1 = 0.0;
		double lat2 = 0.0;
		double lon2 = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.distance(lat1, lon1, lat2, lon2);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testFriendlyDistance () {
		System.out.println("friendlyDistance");
		double lat = 0.0;
		double lon = 0.0;
		ZonePoint instance = new ZonePoint();
		String expResult = "";
		String result = instance.friendlyDistance(lat, lon);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testMakeFriendlyDistance () {
		System.out.println("makeFriendlyDistance");
		double dist = 0.0;
		String expResult = "";
		String result = ZonePoint.makeFriendlyDistance(dist);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testMakeFriendlyAngle () {
		System.out.println("makeFriendlyAngle");
		double angle = 0.0;
		String expResult = "";
		String result = ZonePoint.makeFriendlyAngle(angle);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testMakeFriendlyLatitude () {
		System.out.println("makeFriendlyLatitude");
		double angle = 0.0;
		String expResult = "";
		String result = ZonePoint.makeFriendlyLatitude(angle);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testMakeFriendlyLongitude () {
		System.out.println("makeFriendlyLongitude");
		double angle = 0.0;
		String expResult = "";
		String result = ZonePoint.makeFriendlyLongitude(angle);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	private void assertAngle (ZonePoint z, double azim)
	{
		ZonePoint zz = z.translate(azim, 100);
		assertEquals(azim, ZonePoint.angle2azimuth(zz.bearing(z.latitude, z.longitude)), FUZZ);
	}

	@Test
	public void testBearing () {
		ZonePoint z = randomZonePoint();
		double angle = Math.random() * 360;
		assertAngle(z, angle);
		assertAngle(z, 0);
		assertAngle(z, 90);
		assertAngle(z, 180);
		assertAngle(z, 270);
	}

	@Test
	public void testAzimuths () {
		double angle = (Math.random() * Math.PI * 2) - Math.PI;
		double azimuth = Math.random() * 360;

		// reflexivity
		double az = ZonePoint.angle2azimuth(angle);
		double ra = ZonePoint.azimuth2angle(az);
		assertEquals(angle, ra, FUZZ);

		double an = ZonePoint.azimuth2angle(azimuth);
		ra = ZonePoint.angle2azimuth(an);
		assertEquals(azimuth, ra, FUZZ);

		// known values
		assertEquals(Math.PI / 2, ZonePoint.azimuth2angle(0), FUZZ);
		assertEquals(0, ZonePoint.azimuth2angle(90), FUZZ);
		assertEquals(0, ZonePoint.angle2azimuth(Math.PI / 2), FUZZ);

		// sign handling, overflow, normalization
		assertEquals(ZonePoint.azimuth2angle(180), ZonePoint.azimuth2angle(-180), FUZZ);
		assertEquals(ZonePoint.azimuth2angle(270), ZonePoint.azimuth2angle(-90), FUZZ);
		assertEquals(0, ZonePoint.azimuth2angle(360 * 5 + 90), FUZZ);
		assertEquals(Math.PI / 2, ZonePoint.azimuth2angle(-360 * 5), FUZZ);

		assertEquals(ZonePoint.angle2azimuth(-Math.PI), ZonePoint.angle2azimuth(Math.PI), FUZZ);
		assertEquals(ZonePoint.angle2azimuth(3 * Math.PI / 2), ZonePoint.angle2azimuth(- Math.PI / 2), FUZZ);
		assertEquals(270, ZonePoint.angle2azimuth(7 * Math.PI), FUZZ);
		assertEquals(0, ZonePoint.angle2azimuth(-11 * Math.PI / 2), FUZZ);
	}

	@Test
	public void testRawset () {
		ZonePoint z = randomZonePoint();
		double lat = Math.random();
		double lon = Math.random();
		Distance alt = new Distance(Math.random(), null);
		z.rawset("latitude", lat);
		assertEquals(z.latitude, lat, 0);
		z.rawset("longitude", lon);
		assertEquals(z.longitude, lon, 0);
		z.rawset("altitude", alt);
		assertSame(z.altitude, alt);

		z.rawset("nonsense", true);
		z.rawset(null, null);
	}

	@Test
	public void testRawget () {
		ZonePoint z = randomZonePoint();
		double lat = (Double)z.rawget("latitude");
		assertEquals(z.latitude, lat, 0);
		double lon = (Double)z.rawget("longitude");
		assertEquals(z.longitude, lon, 0);
		Distance alt = (Distance)z.rawget("altitude");
		assertEquals(z.altitude, alt);

		z.rawset("nonsense", true);
		assertNull(z.rawget("nonsense"));
		assertNull(z.rawget(null));
	}

	private void assertSerializationReverses (ZonePoint z)
	throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bos);
		z.serialize(out);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		DataInputStream in = new DataInputStream(bis);
		ZonePoint zz = new ZonePoint();
		zz.deserialize(in);

		assertEqualZP(z, zz, 0);
	}

	@Test
	public void testSerialize () throws Exception {
		ZonePoint z = randomZonePoint();
		assertSerializationReverses(z);
		ZonePoint y = new ZonePoint();
		assertSerializationReverses(y);
	}
}
