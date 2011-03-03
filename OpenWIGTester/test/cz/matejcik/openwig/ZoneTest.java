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
public class ZoneTest {

    public ZoneTest() {
    }

	@BeforeClass
	public static void setUpClass () throws Exception {
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
	}

	@Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Zone instance = new Zone();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsVisible () {
		System.out.println("isVisible");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.isVisible();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleToPlayer () {
		System.out.println("visibleToPlayer");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.visibleToPlayer();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsLocated () {
		System.out.println("isLocated");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.isLocated();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Zone instance = new Zone();
		instance.setItem(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testTick () {
		System.out.println("tick");
		Zone instance = new Zone();
		instance.tick();
		fail("The test case is a prototype.");
	}

	@Test
	public void testWalk () {
		System.out.println("walk");
		ZonePoint z = null;
		Zone instance = new Zone();
		instance.walk(z);
		fail("The test case is a prototype.");
	}

	@Test
	public void testShowThings () {
		System.out.println("showThings");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.showThings();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleThings () {
		System.out.println("visibleThings");
		Zone instance = new Zone();
		int expResult = 0;
		int result = instance.visibleThings();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testCollectThings () {
		System.out.println("collectThings");
		LuaTable c = null;
		Zone instance = new Zone();
		instance.collectThings(c);
		fail("The test case is a prototype.");
	}

	@Test
	public void testContains () {
		System.out.println("contains");
		Thing t = null;
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.contains(t);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Zone instance = new Zone();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Zone instance = new Zone();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

}