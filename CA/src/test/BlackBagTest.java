/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.BagUnderflowException;
import main.BlackBag;
import main.BlackBagType;
import main.Pebble;

/**
 * @author cai-b
 *
 */
public class BlackBagTest {
	
	static BlackBag x;
	static ArrayList<Pebble> pebbles;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		x = new BlackBag(BlackBagType.X,11); // 11 max pebbles for an instance of 1 player game
	}
	
	@Before
	public void setUpBefore() throws Exception { // before each method, create a list of pebbles to use
		pebbles = new ArrayList<Pebble>();
		for(int i=0;i<11;i++) {
			pebbles.add(new Pebble());
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		x = null;
		pebbles = null;
	}

	/**
	 * Test method for {@link main.BlackBag#BlackBag(main.BlackBagType)}.
	 */
	@Test
	public void testBlackBag() {
		assertEquals(11, x.getNumPebbles()); // assert number of pebbles is correct and bag type is correct
		assertEquals(BlackBagType.X, x.getType());
	}
	
	/**
	 * Test method for {@link main.BlackBag#fillPebbles(java.util.ArrayList)}.
	 */
	@Test
	public void testFillPebbles() {
		x.fillPebbles(pebbles);
		assertEquals(pebbles,x.getPebbles()); // test that giving bag list of pebbles fills the bag with that list
	}

	/**
	 * Test method for {@link main.BlackBag#takePebble()}.
	 */
	@Test
	public void testTakePebble() { 
		x.fillPebbles(pebbles); // fill bag with pebbles
		ArrayList<Pebble> tempPebbles = new ArrayList<Pebble>();
		for(Pebble p:pebbles) {
			tempPebbles.add(p); // list of the same pebble objects
		}
		Pebble takenPebble = null;
		try {
			takenPebble = x.takePebble(); // attempt to take a pebble
		} catch (BagUnderflowException e) {
			e.printStackTrace();
		}
		assertEquals(10,x.getPebbles().size()); // make sure a pebble is taken
		assertNotEquals(tempPebbles,x.getPebbles());
		assertTrue(tempPebbles.remove(takenPebble)); // make sure the taken pebble was indeed in the original list
		for(int i=0;i<x.getPebbles().size();i++) {
			assertEquals(x.getPebbles().get(i),tempPebbles.get(i)); // make sure the rest of the bag hasn't changed
		}
		x.fillPebbles(pebbles); // refill
		boolean bagUnderflowed = false;
		for(int i=0;i<20;i++) { // try and take a pebble 20 times from a bag with 11 pebbles
			try {
				x.takePebble();
			} catch (BagUnderflowException e) {
				bagUnderflowed = true;
			}
		}
		assertTrue(bagUnderflowed); // assert that the exception has indeed been thrown
	}

	/**
	 * Test method for {@link main.BlackBag#getTotalWeight()}.
	 */
	@Test
	public void testGetTotalWeight() {	
		x.fillPebbles(pebbles); // fill bag
		int totalX = 0;
		int totalP = 0;
		for(Pebble p:x.getPebbles()) { // pebbles in bag
			totalX += p.getWeight();
		}
		for(Pebble p:pebbles) { // pebbles in list
			totalP += p.getWeight();
		}
		assertEquals(x.getTotalWeight(),totalX); // assert that the get total weight function works
		assertEquals(x.getTotalWeight(),totalP);
		assertNotEquals(x.getTotalWeight(),0);
	}

}
