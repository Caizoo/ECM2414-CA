package main;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import exceptions.BagOverflowException;
import exceptions.BagUnderflowException;

public class PebbleGame extends Thread {
	
	public static final int PLAYER_PEBBLE_MULTIPLIER = 11; // player x this number is how many pebbles per bag are needed at start of game
	public static final int PLAYER_LIMIT = 100; // limit to number of players allowed for performance reasons
	private static int numPlayers;
	private static int numPebblesPerBag;
	
	private Player[] players; // arrays for pebbles and bags
	private volatile BlackBag[] bBags; // 0 - X, 1 - Y, 2 - Z
	private volatile WhiteBag[] wBags;
	
	private volatile boolean finishedGame = false; // if a player has won, switch to true
	protected boolean bagEmpty = false; // true while bags are being filled, locks threads
	protected boolean checkingWin = false; // stops threads from running if a win is being checked
	public static CyclicBarrier gate; // holds all threads from running until all threads are created
	
	public static final Object lock = new Object(); // used to lock threads in a synch block
	public ArrayList<Pebble> winningHand;
	public volatile static List<String> turns; // synchronized list to store player moves to be written to a file at the end
	
	public static void main(String[] args) {
		int x = 0;
		ArrayList<Pebble> pebbles;
		while(true) {
			x = ReadWriteFile.getNumPlayers(); // read in from command line number of players
			if(x<1) continue; // go back if wrong input
			pebbles = ReadWriteFile.readPebbles(x); // read in pebbles from pebble file
			if(pebbles==null) continue;
			break;
		}
		
		gate = new CyclicBarrier(x+1); // gates for all threads plus main thread
		PebbleGame game = new PebbleGame(pebbles,x);
		game.mainLoop();
		try {
			ArrayList<String> outputTurns = new ArrayList<String>(); // arraylist for writing to file
			Iterator<String> itr = turns.iterator();
			synchronized(turns) {
				while(itr.hasNext()) { outputTurns.add(itr.next()); } // copy from synchronized list to arraylist
			}
			ReadWriteFile.writeToFile("gameOutput.txt", outputTurns); // write game output file from arraylist
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static ArrayList<Pebble> splitPebbleList(ArrayList<Pebble> pebbles, int bagIndex) { // split pebble list into 3 for putting into black bags at start of game
		ArrayList<Pebble> rtnPebbles = new ArrayList<Pebble>();
		for(int i=bagIndex*PebbleGame.numPebblesPerBag;i<bagIndex*PebbleGame.numPebblesPerBag+PebbleGame.numPebblesPerBag;i++) {
			rtnPebbles.add(pebbles.get(i));
		}
		return rtnPebbles;
	}
	
	public PebbleGame(ArrayList<Pebble> pebbles,int numPlayers) {
		PebbleGame.numPlayers = numPlayers;
		PebbleGame.numPebblesPerBag = PebbleGame.numPlayers * PebbleGame.PLAYER_PEBBLE_MULTIPLIER; // set player number and number of pebbles per bag
		turns = Collections.synchronizedList(new ArrayList<String>()); // stores printing of players' turns
		bBags = new BlackBag[3];
		wBags = new WhiteBag[3];
		for (int i = 0;i<3;i++) {
			bBags[i] = new BlackBag(BlackBagType.getType(i),PebbleGame.numPebblesPerBag); // make an X,Y and Z bag
			wBags[i] = new WhiteBag(WhiteBagType.getType(i),PebbleGame.numPebblesPerBag); // make an A,B and C bag
		}
		for(int i=0;i<3;i++) {
			bBags[i].fillPebbles(PebbleGame.splitPebbleList(pebbles,i)); // fill black bags with pebbles read in from file
		}
		players = new Player[numPlayers];
		for (int i = 0;i<numPlayers;i++) { // make an array of players
			try {
				players[i] = new Player(i+1);
			} catch (BagUnderflowException e) { 
				e.printStackTrace();
				System.out.println("Not enough pebbles in bags for players"); // this will only happen if there is less than 10 times the amount of pebbles as players
				System.exit(0);
			}
		}
	}
	
	public ArrayList<Pebble> mainLoop() {
		for (Player p:players) {
			p.start();
		}
		try {
			gate.await(); // after players have been made, sets the threads running
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		while (!finishedGame) { // while game is running, main thread does nothing
			yield();
		}
		return winningHand; // returns winning hand when game is done
	}
	
	protected synchronized Pebble pickUp(int i) throws BagUnderflowException { // synchronized pickup method, throws exception if no pebbles in bag
		return bBags[i].takePebble();
	}
	
	protected synchronized void drop(int i, Pebble p) throws BagOverflowException { // synchronized drop method, throws exception if too many pebbles in bag
		wBags[i].pebbles.add(p);
	}
	
	
	private synchronized void fillBags() {
		if (bagEmpty) { // only call if bagEmpty is true and threads are ready to lock and wait
			for (int i=0;i<3;i++) {
				if (bBags[i].pebbles.size()==0) { // if bag is empty, take pebbles from associated white bag and fill
					bBags[i].fillPebbles(wBags[i].takeAllPebbles());
				}
			}
			bagEmpty = false; // threads can unlock
		}
	}
	
	protected void finishGame(ArrayList<Pebble> hand) { // when game is finished, set flag and store winning hand
		finishedGame=true;
		winningHand = hand;
	}
	
	public boolean isDone() { return finishedGame; } // public access method to finishedGame
	
	public static int getNumPlayers() { return PebbleGame.numPlayers; }
	public static int numPebblesPerBag() { return PebbleGame.numPebblesPerBag; }
	
	public class Player extends Thread {
		
		volatile ArrayList<Pebble> hand; // current hand of player
		int indexLastHand; // bag last pebbles was picked up from
		int playerIndex;
		
		public Player(int playerIndex) throws BagUnderflowException {
			hand = new ArrayList<Pebble>();
			this.playerIndex = playerIndex;
			int b = 0;
			for (int i=0;i<10;i++) { // pick up 10 pebbles
				b = chooseRandomBag();
				hand.add(PebbleGame.this.pickUp(b));
			}
			this.indexLastHand = b; // last bag picked up from 
		}
		
		public void run() {
			try {
				gate.await(); // wait for all threads plus main thread before continuing
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BrokenBarrierException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mainLoop: 
			while (!PebbleGame.this.isDone()) { // while game hasn't finished
				if(PebbleGame.this.isDone()) break;
				drop(); 
				while (PebbleGame.this.bagEmpty) { // if bag is empty and being filled by another thread
					if (PebbleGame.this.isDone()) break mainLoop;
					try {
						synchronized(lock) {
							lock.wait(); // lock threads not filling bag
						}
					} catch (InterruptedException e) {
						// do nothing
					}
				}
				pickUp();
				checkWeight(); // check to see if hand is a winning hand
			}
			
		}
		
		private synchronized void pickUp()  {
			boolean looped = false; // if has picked up
			mainLoop:
			while(true&&!looped) {
				try {
					synchronized(lock) { // lock threads from this
						int i = -1;
						try {
							i = chooseRandomBag();
							Pebble p = PebbleGame.this.pickUp(i); // try to pick up a pebble, catch bag underflow exception
							hand.add(p);
							this.indexLastHand = i;
							looped = true; 
							synchronized(turns) { // add details of turn to synchronized list to be written at end of game 
								turns.add("Player "+this.playerIndex+" has drawn "+p.getWeight()+" from bag "+BlackBagType.getBagName(i)+"\n"+"Player "+this.playerIndex+" hand is "+PebbleGame.drawHand(hand));
							}
							turns.add("Player "+this.playerIndex+" has drawn "+p.getWeight()+" from bag "+BlackBagType.getBagName(i)+"\n"+"Player "+this.playerIndex+" hand is "+PebbleGame.drawHand(hand));
							break mainLoop;
						} catch (BagUnderflowException e) {
							// picked bag is empty
							PebbleGame.this.bagEmpty = true;
							while (PebbleGame.this.bagEmpty) {
								fillBags();
								lock.notifyAll(); // unlock all threads when filling is done
							}
						}
						if (i>=0 && PebbleGame.this.bBags[i].pebbles.size()==0) { // if last pebble was picked up, fill bags
							// picked bag is empty
							PebbleGame.this.bagEmpty = true;
							while (PebbleGame.this.bagEmpty) {
								fillBags();
								lock.notifyAll(); // unlock all threads when filling is done
							}
						}
					}
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
				
		}
		
		private synchronized void drop() {
			Random r = new Random();
			int j = (int)(r.nextDouble()*hand.size()); // random and random pebble from hand
			try {
				Pebble p = hand.remove(j); // try and put pebble in white bag
				PebbleGame.this.drop(this.indexLastHand,p);
				synchronized(turns) { // add details of player turn to synchronized list
					turns.add("Player "+this.playerIndex+" has discarded "+p.getWeight()+" to bag "+WhiteBagType.getBagName(indexLastHand)+"\n"+"Player "+this.playerIndex+" hand is "+PebbleGame.drawHand(hand));
				}
			} catch (BagOverflowException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		private void checkWeight() {
			if (handWeight()==100 && hand.size()==10) { // if winning hand, lock to this method so other threads can't wit
				synchronized(turns) {
					checkingWin = true;  // finish game and add end game details to synchronized list
					turns.add("\n\n Player "+this.playerIndex+" has won with hand:\n"+drawHand(this.hand));
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
	
	public static String drawHand(ArrayList<Pebble> hand) { // used to print a hand of pebbles in human-readable format
		String s = "";
		for(Pebble p:hand) {
			s += p.getWeight()+",";
		}
		s = s.substring(0,s.length()-1);
		return s;
	}

}
