/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.Bag;

/**
 * @author cai-b
 *
 */
public class BagTest {

	static Bag b;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		b = new Bag(11);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		b = null;
	}

	/**
	 * Test method for {@link main.Bag#Bag()}.
	 */
	@Test
	public void testBag() {
		assertNotEquals(b,null); // assert a bag object is created
	}

	/**
	 * Test method for {@link main.Bag#printPebbles()}.
	 */

}
