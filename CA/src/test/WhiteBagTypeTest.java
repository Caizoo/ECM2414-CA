/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.Pebble;
import main.WhiteBag;
import main.WhiteBagType;

/**
 * @author cai-b
 *
 */
public class WhiteBagTypeTest {
	static WhiteBag a,b,c;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		a = new WhiteBag(WhiteBagType.A,11);
		b = new WhiteBag(WhiteBagType.B,11);
		c = new WhiteBag(WhiteBagType.C,11);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		a = null;
		b = null;
		c = null;
	}

	/**
	 * Test method for {@link main.WhiteBagType#WhiteBagType(int)}.
	 */
	@Test
	public void testWhiteBagType() {
		assertEquals(a.getType(), WhiteBagType.A);
		assertEquals(b.getType(), WhiteBagType.B);
		assertEquals(c.getType(), WhiteBagType.C);
	}

	/**
	 * Test method for {@link main.WhiteBagType#getIndex()}.
	 */
	@Test
	public void testGetIndex() {
		assertEquals(0, a.getType().getIndex());
		assertEquals(1, b.getType().getIndex());
		assertEquals(2, c.getType().getIndex());
	}

	/**
	 * Test method for {@link main.WhiteBagType#getType(int)}.
	 */
	@Test
	public void testGetType() {
		assertEquals(a.getType(), WhiteBagType.getType(0));
		assertEquals(b.getType(), WhiteBagType.getType(1));
		assertEquals(c.getType(), WhiteBagType.getType(2));
	}

}
