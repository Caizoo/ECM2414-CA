package main;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import exceptions.BagOverflowException;
import exceptions.BagUnderflowException;
import test.TestPebbleGameMain;

public class PebbleGame extends Thread {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11;
	public static final int PLAYER_LIMIT = 100;
	private static int numPlayers;
	private static int numPebblesPerBag;
	
	private Player[] players;
	private volatile BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private volatile WhiteBag[] wBags;
	
	private boolean[] pushDownFlags = {false,false,false};
	private boolean finishedGame = false;
	protected boolean bagEmpty = false;
	protected boolean checkingWin = false;
	protected static CyclicBarrier gate;
	
	public static final Object lock = new Object();
	
	public static void main(String[] args) {
		System.out.println("Please enter number of players to start game");
		int x = 0;
		try {
			Scanner sc = new Scanner(System.in);
			x = sc.nextInt();
			if(x <= 0 || x > PebbleGame.PLAYER_LIMIT) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException | InputMismatchException e) {
			System.out.println("Illegal number of players, must be 1 or greater, please try again");
			System.exit(1);
		}
		gate = new CyclicBarrier(x);
		PebbleGame game = new PebbleGame(x);
		game.mainLoop();
	}
	
	public PebbleGame(int numPlayers) {
		PebbleGame.numPlayers = numPlayers;
		PebbleGame.numPebblesPerBag = PebbleGame.numPlayers * PebbleGame.PLAYER_PEBBLE_MULTIPLIER;
		bBags = new BlackBag[3];
		wBags = new WhiteBag[3];
		for (int i = 0;i<3;i++) {
			bBags[i] = new BlackBag(BlackBagType.getType(i),PebbleGame.numPebblesPerBag);
			wBags[i] = new WhiteBag(WhiteBagType.getType(i),PebbleGame.numPebblesPerBag);
		}
		players = new Player[numPlayers];
		for (int i = 0;i<numPlayers;i++) {
			players[i] = new Player();
		}
	}
	
	public void mainLoop() {
		for (Player p:players) {
			p.start();
		}
		try {
			gate.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (!finishedGame) {
			// main loop
		}
		System.exit(0);
	}
	
	protected synchronized Pebble pickUp(int i) {
		try {
			return bBags[i].takePebble();
		} catch (BagUnderflowException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected synchronized void drop(int i, Pebble p) {
		wBags[i].pebbles.add(p);
	}
	
	
	private synchronized void fillBags() {
		if (bagEmpty) {
			for (int i=0;i<3;i++) {
				if (bBags[i].pebbles.size()==0) {
					bBags[i].fillPebbles(wBags[i].takeAllPebbles());
				}
			}
			bagEmpty = false;
		}
	}
	
	private void priorityCheck() {
		
	}
	
	protected void finishGame(ArrayList<Pebble> hand) {
		finishedGame=true;
		TestPebbleGameMain.printHand(hand);
		int i = 0;
		for (Pebble p:hand) {
			i += p.getWeight();
		}
		System.out.print(i);
		System.out.print(Thread.currentThread().getName());
		System.exit(0);
	}
	
	public boolean isDone() { return finishedGame; }
	
	protected synchronized Pebble takePebble(int i) {
		try {
			return bBags[i].takePebble();
		} catch (BagUnderflowException e) {
			e.printStackTrace();
			return null;
		}
		
		// code for player checking empty bag, calls for lockAllThreads then yields to main thread to fill back
	}
	
	protected synchronized void givePebble(WhiteBagType type, Pebble p) {
		try {
			wBags[type.getIndex()].givePebble(p);
		} catch (BagOverflowException e) {
			e.printStackTrace(); // tried to push too many pebbles into bag
		}
	}
	
	public static int getNumPlayers() { return PebbleGame.numPlayers; }
	public static int numPebblesPerBag() { return PebbleGame.numPebblesPerBag; }
	
	public class Player extends Thread {
		
		volatile ArrayList<Pebble> hand;
		int indexLastHand;
		
		public Player() {
			hand = new ArrayList<Pebble>();
			int b = 0;
			for (int i=0;i<10;i++) {
				b = chooseRandomBag();
				hand.add(PebbleGame.this.pickUp(b));
			}
			this.indexLastHand = b;
		}
		
		public void run() {
			try {
				gate.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BrokenBarrierException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mainLoop: 
			while (!PebbleGame.this.isDone()) {
				System.out.println(Thread.currentThread().getName());
				drop();
				while (PebbleGame.this.bagEmpty || PebbleGame.this.checkingWin) {
					if (PebbleGame.this.isDone()) break mainLoop;
					try {
						synchronized(lock) {
							lock.wait();
						}
					} catch (InterruptedException e) {
						// do nothing
					}
				}
				pickUp();
				checkWeight();
				// test calls
			}
		}
		
		private synchronized void pickUp() {
			try {
				synchronized(lock) {
					int i = chooseRandomBag();
					hand.add(PebbleGame.this.pickUp(i));
					this.indexLastHand = i;
					if (PebbleGame.this.bBags[i].pebbles.size()==0) {
						// picked bag is empty
						PebbleGame.this.bagEmpty = true;
						while (PebbleGame.this.bagEmpty) {
							fillBags();
							lock.notifyAll();
						}
					}
				}
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
				
		}
		
		private synchronized void drop() {
			Random r = new Random();
			int i = (int)(r.nextDouble()*3);
			int j = (int)(r.nextDouble()*hand.size());
			PebbleGame.this.drop(i,hand.remove(j));
		}
		
		private void checkWeight() {
			if (handWeight()==100 && hand.size()==10) {
				synchronized(lock) {
					checkingWin = true;
					PebbleGame.this.finishGame(hand);
				}
			}
		}
		
		public int handWeight() {
			int weight = 0;
			for (Pebble p:hand) {
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
