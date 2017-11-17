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

import exceptions.BagOverflowException;
import main.Pebble;
import main.WhiteBag;
import main.WhiteBagType;

/**
 * @author cai-b
 *
 */
public class WhiteBagTest {
	
	static WhiteBag a;
	static ArrayList<Pebble> pebbles;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		a = new WhiteBag(WhiteBagType.A,11); // 11 max pebbles for an instance of 1 player game 
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
		a = null;
		pebbles = null;
	}

	/**
	 * Test method for {@link main.WhiteBag#WhiteBag(main.WhiteBagType)}.
	 */
	@Test
	public void testWhiteBag() {
		assertEquals(11, a.getNumPebbles()); // assert number of pebbles is correct and bag type is correct
		assertEquals(WhiteBagType.A, a.getType());
	}

	/**
	 * Test method for {@link main.WhiteBag#givePebble(main.Pebble)}.
	 */
	@Test
	public void testGivePebble() {
		Pebble p = new Pebble();
		try {
			a.givePebble(p); // try to give pebble
		} catch (BagOverflowException e) {
			
		}
		assertEquals(1, a.getPebbles().size()); // assert a single pebble has been given to white bag
		assertEquals(a.getPebbles().get(0), p); // assert it's the same pebble
		boolean hasOverflowed = false;
		for(int i=0;i<20;i++) { // try and push 20 pebbles into a bag fit only for 11
			try {
				a.givePebble(new Pebble());
			} catch (BagOverflowException e) {
				hasOverflowed = true;
			}
		}
		assertTrue(hasOverflowed); // assert exception has been thrown if tried to over-fill bag
		assertEquals(a.getNumPebbles(), a.getPebbles().size()); // assert no extra pebbles have been added past limit
	}

	/**
	 * Test method for {@link main.WhiteBag#takeAllPebbles()}.
	 */
	@Test
	public void testTakeAllPebbles() {
		for(int i=0;i<11;i++) { // fill the bag completely
			try {
				a.givePebble(new Pebble());
			} catch (BagOverflowException e) {
				// do nothing
			}
		}
		@SuppressWarnings("unchecked")
		ArrayList<Pebble> tempBag = (ArrayList<Pebble>) a.getPebbles().clone(); // make a copy of the list, same pebble objects
		assertEquals(tempBag, a.getPebbles()); // make sure they're equal
		ArrayList<Pebble> takenPebbles = a.takeAllPebbles(); // take all pebbles
		assertNotEquals(takenPebbles, a.getPebbles()); 
		assertNotEquals(tempBag, a.getPebbles());
		assertEquals(takenPebbles, tempBag); // make sure the taken list and original list in bag are the same
		assertEquals(0,a.getPebbles().size()); // make sure bag is now empty
	}
	
	/**
	 * Test method for {@link main.WhiteBag#getTotalWeight()}.
	 */
	@Test
	public void testGetTotalWeight() {
		for(int i=0;i<11;i++) {
			try {
				a.givePebble(new Pebble());
			} catch (BagOverflowException e) {
				// do nothing
			}
		}
		int totalX = 0;
		for(Pebble p:a.getPebbles()) {
			totalX += p.getWeight();
		}
		assertEquals(a.getTotalWeight(),totalX);
		assertNotEquals(a.getTotalWeight(),0);
	}

}
