package main;

import java.io.Console;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PebbleGame {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11;
	public static final int PLAYER_LIMIT = 20;
	private static int numPlayers;
	
	private BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private WhiteBag[] wBags;
	
	private boolean[] pushDownFlags = {false,false,false};
	private boolean lockAllThreads = false;
	
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
		bBags = new BlackBag[3];
		wBags = new WhiteBag[3];
	}
	
	public void mainLoop() {
		
	}
	
	protected synchronized Pebble pickUp() {
	
		return null;
	}
	
	protected synchronized void drop() {
		
	}
	
	private void fillBags() {
		
	}
	
	private void priorityCheck() {
		
	}
	
	protected synchronized Pebble takePebble(BlackBagType type) {
		return bBags[type.getIndex()].takePebble();
		
		// code for player checking empty bag, calls for lockAllThreads then yields to main thread to fill back
	}
	
	protected synchronized void givePebble(WhiteBagType type, Pebble p) {
		wBags[type.getIndex()].givePebble(p);
	}
	
	public static int getNumPlayers() { return PebbleGame.numPlayers; }
	public static int numPebblesPerBag() { return PebbleGame.numPlayers*PebbleGame.PLAYER_PEBBLE_MULTIPLIER; }
	
	class Player extends Thread {
		
		public void run() {
			
		}
		
		
	}

}
