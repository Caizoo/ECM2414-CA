package main;

import java.util.ArrayList;
import java.util.Collection;

import exceptions.BagOverflowException;

public class WhiteBag extends Bag {

	WhiteBagType type;
	
	public WhiteBag(WhiteBagType type,int numPebbles) {
		super(numPebbles); // set max number of pebbles
		this.type = type;
	}
	
	public synchronized void givePebble(Pebble p) throws BagOverflowException {
		if(pebbles.size()>=numPebblesPerBag) {
			throw new BagOverflowException(); // if trying to push a pebble into a full bag
		}else{
			pebbles.add(p);
		}
	}
	
	public synchronized ArrayList<Pebble> takeAllPebbles() {
		ArrayList<Pebble> p = (ArrayList<Pebble>)(super.pebbles.clone()); // return list of pebble objects and clear list
		super.pebbles.clear();
		return p;
	}
	
	public int getTotalWeight() { // get total weight of the bag
		int x = 0;
		for(Pebble p:super.pebbles) {
			x += p.getWeight();
		}
		return x;
	}
		
	public WhiteBagType getType() { return this.type; }
	public int getNumPebbles() { return numPebblesPerBag; }
	public ArrayList<Pebble> getPebbles() { return pebbles; }
}
