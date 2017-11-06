package main;

import java.util.ArrayList;
import java.util.Collection;

public class WhiteBag extends Bag {

	WhiteBagType type;
	
	public WhiteBag(WhiteBagType type) {
		super();
		this.type = type;
	}
	
	public synchronized void givePebble(Pebble p) {
		if(pebbles.size()>PebbleGame.numPebblesPerBag()) {
			System.out.println("too many pebbles in this bag");
		}
		pebbles.add(p);
	}
	
	public synchronized ArrayList<Pebble> takeAllPebbles() {
		ArrayList<Pebble> p = new ArrayList<Pebble>();
		for(Pebble ps:super.pebbles) {
			p.add(ps);
		}
		super.pebbles.clear();
		return p;
	}
	
}
