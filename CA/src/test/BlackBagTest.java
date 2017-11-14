/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
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
		assertEquals(11, x.getNumPebbles());
		assertEquals(BlackBagType.X, x.getType());
	}
	
	/**
	 * Test method for {@link main.BlackBag#fillPebbles(java.util.ArrayList)}.
	 */
	@Test
	public void testFillPebbles() {
		x.fillPebbles(pebbles);
		assertEquals(pebbles,x.getPebbles());
	}

	/**
	 * Test method for {@link main.BlackBag#takePebble()}.
	 */
	@Test
	public void testTakePebble() {
		x.fillPebbles(pebbles);
		ArrayList<Pebble> tempPebbles = new ArrayList<Pebble>();
		for(Pebble p:pebbles) {
			tempPebbles.add(p);
		}
		Pebble takenPebble = null;
		try {
			takenPebble = x.takePebble();
		} catch (BagUnderflowException e) {
			e.printStackTrace();
		}
		assertEquals(10,x.getPebbles().size());
		assertNotEquals(tempPebbles,x.getPebbles());
		assertTrue(tempPebbles.remove(takenPebble));
		for(int i=0;i<x.getPebbles().size();i++) {
			assertEquals(x.getPebbles().get(i),tempPebbles.get(i));
		}
		x.fillPebbles(pebbles);
		boolean bagUnderflowed = false;
		for(int i=0;i<20;i++) {
			try {
				x.takePebble();
			} catch (BagUnderflowException e) {
				bagUnderflowed = true;
			}
		}
		assertTrue(bagUnderflowed);
	}

	/**
	 * Test method for {@link main.BlackBag#getTotalWeight()}.
	 */
	@Test
	public void testGetTotalWeight() {
		pebbles = new ArrayList<Pebble>();
		for(int i=0;i<11;i++) { pebbles.add(new Pebble()); }
		
		x.fillPebbles(pebbles);
		int totalX = 0;
		int totalP = 0;
		for(Pebble p:x.getPebbles()) {
			totalX += p.getWeight();
		}
		for(Pebble p:pebbles) {
			totalP += p.getWeight();
		}
		assertEquals(x.getTotalWeight(),totalX);
		assertEquals(x.getTotalWeight(),totalP);
		assertNotEquals(x.getTotalWeight(),0);
	}

}
