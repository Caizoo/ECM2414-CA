package test;

import java.util.concurrent.locks.ReentrantLock;

import main.PebbleGame;

public class TestPlayer {

	public static void main(String[] args) {
		PebbleGame g = new PebbleGame(1);
		PebbleGame.Player p = g.new Player();
		System.out.println();
	}
	
}
