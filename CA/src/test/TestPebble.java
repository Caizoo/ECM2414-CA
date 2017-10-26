package test;

import main.Pebble;

public class TestPebble {
	
	public static void main(String[] args) {
		for(int i = 0;i<100000;i++) {
			Pebble p = new Pebble();
			System.out.println(p.getWeight());
			if(p.getWeight()>30 || p.getWeight()<1) {
				throw new NumberFormatException();
			}
		}
		
	}

}
