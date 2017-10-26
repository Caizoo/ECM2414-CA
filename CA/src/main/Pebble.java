package main;

import java.util.Random;

public class Pebble {
	
	private int weight;
	
	public Pebble() {
		Random r = new Random();
		weight = (int) (r.nextDouble()*30) + 1;
	}
	
	
	public synchronized int getWeight() { return weight; }

}
