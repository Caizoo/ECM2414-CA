/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.Pebble;

/**
 * @author cai-b
 *
 */
public class PebbleTest {
	
	static Pebble p;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p = new Pebble();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		p = null;
	}

	/**
	 * Test method for {@link main.Pebble#Pebble()}.
	 */
	@Test
	public void testPebble() {
		assertNotNull(p);
	}

	/**
	 * Test method for {@link main.Pebble#getWeight()}.
	 */
	@Test
	public void testGetWeight() {
		assertTrue(p.getWeight()>0); // assert weight of pebble is between 1 and 30
		assertTrue(p.getWeight()<=30);
	}

}
