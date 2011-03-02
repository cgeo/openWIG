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
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;

/**
 *
 * @author matejcik
 */
public class WherigoLibTest {

    public WherigoLibTest() {
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
	 * Test of register method, of class WherigoLib.
	 */ @Test
	public void testRegister () {
		System.out.println("register");
		LuaState state = null;
		WherigoLib.register(state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class WherigoLib.
	 */ @Test
	public void testToString () {
		System.out.println("toString");
		WherigoLib instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of call method, of class WherigoLib.
	 */ @Test
	public void testCall () {
		System.out.println("call");
		LuaCallFrame callFrame = null;
		int nArguments = 0;
		WherigoLib instance = null;
		int expResult = 0;
		int result = instance.call(callFrame, nArguments);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}