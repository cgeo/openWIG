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
public class MediaTest {

    public MediaTest() {
    }

	@BeforeClass
	public static void setUpClass () throws Exception {
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
	}

	@Test
	public void testReset () {
		System.out.println("reset");
		Media.reset();
		fail("The test case is a prototype.");
	}

	@Test
	public void testSerialize () throws Exception {
		System.out.println("serialize");
		DataOutputStream out = null;
		Media instance = new Media();
		instance.serialize(out);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDeserialize () throws Exception {
		System.out.println("deserialize");
		DataInputStream in = null;
		Media instance = new Media();
		instance.deserialize(in);
		fail("The test case is a prototype.");
	}

	@Test
	public void testSetItem () {
		System.out.println("setItem");
		String key = "";
		Object value = null;
		Media instance = new Media();
		instance.setItem(key, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testJarFilename () {
		System.out.println("jarFilename");
		Media instance = new Media();
		String expResult = "";
		String result = instance.jarFilename();
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testPlay () {
		System.out.println("play");
		Media instance = new Media();
		instance.play();
		fail("The test case is a prototype.");
	}

}