/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.platform.LocationService;
import cz.matejcik.openwig.platform.UI;
import java.io.OutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaTable;

/**
 *
 * @author matejcik
 */
public class EngineTest {

    public EngineTest() {
    }

	@BeforeClass
	public static void setUpClass () throws Exception {
	}

	@AfterClass
	public static void tearDownClass () throws Exception {
	}

	@Test
	public void testNewInstance () throws Exception {
		System.out.println("newInstance");
		CartridgeFile cf = null;
		OutputStream log = null;
		UI ui = null;
		LocationService service = null;
		Engine expResult = null;
		Engine result = Engine.newInstance(cf, log, ui, service);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testStart () {
		System.out.println("start");
		Engine instance = null;
		instance.start();
		fail("The test case is a prototype.");
	}

	@Test
	public void testRestore () {
		System.out.println("restore");
		Engine instance = null;
		instance.restore();
		fail("The test case is a prototype.");
	}

	@Test
	public void testRun () {
		System.out.println("run");
		Engine instance = null;
		instance.run();
		fail("The test case is a prototype.");
	}

	@Test
	public void testStacktrace () {
		System.out.println("stacktrace");
		Throwable e = null;
		Engine.stacktrace(e);
		fail("The test case is a prototype.");
	}

	@Test
	public void testKill () {
		System.out.println("kill");
		Engine.kill();
		fail("The test case is a prototype.");
	}

	@Test
	public void testMessage () {
		System.out.println("message");
		LuaTable message = null;
		Engine.message(message);
		fail("The test case is a prototype.");
	}

	@Test
	public void testDialog () {
		System.out.println("dialog");
		String[] texts = null;
		Media[] media = null;
		Engine.dialog(texts, media);
		fail("The test case is a prototype.");
	}

	@Test
	public void testInput () {
		System.out.println("input");
		EventTable input = null;
		Engine.input(input);
		fail("The test case is a prototype.");
	}

	@Test
	public void testCallEvent () {
		System.out.println("callEvent");
		EventTable subject = null;
		String name = "";
		Object param = null;
		Engine.callEvent(subject, name, param);
		fail("The test case is a prototype.");
	}

	@Test
	public void testInvokeCallback () {
		System.out.println("invokeCallback");
		LuaClosure callback = null;
		Object value = null;
		Engine.invokeCallback(callback, value);
		fail("The test case is a prototype.");
	}

	@Test
	public void testMediaFile () throws Exception {
		System.out.println("mediaFile");
		Media media = null;
		byte[] expResult = null;
		byte[] result = Engine.mediaFile(media);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testLog () {
		System.out.println("log");
		String s = "";
		int level = 0;
		Engine.log(s, level);
		fail("The test case is a prototype.");
	}

	@Test
	public void testRemoveHtml () {
		System.out.println("removeHtml");
		String s = "";
		String expResult = "";
		String result = Engine.removeHtml(s);
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

	@Test
	public void testStore () {
		System.out.println("store");
		Engine instance = null;
		instance.store();
		fail("The test case is a prototype.");
	}

	@Test
	public void testRequestSync () {
		System.out.println("requestSync");
		Engine.requestSync();
		fail("The test case is a prototype.");
	}

}