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

public class PebbleGame extends Thread {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11;
	public static final int PLAYER_LIMIT = 100;
	private static int numPlayers;
	private static int numPebblesPerBag;
	
	private Player[] players;
	private volatile BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private volatile WhiteBag[] wBags;
	
	private volatile boolean finishedGame = false;
	protected boolean bagEmpty = false;
	protected boolean checkingWin = false;
	public static CyclicBarrier gate;
	
	public static final Object lock = new Object();
	public ArrayList<Pebble> winningHand;
	
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
		System.exit(0);
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
			try {
				players[i] = new Player(i+1);
			} catch (BagUnderflowException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Pebble> mainLoop() {
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
		return winningHand;
	}
	
	protected synchronized Pebble pickUp(int i) throws BagUnderflowException {
		return bBags[i].takePebble();
	}
	
	protected synchronized void drop(int i, Pebble p) throws BagOverflowException {
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
	
	protected void finishGame(ArrayList<Pebble> hand) {
		finishedGame=true;
		winningHand = hand;
	}
	
	public boolean isDone() { return finishedGame; }
	
	public static int getNumPlayers() { return PebbleGame.numPlayers; }
	public static int numPebblesPerBag() { return PebbleGame.numPebblesPerBag; }
	
	public class Player extends Thread {
		
		volatile ArrayList<Pebble> hand;
		int indexLastHand;
		int playerIndex;
		
		public Player(int playerIndex) throws BagUnderflowException {
			hand = new ArrayList<Pebble>();
			this.playerIndex = playerIndex;
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
				if(PebbleGame.this.isDone()) break;
				drop();
				while (PebbleGame.this.bagEmpty) {
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
			}
			
		}
		
		private synchronized void pickUp()  {
			try {
				synchronized(lock) {
					int i = -1;
					try {
						i = chooseRandomBag();
						hand.add(PebbleGame.this.pickUp(i));
						this.indexLastHand = i;
					} catch (BagUnderflowException e) {
						// picked bag is empty
						PebbleGame.this.bagEmpty = true;
						while (PebbleGame.this.bagEmpty) {
							fillBags();
							lock.notifyAll();
						}
					}
					if (i>=0 && PebbleGame.this.bBags[i].pebbles.size()==0) {
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
			try {
				PebbleGame.this.drop(i,hand.remove(j));
			} catch (BagOverflowException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		private void checkWeight() {
			if (handWeight()==100 && hand.size()==10) {
				synchronized(lock) {
					checkingWin = true;
					PebbleGame.this.finishGame(hand);
					System.out.println(playerIndex);
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
