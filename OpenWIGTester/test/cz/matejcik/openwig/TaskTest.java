/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

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
public class TaskTest {

    public TaskTest() {
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
	 * Test of isVisible method, of class Task.
	 */ @Test
	public void testIsVisible () {
		System.out.println("isVisible");
		Task instance = new Task();
		boolean expResult = false;
		boolean result = instance.isVisible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isComplete method, of class Task.
	 */ @Test
	public void testIsComplete () {
		System.out.println("isComplete");
		Task instance = new Task();
		boolean expResult = false;
		boolean result = instance.isComplete();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of state method, of class Task.
	 */ @Test
	public void testState () {
		System.out.println("state");
		Task instance = new Task();
		int expResult = 0;
		int result = instance.state();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of luaTostring method, of class Task.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Task instance = new Task();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class Task.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Task instance = new Task();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}