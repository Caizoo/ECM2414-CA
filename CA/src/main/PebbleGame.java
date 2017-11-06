package main;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import test.TestPebbleGame;

public class PebbleGame {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11;
	public static final int PLAYER_LIMIT = 20;
	private static int numPlayers;
	private static int numPebblesPerBag;
	
	private Player[] players;
	private BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private WhiteBag[] wBags;
	
	private boolean[] pushDownFlags = {false,false,false};
	protected volatile boolean lockAllThreads = false;
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
			wBags[i] = new WhiteBag(WhiteBagType.getType(i));
		}
		for (int i=0;i<numPebblesPerBag;i++) {
			for (int j=0;j<3;j++) {
				bBags[j].pebbles.add(new Pebble());
			}
		}
		players = new Player[numPlayers];
		for(int i = 0;i<numPlayers;i++) {
			players[i] = new Player();
		}
	}
	
	public void mainLoop() {
		for(Player p:players) {
			p.start();
		}
		while(!finishedGame) {
			fillBags();
		}
		System.exit(0);
	}
	
	protected synchronized Pebble pickUp(int i) {
		return bBags[i].takePebble();
	}
	
	protected synchronized void drop(int i, Pebble p) {
		wBags[i].pebbles.add(p);
	}
	
	
	private void fillBags() {
		if (lockAllThreads) {
			System.out.println("Filling bags");
			for (int i=0;i<3;i++) {
				if (bBags[i].pebbles.size() == 0) {
					bBags[i].fillPebbles(wBags[i].takeAllPebbles());
				}
			}
			lockAllThreads=false;
		}
	}
	
	private void priorityCheck() {
		
	}
	
	protected void finishGame(ArrayList<Pebble> hand) {
		lockAllThreads=true;
		finishedGame=true;
		TestPebbleGame.printHand(hand);
		System.out.println("");
		int i = 0;
		for(Pebble p:hand) {
			i+=p.getWeight();
		}
		System.out.println(i);
		System.exit(0);
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
		
		volatile ArrayList<Pebble> hand;
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
				while(!PebbleGame.this.lockAllThreads) {
					drop();
					TestPebbleGame.printHand(hand);
					if(PebbleGame.this.lockAllThreads) break;
					pickUp();
					TestPebbleGame.printHand(hand);
					checkWeight();
					// test calls
				}
				if (PebbleGame.this.lockAllThreads) yield();
			}
			
		}
		
		private synchronized void pickUp() {
			int i = chooseRandomBag();
			hand.add(PebbleGame.this.pickUp(i));
			this.indexLastHand = i;
			if(PebbleGame.this.bBags[i].pebbles.size()==0) {
				// picked bag is empty
				PebbleGame.this.lockAllThreads = true;
				return;
			}
		}
		
		private synchronized void drop() {
			Random r = new Random();
			int i = (int)(r.nextDouble()*3);
			int j = (int)(r.nextDouble()*hand.size());
			PebbleGame.this.drop(i,hand.remove(j));
		}
		
		private void checkWeight() {
			if(handWeight()==100 && hand.size()==10) {
				PebbleGame.this.finishGame(hand);
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
