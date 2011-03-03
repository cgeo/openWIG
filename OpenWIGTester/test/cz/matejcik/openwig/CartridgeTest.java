/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.io.DataInputStream;
import org.junit.AfterClass;
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

	@Test
	public void testRegister () {
		System.out.println("register");
		Cartridge.register();
		fail("The test case is a prototype.");
	}

	@Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Cartridge instance = new Cartridge();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testWalk () {
		System.out.println("walk");
		ZonePoint zp = null;
		Cartridge instance = new Cartridge();
		instance.walk(zp);
		fail("The test case is a prototype.");
	}

	@Test
	public void testTick () {
		System.out.println("tick");
		Cartridge instance = new Cartridge();
		instance.tick();
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleZones () {
		System.out.println("visibleZones");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleZones();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleThings () {
		System.out.println("visibleThings");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleThings();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testCurrentThings () {
		System.out.println("currentThings");
		Cartridge instance = new Cartridge();
		LuaTable expResult = null;
		LuaTable result = instance.currentThings();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleUniversalActions () {
		System.out.println("visibleUniversalActions");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleUniversalActions();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleTasks () {
		System.out.println("visibleTasks");
		Cartridge instance = new Cartridge();
		int expResult = 0;
		int result = instance.visibleTasks();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testAddObject () {
		System.out.println("addObject");
		Object o = null;
		Cartridge instance = new Cartridge();
		instance.addObject(o);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Cartridge instance = new Cartridge();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

}