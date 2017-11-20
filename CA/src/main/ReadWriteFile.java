package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class ReadWriteFile {
	
	public static int getNumPlayers() { // used to input number of players at start game
		System.out.println("Please enter number of players to start game");		
		int x = 0;
		try {
			Scanner sc = new Scanner(System.in);
			String s = sc.nextLine();
			if(s.matches("e")||s.matches("E")) System.exit(0); // exit if input is E or e
			x = Integer.parseInt(s);
			if(x <= 0 || x > PebbleGame.PLAYER_LIMIT) { // make sure number is greater than 1 and less than limit to player numbers
				throw new NumberFormatException();
			}
		} catch (NumberFormatException | InputMismatchException e) {
			System.out.println("Illegal number of players, must be 1 or greater, please try again");
			return -1;
		}
		return x;
	}
	
	public static ArrayList<Pebble> readPebbles(int players) { // used to input filename of pebble text file
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		String fileName = "";
		System.out.println("Please enter filename for pebbles");
		try {
			Scanner sc = new Scanner(System.in);
			fileName = sc.nextLine();
			if(fileName.matches("e")||fileName.matches("E")) System.exit(0);
			pebbles = ReadWriteFile.readPebbles(fileName,players*PebbleGame.PLAYER_PEBBLE_MULTIPLIER*3); // read in enough pebbles to fill each bag
		} catch (InputMismatchException | NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return pebbles;
	}
	
	public static String[] readFromFile(String fileName) throws IOException { // read from file
		if(!getFileExtension(fileName).matches("txt")) throw new IOException("Not a .txt file"); // must exist and be a txt file
		if(!new File(fileName).isFile()) throw new IOException("Not a file");
		ArrayList<String> lines = new ArrayList<String>();
		int i = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = "";
			while((line=buffer.readLine())!=null) {
				lines.add(line);
			}
			fileReader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		String[] strLines = new String[lines.size()];
		for(String line:lines) { // each line in file is an element in the array
			strLines[i] = line;
			i++;
		}
		return strLines;
	}
	
	public static void writeToFile(String fileName, ArrayList<String> message) throws IOException { // write an arraylist of string to a specific file
		if(!getFileExtension(fileName).matches("txt")) throw new IOException("Not a .txt file"); // must be a txt file
		FileWriter fileWriter = new FileWriter(fileName);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		String line = "";
		Iterator iter = message.iterator(); // iterate through list
		while(iter.hasNext()) {
			try {
				buffer.write((String)(iter.next())); // write line and make new line
				buffer.newLine();
			} catch (NullPointerException e) {
				// do nothing, end of file
			}
			
		}
		buffer.flush(); // flush buffer to file
		fileWriter.close();
	}
	
	public static void writeToFile(String fileName, String message) throws IOException { // overloads writing using a single string element which could have newline characters in
		if(!getFileExtension(fileName).matches("txt")) throw new IOException("Bad file format");
		FileWriter fileWriter = new FileWriter(fileName);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		buffer.write(message);
		buffer.flush();
		fileWriter.close();
	}
	
	public static ArrayList<Pebble> readPebbles(String fileName) throws NumberFormatException, IOException { // read and create pebbles from list
		String[] strLines = readFromFile(fileName);
		String[] pebbleWeights = strLines[0].split(","); // split CSV file
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		for(String p:pebbleWeights) {
			Pebble pebble = new Pebble(); // make pebbles (with random weights) and set weights
			pebble.setWeight(Integer.parseInt(p));
			pebbles.add(pebble);
		}
		return pebbles;
	}
	
	public static ArrayList<Pebble> readPebbles(String fileName, int numPebbles) throws NumberFormatException, IOException { // overload with a specified amount of pebbles to return if list is larger than necessary 
		String[] strLines = readFromFile(fileName);
		String[] pebbleWeights = strLines[0].split(",");
		ArrayList<String> weights = new ArrayList<String>(Arrays.asList(pebbleWeights));
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		Random r = new Random();
		for(int i=0;i<numPebbles;i++) {
			if(weights.size()==0) throw new IOException("Not enough pebbles in list"); // throw IO exception if there isn't enough pebbles
			Pebble p = new Pebble();
			p.setWeight(Integer.parseInt(weights.remove((int)(r.nextDouble()*weights.size()))));
			pebbles.add(p);
		}
		return pebbles;
	}
	
	private static String getFileExtension(String fileName) { // returns file extension, e.g. txt as a string
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
	public static void makePebbleFile(String fileName, int numPebbles) throws IOException { // makes a file of pebbles in the specified fileName 
		Random r = new Random();
		String s = new String();
		for(int i=0;i<numPebbles;i++) { // for the specified amount of time
			s += String.valueOf(((int)(r.nextDouble()*30)+1)) + ","; // add a pebble weight between 1 and 30
		}
		s = s.substring(0, s.length()-1);
		writeToFile(fileName,s); // write to file
	}
	
	public static void main(String[] args) throws IOException {
		makePebbleFile("pebbles.txt",3500); // make a pebbles file enough for 100 player (3300 pebbles)
	}

}
