/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
public class MediaTest {

    public MediaTest() {
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
	 * Test of reset method, of class Media.
	 */ @Test
	public void testReset () {
		System.out.println("reset");
		Media.reset();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of serialize method, of class Media.
	 */ @Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Media instance = new Media();
		instance.serialize(out);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deserialize method, of class Media.
	 */ @Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Media instance = new Media();
		instance.deserialize(in);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setItem method, of class Media.
	 */ @Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Media instance = new Media();
		instance.setItem(key, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of jarFilename method, of class Media.
	 */ @Test
	public void testJarFilename () {
		System.out.println("jarFilename");
		Media instance = new Media();
		String expResult = "";
		String result = instance.jarFilename();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of play method, of class Media.
	 */ @Test
	public void testPlay () {
		System.out.println("play");
		Media instance = new Media();
		instance.play();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}