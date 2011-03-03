/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.util.Vector;
import org.junit.AfterClass;
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

	@Test
	public void testAssociateWithTargets () {
		System.out.println("associateWithTargets");
		Action instance = new Action();
		instance.associateWithTargets();
		fail("The test case is a prototype.");
	}

	@Test
	public void testDissociateFromTargets () {
		System.out.println("dissociateFromTargets");
		Action instance = new Action();
		instance.dissociateFromTargets();
		fail("The test case is a prototype.");
	}

	@Test
	public void testLuaTostring () {
		System.out.println("luaTostring");
		Action instance = new Action();
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
		Action instance = new Action();
		instance.setItem(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleTargets () {
		System.out.println("visibleTargets");
		Container where = null;
		Action instance = new Action();
		int expResult = 0;
		int result = instance.visibleTargets(where);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testTargetsInside () {
		System.out.println("targetsInside");
		LuaTable v = null;
		Action instance = new Action();
		int expResult = 0;
		int result = instance.targetsInside(v);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsTarget () {
		System.out.println("isTarget");
		Thing t = null;
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isTarget(t);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetTargets () {
		System.out.println("getTargets");
		Action instance = new Action();
		Vector expResult = null;
		Vector result = instance.getTargets();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetName () {
		System.out.println("getName");
		Action instance = new Action();
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testHasParameter () {
		System.out.println("hasParameter");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.hasParameter();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsEnabled () {
		System.out.println("isEnabled");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isEnabled();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsUniversal () {
		System.out.println("isUniversal");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isUniversal();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetActor () {
		System.out.println("setActor");
		Thing a = null;
		Action instance = new Action();
		instance.setActor(a);
		fail("The test case is a prototype.");
	}

	@Test
	public void testGetActor () {
		System.out.println("getActor");
		Action instance = new Action();
		Thing expResult = null;
		Thing result = instance.getActor();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testIsReciprocal () {
		System.out.println("isReciprocal");
		Action instance = new Action();
		boolean expResult = false;
		boolean result = instance.isReciprocal();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}