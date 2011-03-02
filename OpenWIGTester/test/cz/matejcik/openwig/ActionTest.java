/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.util.Vector;
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
public class ActionTest {

    public ActionTest() {
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
	 * Test of associateWithTargets method, of class Action.
	 */ @Test
	public void testAssociateWithTargets () {
		System.out.println("associateWithTargets");
		Action instance = new Action();
		instance.associateWithTargets();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of dissociateFromTargets method, of class Action.
	 */ @Test
	public void testDissociateFromTargets () {
		System.out.println("dissociateFromTargets");
		Action instance = new Action();
		instance.dissociateFromTargets();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of luaTostring method, of class Action.
	 */ @Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Action instance = new Action();
		String expResult = "";
		String result = instance.luaTostring();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class Action.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Action instance = new Action();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of visibleTargets method, of class Action.
	 */ @Test
	public void testVisibleTargets () {
		System.out.println("visibleTargets");
		Container where = null;
		Action instance = new Action();
		int expResult = 0;
		int result = instance.visibleTargets(where);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of targetsInside method, of class Action.
	 */ @Test
	public void testTargetsInside () {
		System.out.println("targetsInside");
		LuaTable v = null;
		Action instance = new Action();
		int expResult = 0;
		int result = instance.targetsInside(v);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isTarget method, of class Action.
	 */ @Test
	public void testIsTarget () {
		System.out.println("isTarget");
		Thing t = null;
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isTarget(t);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTargets method, of class Action.
	 */ @Test
	public void testGetTargets () {
		System.out.println("getTargets");
		Action instance = new Action();
		Vector expResult = null;
		Vector result = instance.getTargets();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Action.
	 */ @Test
	public void testGetName () {
		System.out.println("getName");
		Action instance = new Action();
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hasParameter method, of class Action.
	 */ @Test
	public void testHasParameter () {
		System.out.println("hasParameter");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.hasParameter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isEnabled method, of class Action.
	 */ @Test
	public void testIsEnabled () {
		System.out.println("isEnabled");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isEnabled();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isUniversal method, of class Action.
	 */ @Test
	public void testIsUniversal () {
		System.out.println("isUniversal");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isUniversal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setActor method, of class Action.
	 */ @Test
	public void testSetActor () {
		System.out.println("setActor");
		Thing a = null;
		Action instance = new Action();
		instance.setActor(a);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getActor method, of class Action.
	 */ @Test
	public void testGetActor () {
		System.out.println("getActor");
		Action instance = new Action();
		Thing expResult = null;
		Thing result = instance.getActor();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isReciprocal method, of class Action.
	 */ @Test
	public void testIsReciprocal () {
		System.out.println("isReciprocal");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isReciprocal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}