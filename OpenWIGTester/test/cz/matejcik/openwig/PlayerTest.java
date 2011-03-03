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

/**
 *
 * @author matejcik
 */
public class PlayerTest {

    public PlayerTest() {
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
		Player.register();
		fail("The test case is a prototype.");
	}

	@Test
	public void testMoveTo () {
		System.out.println("moveTo");
		Container c = null;
		Player instance = new Player();
		instance.moveTo(c);
		fail("The test case is a prototype.");
	}

	@Test
	public void testEnterZone () {
		System.out.println("enterZone");
		Zone z = null;
		Player instance = new Player();
		instance.enterZone(z);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLeaveZone () {
		System.out.println("leaveZone");
		Zone z = null;
		Player instance = new Player();
		instance.leaveZone(z);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Player instance = new Player();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Player instance = new Player();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleThings () {
		System.out.println("visibleThings");
		Player instance = new Player();
		int expResult = 0;
		int result = instance.visibleThings();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRefreshLocation () {
		System.out.println("refreshLocation");
		Player instance = new Player();
		instance.refreshLocation();
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawset () {
		System.out.println("rawset");
		Object key = null;
		Object value = null;
		Player instance = new Player();
		instance.rawset(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRawget () {
		System.out.println("rawget");
		Object key = null;
		Player instance = new Player();
		Object expResult = null;
		Object result = instance.rawget(key);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}