package main;

import java.util.ArrayList;
import java.util.Random;

public class BlackBag extends Bag {

	BlackBagType type;
	
	public BlackBag(BlackBagType type) {
		super();
		this.type = type;
		for(int i = 0;i<PebbleGame.numPebblesPerBag();i++) {
			super.pebbles.add(new Pebble());
		}
	}
	
	public synchronized Pebble takePebble() {
		Random r = new Random();
		int index = (int)(r.nextDouble()*pebbles.size());
		return pebbles.remove(index);
	}
	
	public void fillPebbles(ArrayList<Pebble> p) {
		super.pebbles = p;
	}
	
	
	// test functions
	public int getTotalWeight() {
		int x = 0;
		for(Pebble p:super.pebbles) {
			x += p.getWeight();
		}
		return x;
	}
	
}
