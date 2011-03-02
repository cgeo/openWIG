/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.io.DataInputStream;
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
public class CartridgeTest {

    public CartridgeTest() {
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
	 * Test of register method, of class Cartridge.
	 */ @Test
	public void testRegister () {
		System.out.println("register");
		Cartridge.register();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of luaTostring method, of class Cartridge.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Cartridge instance = new Cartridge();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of walk method, of class Cartridge.
	 */ @Test
	public void testWalk () {
		System.out.println("walk");
		ZonePoint zp = null;
		Cartridge instance = new Cartridge();
		instance.walk(zp);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class Cartridge.
	 */ @Test
	public void testTick () {
		System.out.println("tick");
		Cartridge instance = new Cartridge();
		instance.tick();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleZones method, of class Cartridge.
	 */ @Test
	public void testVisibleZones () {
		System.out.println("visibleZones");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleZones();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleThings method, of class Cartridge.
	 */ @Test
	public void testVisibleThings () {
		System.out.println("visibleThings");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleThings();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of currentThings method, of class Cartridge.
	 */ @Test
	public void testCurrentThings () {
		System.out.println("currentThings");
		Cartridge instance = new Cartridge();
		LuaTable expResult = null;
		LuaTable result = instance.currentThings();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleUniversalActions method, of class Cartridge.
	 */ @Test
	public void testVisibleUniversalActions () {
		System.out.println("visibleUniversalActions");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleUniversalActions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleTasks method, of class Cartridge.
	 */ @Test
	public void testVisibleTasks () {
		System.out.println("visibleTasks");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleTasks();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addObject method, of class Cartridge.
	 */ @Test
	public void testAddObject () {
		System.out.println("addObject");
		Object o = null;
		Cartridge instance = new Cartridge();
		instance.addObject(o);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deserialize method, of class Cartridge.
	 */ @Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Cartridge instance = new Cartridge();
		instance.deserialize(in);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}