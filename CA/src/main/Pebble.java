package main;

import java.util.Random;

public class Pebble {
	
	private int weight;
	
	public Pebble() { // assign random weight to pebble on creation
		Random r = new Random();
		weight = (int) (r.nextDouble()*30) + 1;
	}
	
	public void setWeight(int weight) { if(weight>0 && weight<=30) this.weight = weight; } // if reading from a file, can change weight value
	
	public synchronized int getWeight() { return weight; }

}
