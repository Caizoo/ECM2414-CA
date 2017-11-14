package main;

import java.util.ArrayList;
import java.util.Random;

import exceptions.BagUnderflowException;

public class BlackBag extends Bag {

	BlackBagType type;
	
	public BlackBag(BlackBagType type, int numPebbles) {
		super(numPebbles);
		this.type = type;
		for(int i = 0;i<numPebbles;i++) {
			super.pebbles.add(new Pebble());
		}
	}
	
	public synchronized Pebble takePebble() throws BagUnderflowException {
		Random r = new Random();
		int index = (int)(r.nextDouble()*pebbles.size());
		try {
			return pebbles.remove(index);
		} catch (IndexOutOfBoundsException e) {
			throw new BagUnderflowException();
		}
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
	
	public BlackBagType getType() { return this.type; }
	public int getNumPebbles() { return this.numPebblesPerBag; }
	public ArrayList<Pebble> getPebbles() { return this.pebbles; }
	
}
