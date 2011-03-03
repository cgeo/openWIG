/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.krka.kahlua.vm.LuaTable;

/**
 *
 * @author matejcik
 */
public class ZonePointTest {

    public ZonePointTest() {
    }

	@BeforeClass
	public static void setUpClass () throws Exception {
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
	}

	@Test
	public void testCopy () {
		System.out.println("copy");
		ZonePoint z = null;
		ZonePoint expResult = null;
		ZonePoint result = ZonePoint.copy(z);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testTranslate () {
		System.out.println("translate");
		double angle = 0.0;
		Distance distance = null;
		ZonePoint instance = new ZonePoint();
		ZonePoint expResult = null;
		ZonePoint result = instance.translate(angle, distance);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDiff () {
		System.out.println("diff");
		ZonePoint z = null;
		ZonePoint instance = new ZonePoint();
		instance.diff(z);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSync () {
		System.out.println("sync");
		ZonePoint z = null;
		ZonePoint instance = new ZonePoint();
		instance.sync(z);
		fail("The test case is a prototype.");
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

	@Test
	public void testBearing () {
		System.out.println("bearing");
		double lat = 0.0;
		double lon = 0.0;
		ZonePoint instance = new ZonePoint();
		double expResult = 0.0;
		double result = instance.bearing(lat, lon);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testAngle2azimuth () {
		System.out.println("angle2azimuth");
		double angle = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.angle2azimuth(angle);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testAzimuth2angle () {
		System.out.println("azimuth2angle");
		double azim = 0.0;
		double expResult = 0.0;
		double result = ZonePoint.azimuth2angle(azim);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetMetatable () {
		System.out.println("setMetatable");
		LuaTable metatable = null;
		ZonePoint instance = new ZonePoint();
		instance.setMetatable(metatable);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMetatable () {
		System.out.println("getMetatable");
		ZonePoint instance = new ZonePoint();
		LuaTable expResult = null;
		LuaTable result = instance.getMetatable();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawset () {
		System.out.println("rawset");
		Object key = null;
		Object value = null;
		ZonePoint instance = new ZonePoint();
		instance.rawset(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawget () {
		System.out.println("rawget");
		Object key = null;
		ZonePoint instance = new ZonePoint();
		Object expResult = null;
		Object result = instance.rawget(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testNext () {
		System.out.println("next");
		Object key = null;
		ZonePoint instance = new ZonePoint();
		Object expResult = null;
		Object result = instance.next(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLen () {
		System.out.println("len");
		ZonePoint instance = new ZonePoint();
		int expResult = 0;
		int result = instance.len();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testUpdateWeakSettings () {
		System.out.println("updateWeakSettings");
		boolean weakKeys = false;
		boolean weakValues = false;
		ZonePoint instance = new ZonePoint();
		instance.updateWeakSettings(weakKeys, weakValues);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		ZonePoint instance = new ZonePoint();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		ZonePoint instance = new ZonePoint();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

	@Test
	public void testToString () {
		System.out.println("toString");
		ZonePoint instance = new ZonePoint();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}