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

/**
 *
 * @author matejcik
 */
public class ThingTest {

    public ThingTest() {
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
	 * Test of luaTostring method, of class Thing.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Thing instance = new Thing();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of serialize method, of class Thing.
	 */ @Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Thing instance = new Thing();
		instance.serialize(out);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deserialize method, of class Thing.
	 */ @Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Thing instance = new Thing();
		instance.deserialize(in);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class Thing.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Thing instance = new Thing();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleActions method, of class Thing.
	 */ @Test
	public void testVisibleActions () {
		System.out.println("visibleActions");
		Thing instance = new Thing();
		int expResult = 0;
		int result = instance.visibleActions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isItem method, of class Thing.
	 */ @Test
	public void testIsItem () {
		System.out.println("isItem");
		Thing instance = new Thing();
		boolean expResult = false;
		boolean result = instance.isItem();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCharacter method, of class Thing.
	 */ @Test
	public void testIsCharacter () {
		System.out.println("isCharacter");
		Thing instance = new Thing();
		boolean expResult = false;
		boolean result = instance.isCharacter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}