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
public class ContainerTest {

    public ContainerTest() {
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
		Container.register();
		fail("The test case is a prototype.");
	}

	@Test
	public void testMoveTo () {
		System.out.println("moveTo");
		Container c = null;
		Container instance = new Container();
		instance.moveTo(c);
		fail("The test case is a prototype.");
	}

	@Test
	public void testContains () {
		System.out.println("contains");
		Thing t = null;
		Container instance = new Container();
		boolean expResult = false;
		boolean result = instance.contains(t);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testVisibleToPlayer () {
		System.out.println("visibleToPlayer");
		Container instance = new Container();
		boolean expResult = false;
		boolean result = instance.visibleToPlayer();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Container instance = new Container();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

}