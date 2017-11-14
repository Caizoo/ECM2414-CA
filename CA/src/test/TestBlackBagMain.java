package test;

import main.BlackBag;
import main.BlackBagType;
import main.Pebble;
import main.PebbleGame;
import main.WhiteBag;
import main.WhiteBagType;

public class TestBlackBagMain {
	
	public static void main(String[] args) {
		/**
		PebbleGame game = new PebbleGame(2);
		BlackBag b = new BlackBag(BlackBagType.X);
		System.out.println(b);
		System.out.println(b.getTotalWeight());
		b.printPebbles();
		Pebble taken = b.takePebble();
		System.out.println();
		b.printPebbles();
		System.out.println();
		b.printPebbles();
		**/
		
		PebbleGame game = new PebbleGame(2);
		BlackBag b = new BlackBag(BlackBagType.X);
		b.printPebbles();
		System.out.println();
		WhiteBag w = new WhiteBag(WhiteBagType.A);
		w.printPebbles();
		System.out.println();
		System.out.println();
		for(int i = 0;i<22;i++) {
			w.givePebble(b.takePebble());
		}
		b.printPebbles();
		System.out.println();
		w.printPebbles();
		System.out.println();
		System.out.println();
		b.fillPebbles(w.takeAllPebbles());
		b.printPebbles();
		System.out.println();
		w.printPebbles();
		
		System.out.println();
	}

}
