/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import org.junit.AfterClass;
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

	@Test
	public void testRegister () {
		System.out.println("register");
		LuaState state = null;
		WherigoLib.register(state);
		fail("The test case is a prototype.");
	}

	@Test
	public void testToString () {
		System.out.println("toString");
		WherigoLib instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testCall () {
		System.out.println("call");
		LuaCallFrame callFrame = null;
		int nArguments = 0;
		WherigoLib instance = null;
		int expResult = 0;
		int result = instance.call(callFrame, nArguments);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}