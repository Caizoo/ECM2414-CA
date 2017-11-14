package test;

import java.util.concurrent.locks.ReentrantLock;

import exceptions.BagUnderflowException;
import main.PebbleGame;

public class TestPlayerMain {

	public static void main(String[] args) throws BagUnderflowException {
		PebbleGame g = new PebbleGame(1);
		PebbleGame.Player p = g.new Player(1);
		System.out.println();
	} 
	
}
