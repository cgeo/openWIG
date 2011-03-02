/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

	/**
	 * Test of luaTostring method, of class Zone.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Zone instance = new Zone();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isVisible method, of class Zone.
	 */ @Test
	public void testIsVisible () {
		System.out.println("isVisible");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.isVisible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleToPlayer method, of class Zone.
	 */ @Test
	public void testVisibleToPlayer () {
		System.out.println("visibleToPlayer");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.visibleToPlayer();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLocated method, of class Zone.
	 */ @Test
	public void testIsLocated () {
		System.out.println("isLocated");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.isLocated();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class Zone.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Zone instance = new Zone();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class Zone.
	 */ @Test
	public void testTick () {
		System.out.println("tick");
		Zone instance = new Zone();
		instance.tick();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of walk method, of class Zone.
	 */ @Test
	public void testWalk () {
		System.out.println("walk");
		ZonePoint z = null;
		Zone instance = new Zone();
		instance.walk(z);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of showThings method, of class Zone.
	 */ @Test
	public void testShowThings () {
		System.out.println("showThings");
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.showThings();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleThings method, of class Zone.
	 */ @Test
	public void testVisibleThings () {
		System.out.println("visibleThings");
		Zone instance = new Zone();
		int expResult = 0;
		int result = instance.visibleThings();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of collectThings method, of class Zone.
	 */ @Test
	public void testCollectThings () {
		System.out.println("collectThings");
		LuaTable c = null;
		Zone instance = new Zone();
		instance.collectThings(c);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of contains method, of class Zone.
	 */ @Test
	public void testContains () {
		System.out.println("contains");
		Thing t = null;
		Zone instance = new Zone();
		boolean expResult = false;
		boolean result = instance.contains(t);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of serialize method, of class Zone.
	 */ @Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Zone instance = new Zone();
		instance.serialize(out);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deserialize method, of class Zone.
	 */ @Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Zone instance = new Zone();
		instance.deserialize(in);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}