/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import org.junit.AfterClass;
import org.junit.Before;
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
	static ArrayList<Pebble> pebbles;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBefore() throws Exception {
		pebbles = new ArrayList<Pebble>();
	}

	/**
	 * Test method for {@link main.PebbleGame#PebbleGame(int)}.
	 */
	@Test
	public void testPebbleGame() {
		for(int i=0;i<165;i++) { pebbles.add(new Pebble()); }
		game = new PebbleGame(pebbles,5); // make a game with 5 players
		game.gate = new CyclicBarrier(6);
		assertNotNull(game); // make sure the game isn't null
		ArrayList<Pebble> winningHand = game.mainLoop(); // return the winning hand after game loop has finished
		assertTrue(game.isDone());
		int totalWeight = 0;
		for(Pebble p:winningHand) { // make sure winning hand is actually a winning hand
			totalWeight += p.getWeight();
		}
		assertEquals(100,totalWeight);
		assertEquals(10,winningHand.size());
	} 

	/**
	 * Test method for {@link main.PebbleGame#getNumPlayers()}.
	 */
	@Test
	public void testGetNumPlayers() {
		for(int i=0;i<66;i++) { pebbles.add(new Pebble()); }
		game = new PebbleGame(pebbles,2);
		assertEquals(game.getNumPlayers(),2); // make sure this is set correctly
	}

	/**
	 * Test method for {@link main.PebbleGame#numPebblesPerBag()}.
	 */
	@Test
	public void testNumPebblesPerBag() {
		for(int i=0;i<66;i++) { pebbles.add(new Pebble()); }
		game = new PebbleGame(pebbles,2);
		assertEquals(game.numPebblesPerBag(),22); // make sure max pebbles per bag is indeed 11 times number of players
	}

}
