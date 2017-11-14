/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.Pebble;
import main.PebbleGame;

/**
 * @author cai-b
 *
 */
public class PebbleGameTest {
	
	static PebbleGame game;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		game = null;
	}

	/**
	 * Test method for {@link main.PebbleGame#PebbleGame(int)}.
	 */
	@Test
	public void testPebbleGame() {
		game = new PebbleGame(5);
		game.gate = new CyclicBarrier(6);
		assertNotEquals(game,null);
		ArrayList<Pebble> winningHand = game.mainLoop();
		System.out.println(winningHand);
		assertTrue(game.isDone());
		int totalWeight = 0;
		for(Pebble p:winningHand) {
			totalWeight += p.getWeight();
		}
		assertEquals(totalWeight, 100);
	}

	/**
	 * Test method for {@link main.PebbleGame#getNumPlayers()}.
	 */
	@Test
	public void testGetNumPlayers() {
		game = new PebbleGame(2);
		assertEquals(game.getNumPlayers(),2);
	}

	/**
	 * Test method for {@link main.PebbleGame#numPebblesPerBag()}.
	 */
	@Test
	public void testNumPebblesPerBag() {
		game = new PebbleGame(2);
		assertEquals(game.numPebblesPerBag(),22);
	}

}
