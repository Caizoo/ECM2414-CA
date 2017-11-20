package test;

import java.io.File;

public class RemoveFiles {
	
	public static void main(String[] args) { // deletes all the test files 
		File x = new File("pebbletest.txt");
		File y = new File("test.txt");
		File z = new File("testfile.txt");
		x.delete();
		y.delete();
		z.delete();
	}

}