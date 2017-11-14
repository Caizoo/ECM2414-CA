package main;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
	
	protected ArrayList<Pebble> pebbles;
	protected int numPebblesPerBag;
	
	public Bag(int numPebbles) {
		pebbles = new ArrayList();
		numPebblesPerBag = numPebbles;
	}

	// test functions
	public void printPebbles() {
		System.out.print(this.getClass().getName()+": ");
		for(Pebble p:pebbles) {
			System.out.print(p.getWeight()+",");
		}
	}

}
