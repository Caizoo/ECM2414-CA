/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
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
		assertEquals(11, a.getNumPebbles());
		assertEquals(WhiteBagType.A, a.getType());
	}

	/**
	 * Test method for {@link main.WhiteBag#givePebble(main.Pebble)}.
	 */
	@Test
	public void testGivePebble() {
		Pebble p = new Pebble();
		try {
			a.givePebble(p);
		} catch (BagOverflowException e) {
			
		}
		assertEquals(1, a.getPebbles().size());
		assertEquals(a.getPebbles().get(0), p);
		boolean hasOverflowed = false;
		for(int i=0;i<20;i++) {
			try {
				a.givePebble(new Pebble());
			} catch (BagOverflowException e) {
				assertNotEquals(e.getLocalizedMessage(), null);
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
		for(int i=0;i<11;i++) {
			try {
				a.givePebble(new Pebble());
			} catch (BagOverflowException e) {
				// do nothing
			}
		}
		ArrayList<Pebble> tempBag = (ArrayList<Pebble>) a.getPebbles().clone();
		assertEquals(tempBag, a.getPebbles());
		ArrayList<Pebble> takenPebbles = a.takeAllPebbles();
		assertNotEquals(takenPebbles, a.getPebbles());
		assertNotEquals(tempBag, a.getPebbles());
		assertEquals(takenPebbles, tempBag);
	}
	
	/**
	 * Test method for {@link main.WhiteBag#getTotalWeight()}.
	 */
	@Test
	public void testGetTotalWeight() {
		pebbles = new ArrayList<Pebble>();
		for(int i=0;i<11;i++) { pebbles.add(new Pebble()); }
		
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
