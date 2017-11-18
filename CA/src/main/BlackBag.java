package main;

import java.util.ArrayList;
import java.util.Random;

import exceptions.BagUnderflowException;

public class BlackBag extends Bag {

	BlackBagType type;
	
	public BlackBag(BlackBagType type, int numPebbles) {
		super(numPebbles); // set max number of pebbles
		this.type = type;
	}
	
	public synchronized Pebble takePebble() throws BagUnderflowException {
		Random r = new Random();
		int index = (int)(r.nextDouble()*pebbles.size());
		try {
			return pebbles.remove(index);
		} catch (IndexOutOfBoundsException e) { // if trying to remove from empty bag, throw underflow exception
			throw new BagUnderflowException();
		}
	}
	
	public void fillPebbles(ArrayList<Pebble> p) { // simply change the arraylist variable to point to new arraylist of pebbles
		super.pebbles = p;
	}
	
	public int getTotalWeight() {
		int x = 0;
		for(Pebble p:super.pebbles) {
			x += p.getWeight();
		}
		return x;
	}
	
	public BlackBagType getType() { return this.type; }
	public int getNumPebbles() { return this.numPebblesPerBag; }
	public ArrayList<Pebble> getPebbles() { return this.pebbles; }
	
}
