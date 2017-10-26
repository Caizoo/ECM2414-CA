package main;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
	
	protected ArrayList<Pebble> pebbles;
	
	public Bag() {
		pebbles = new ArrayList();
	}
	
	
	
	
	// test functions
	public void printPebbles() {
		System.out.print(this.getClass().getName()+": ");
		for(Pebble p:pebbles) {
			System.out.print(p.getWeight()+",");
		}
	}

}
