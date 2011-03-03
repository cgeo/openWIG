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
public class DistanceTest {

    public DistanceTest() {
    }

	@BeforeClass
	public static void setUpClass () throws Exception {
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Distance instance = new Distance();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Distance instance = new Distance();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRegister () {
		System.out.println("register");
		Distance.register();
		fail("The test case is a prototype.");
	}

	@Test
	public void testCopy () {
		System.out.println("copy");
		Distance d = null;
		Distance expResult = null;
		Distance result = Distance.copy(d);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetValue () {
		System.out.println("setValue");
		double value = 0.0;
		String unit = "";
		Distance instance = new Distance();
		instance.setValue(value, unit);
		fail("The test case is a prototype.");
	}

	@Test
	public void testConvert () {
		System.out.println("convert");
		double value = 0.0;
		String unit = "";
		double expResult = 0.0;
		double result = Distance.convert(value, unit);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetValue () {
		System.out.println("getValue");
		String unit = "";
		Distance instance = new Distance();
		double expResult = 0.0;
		double result = instance.getValue(unit);
		assertEquals(expResult, result, 0.0);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetMetatable () {
		System.out.println("setMetatable");
		LuaTable metatable = null;
		Distance instance = new Distance();
		instance.setMetatable(metatable);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetMetatable () {
		System.out.println("getMetatable");
		Distance instance = new Distance();
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
		Distance instance = new Distance();
		instance.rawset(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawget () {
		System.out.println("rawget");
		Object key = null;
		Distance instance = new Distance();
		Object expResult = null;
		Object result = instance.rawget(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLen () {
		System.out.println("len");
		Distance instance = new Distance();
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
		Distance instance = new Distance();
		instance.updateWeakSettings(weakKeys, weakValues);
		fail("The test case is a prototype.");
	}

	@Test
	public void testNext () {
		System.out.println("next");
		Object key = null;
		Distance instance = new Distance();
		Object expResult = null;
		Object result = instance.next(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testToString () {
		System.out.println("toString");
		Distance instance = new Distance();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}