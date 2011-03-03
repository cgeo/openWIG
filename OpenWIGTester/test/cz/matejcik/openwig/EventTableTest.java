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
public class EventTableTest {

    public EventTableTest() {
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
		EventTable instance = new EventTable();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		EventTable instance = new EventTable();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		EventTable instance = new EventTable();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMedia () throws Exception {
		System.out.println("getMedia");
		EventTable instance = new EventTable();
		byte[] expResult = null;
		byte[] result = instance.getMedia();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetIcon () throws Exception {
		System.out.println("getIcon");
		EventTable instance = new EventTable();
		byte[] expResult = null;
		byte[] result = instance.getIcon();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsVisible () {
		System.out.println("isVisible");
		EventTable instance = new EventTable();
		boolean expResult = false;
		boolean result = instance.isVisible();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetPosition () {
		System.out.println("setPosition");
		ZonePoint location = null;
		EventTable instance = new EventTable();
		instance.setPosition(location);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsLocated () {
		System.out.println("isLocated");
		EventTable instance = new EventTable();
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
		EventTable instance = new EventTable();
		instance.setItem(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetItem () {
		System.out.println("getItem");
		String key = "";
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.getItem(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetTable () {
		System.out.println("setTable");
		LuaTable table = null;
		EventTable instance = new EventTable();
		instance.setTable(table);
		fail("The test case is a prototype.");
	}

	@Test
	public void testCallEvent () {
		System.out.println("callEvent");
		String name = "";
		Object param = null;
		EventTable instance = new EventTable();
		instance.callEvent(name, param);
		fail("The test case is a prototype.");
	}

	@Test
	public void testHasEvent () {
		System.out.println("hasEvent");
		String name = "";
		EventTable instance = new EventTable();
		boolean expResult = false;
		boolean result = instance.hasEvent(name);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testToString () {
		System.out.println("toString");
		EventTable instance = new EventTable();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawset () {
		System.out.println("rawset");
		Object key = null;
		Object value = null;
		EventTable instance = new EventTable();
		instance.rawset(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetMetatable () {
		System.out.println("setMetatable");
		LuaTable metatable = null;
		EventTable instance = new EventTable();
		instance.setMetatable(metatable);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMetatable () {
		System.out.println("getMetatable");
		EventTable instance = new EventTable();
		LuaTable expResult = null;
		LuaTable result = instance.getMetatable();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawget () {
		System.out.println("rawget");
		Object key = null;
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.rawget(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testNext () {
		System.out.println("next");
		Object key = null;
		EventTable instance = new EventTable();
		Object expResult = null;
		Object result = instance.next(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLen () {
		System.out.println("len");
		EventTable instance = new EventTable();
		int expResult = 0;
		int result = instance.len();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}