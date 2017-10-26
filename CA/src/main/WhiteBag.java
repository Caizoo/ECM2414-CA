package main;

import java.util.ArrayList;

public class WhiteBag extends Bag {

	WhiteBagType type;
	
	public WhiteBag(WhiteBagType type) {
		super();
		this.type = type;
	}
	
	public synchronized void givePebble(Pebble p) {
		if(pebbles.size()>=PebbleGame.numPebblesPerBag()) {
			System.out.println("too many pebbles in this bag");
		}
		pebbles.add(p);
	}
	
	public synchronized ArrayList<Pebble> takeAllPebbles() {
		ArrayList<Pebble> p = super.pebbles;
		super.pebbles = new ArrayList<Pebble>();
		return p;
	}
	
}
