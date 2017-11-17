/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.BlackBag;
import main.BlackBagType;

/**
 * @author cai-b
 *
 */
public class BlackBagTypeTest {
	static BlackBag x,y,z;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		x = new BlackBag(BlackBagType.X,11);
		y = new BlackBag(BlackBagType.Y,11);
		z = new BlackBag(BlackBagType.Z,11);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		x = null;
		y = null;
		z = null;
	}

	/**
	 * Test method for {@link main.BlackBagType#BlackBagType(int)}.
	 */
	@Test
	public void testBlackBagType() { // assert the type is correct
		assertEquals(x.getType(), BlackBagType.X);
		assertEquals(y.getType(), BlackBagType.Y);
		assertEquals(z.getType(), BlackBagType.Z);	
	}

	/**
	 * Test method for {@link main.BlackBagType#getIndex()}.
	 */
	@Test
	public void testGetIndex() { // assert the get index function works correctly
		assertEquals(0, x.getType().getIndex());
		assertEquals(1, y.getType().getIndex());
		assertEquals(2, z.getType().getIndex());
	}

	/**
	 * Test method for {@link main.BlackBagType#getType(int)}.
	 */
	@Test
	public void testGetType() { // assert get type from index works correctly
		assertEquals(x.getType(), BlackBagType.getType(0));
		assertEquals(y.getType(), BlackBagType.getType(1));
		assertEquals(z.getType(), BlackBagType.getType(2));
	}

}
