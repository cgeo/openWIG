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

/**
 *
 * @author matejcik
 */
public class TimerTest {

    public TimerTest() {
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
		Timer.register();
		fail("The test case is a prototype.");
	}

	@Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Timer instance = new Timer();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Timer instance = new Timer();
		instance.setItem(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testStart () {
		System.out.println("start");
		Timer instance = new Timer();
		instance.start();
		fail("The test case is a prototype.");
	}

	@Test
	public void testStop () {
		System.out.println("stop");
		Timer instance = new Timer();
		instance.stop();
		fail("The test case is a prototype.");
	}

	@Test
	public void testTick () {
		System.out.println("tick");
		Timer instance = new Timer();
		instance.tick();
		fail("The test case is a prototype.");
	}

	@Test
	public void testUpdateRemaining () {
		System.out.println("updateRemaining");
		Timer instance = new Timer();
		instance.updateRemaining();
		fail("The test case is a prototype.");
	}

	@Test
	public void testKill () {
		System.out.println("kill");
		Timer.kill();
		fail("The test case is a prototype.");
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Timer instance = new Timer();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Timer instance = new Timer();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

}