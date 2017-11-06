package test;

import java.util.ArrayList;

import main.Pebble;
import main.PebbleGame;

public class TestPebbleGame {
	
	public static void main(String[] args) {
		PebbleGame game = new PebbleGame(1);
		game.mainLoop();
		System.out.println("Finished");
	}
	
	public static void printHand(ArrayList<Pebble> pebbles) {
		System.out.println("");
		for(Pebble p:pebbles) {
			System.out.print(p.getWeight()+",");
		}
	}
	
}
