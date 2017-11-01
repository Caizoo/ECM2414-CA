package main;

import java.util.ArrayList;

public class Player extends Thread {
	
	PebbleGame game;
	public boolean lock = false;
	
	ArrayList<Pebble> hand;
	
	public Player(PebbleGame game) {
		this.game = game;
		hand = new ArrayList<Pebble>();
		for(int i = 0;i<10;i++) {
			hand.add(game.pickUp());
		}
	}
	
	public void run() {
		while(!game.isDone()) {
			while(!lock) {
				pickUp();
				if(lock) break;
				drop();
			}
		}
		
	}
	
	private void pickUp() {
		
	}
	
	private void drop() {
		
	}

}
