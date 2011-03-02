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
public class EventTableTest {

    public EventTableTest() {
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
	 * Test of luaTostring method, of class EventTable.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		EventTable instance = new EventTable();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of serialize method, of class EventTable.
	 */ @Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		EventTable instance = new EventTable();
		instance.serialize(out);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deserialize method, of class EventTable.
	 */ @Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		EventTable instance = new EventTable();
		instance.deserialize(in);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMedia method, of class EventTable.
	 */ @Test
	public void testGetMedia () throws Exception {
		System.out.println("getMedia");
		EventTable instance = new EventTable();
		byte[] expResult = null;
		byte[] result = instance.getMedia();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIcon method, of class EventTable.
	 */ @Test
	public void testGetIcon () throws Exception {
		System.out.println("getIcon");
		EventTable instance = new EventTable();
		byte[] expResult = null;
		byte[] result = instance.getIcon();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isVisible method, of class EventTable.
	 */ @Test
	public void testIsVisible () {
		System.out.println("isVisible");
		EventTable instance = new EventTable();
		boolean expResult = false;
		boolean result = instance.isVisible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPosition method, of class EventTable.
	 */ @Test
	public void testSetPosition () {
		System.out.println("setPosition");
		ZonePoint location = null;
		EventTable instance = new EventTable();
		instance.setPosition(location);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLocated method, of class EventTable.
	 */ @Test
	public void testIsLocated () {
		System.out.println("isLocated");
		EventTable instance = new EventTable();
		boolean expResult = false;
		boolean result = instance.isLocated();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class EventTable.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		EventTable instance = new EventTable();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getItem method, of class EventTable.
	 */ @Test
	public void testGetItem () {
		System.out.println("getItem");
		String key = "";
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.getItem(key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTable method, of class EventTable.
	 */ @Test
	public void testSetTable () {
		System.out.println("setTable");
		LuaTable table = null;
		EventTable instance = new EventTable();
		instance.setTable(table);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of callEvent method, of class EventTable.
	 */ @Test
	public void testCallEvent () {
		System.out.println("callEvent");
		String name = "";
		Object param = null;
		EventTable instance = new EventTable();
		instance.callEvent(name, param);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hasEvent method, of class EventTable.
	 */ @Test
	public void testHasEvent () {
		System.out.println("hasEvent");
		String name = "";
		EventTable instance = new EventTable();
		boolean expResult = false;
		boolean result = instance.hasEvent(name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class EventTable.
	 */ @Test
	public void testToString () {
		System.out.println("toString");
		EventTable instance = new EventTable();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rawset method, of class EventTable.
	 */ @Test
	public void testRawset () {
		System.out.println("rawset");
		Object key = null;
		Object value = null;
		EventTable instance = new EventTable();
		instance.rawset(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMetatable method, of class EventTable.
	 */ @Test
	public void testSetMetatable () {
		System.out.println("setMetatable");
		LuaTable metatable = null;
		EventTable instance = new EventTable();
		instance.setMetatable(metatable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMetatable method, of class EventTable.
	 */ @Test
	public void testGetMetatable () {
		System.out.println("getMetatable");
		EventTable instance = new EventTable();
		LuaTable expResult = null;
		LuaTable result = instance.getMetatable();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rawget method, of class EventTable.
	 */ @Test
	public void testRawget () {
		System.out.println("rawget");
		Object key = null;
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.rawget(key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of next method, of class EventTable.
	 */ @Test
	public void testNext () {
		System.out.println("next");
		Object key = null;
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.next(key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of len method, of class EventTable.
	 */ @Test
	public void testLen () {
		System.out.println("len");
		EventTable instance = new EventTable();
		int expResult = 0;
		int result = instance.len();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}