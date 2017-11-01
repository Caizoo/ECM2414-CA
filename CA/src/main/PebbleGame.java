package main;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class PebbleGame {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11;
	public static final int PLAYER_LIMIT = 20;
	private static int numPlayers;
	private static int numPebblesPerBag;
	
	private BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private WhiteBag[] wBags;
	
	private boolean[] pushDownFlags = {false,false,false};
	private boolean lockAllThreads = false;
	private boolean finishedGame = false;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Please enter number of players to start game");
		int x = 0;
		try {
			Scanner sc = new Scanner(System.in);
			x = sc.nextInt();
			if(x <= 0 || x >= PebbleGame.PLAYER_LIMIT) {
				throw new NumberFormatException();
			}
		}catch (NumberFormatException | InputMismatchException e) {
			System.out.println("Illegal number of players, must be 1 or greater, please try again");
			System.exit(1);
		}
		PebbleGame game = new PebbleGame(x);
		game.mainLoop();
	}
	
	public PebbleGame(int numPlayers) {
		PebbleGame.numPlayers = numPlayers;
		PebbleGame.numPebblesPerBag = PebbleGame.numPlayers * PebbleGame.PLAYER_PEBBLE_MULTIPLIER;
		bBags = new BlackBag[3];
		wBags = new WhiteBag[3];
		for(int i = 0;i<3;i++) {
			bBags[i] = new BlackBag(BlackBagType.getType(i));
		}
	}
	
	public void mainLoop() {
		while(true) {
			System.out.println("Looping");
		}
	}
	
	protected synchronized Pebble pickUp(int i) {
		
		return bBags[i].takePebble();
	}
	
	protected synchronized void drop() {
		
	}
	
	private void fillBags() {
		
	}
	
	private void priorityCheck() {
		
	}
	
	public boolean isDone() { return finishedGame; }
	
	protected synchronized Pebble takePebble(int i) {
		return bBags[i].takePebble();
		
		// code for player checking empty bag, calls for lockAllThreads then yields to main thread to fill back
	}
	
	protected synchronized void givePebble(WhiteBagType type, Pebble p) {
		wBags[type.getIndex()].givePebble(p);
	}
	
	public static int getNumPlayers() { return PebbleGame.numPlayers; }
	public static int numPebblesPerBag() { return PebbleGame.numPebblesPerBag; }
	
	public class Player extends Thread {

		public boolean lock = false;
		
		ArrayList<Pebble> hand;
		int indexLastHand;
		
		public Player() {
			hand = new ArrayList<Pebble>();
			int b = 0;
			for(int i = 0;i<10;i++) {
				b = chooseRandomBag();
				hand.add(PebbleGame.this.pickUp(b));
			}
			this.indexLastHand = b;
		}
		
		public void run() {
			while(!PebbleGame.this.isDone()) {
				while(!lock) {
					drop();
					if(lock) break;
					pickUp();
					checkWeight();
				}
			}
			
		}
		
		private void pickUp() {
			int i = chooseRandomBag();
			hand.add(PebbleGame.this.pickUp(i));
			this.indexLastHand = i;
			if(PebbleGame.this.bBags[i].pebbles.size()==0) {
				// picked bag is empty
			}
		}
		
		private void drop() {
			
		}
		
		private void checkWeight() {
			if(handWeight()==100 && hand.size()==10) {
				
			}
		}
		
		public int handWeight() {
			int weight = 0;
			for(Pebble p:hand) {
				weight += p.getWeight();
			}
			return weight;
		}
		
		private int chooseRandomBag() {
			Random r = new Random();
			return (int)(r.nextDouble()*3);
		}
		
		
	}

}
