package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.Pebble;
import main.ReadWriteFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ReadWriteFileTest {

	static String fileName;
	static File file;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception { // make a file full of pebbles 
		fileName = "test.txt";
		file = new File(fileName);
		ReadWriteFile.makePebbleFile(fileName,1000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		file = null;
		fileName = null;
	}

	@Test
	public void testReadFromFile()  {
		String[] r = new String[0]; // empty array
		try {
			r = ReadWriteFile.readFromFile(fileName); // array of string represent lines from file
		} catch (IOException e) {
			fail(e.getMessage()); // fail if IO exception
		}
		assertTrue(r.length>0); // assert elements have been read
		assertEquals(r[0].getClass().getName(),String.class.getName()); // assert the first element is a string
	}

	@Test
	public void testWriteToFileStringArrayListOfString() {
		ArrayList<String> s = new ArrayList<String>(); // arraylist of string to write
		s.add("Hello");
		s.add("World!");
		try {
			ReadWriteFile.writeToFile("testfile.txt", s); // write file
			String[] str = ReadWriteFile.readFromFile("testfile.txt"); // read file back in 
			assertEquals(str.length,2); // assert two lines
			assertEquals(str[0],"Hello"); // assert lines are the same as written
			assertEquals(str[1],"World!");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testWriteToFileStringString() {
		String s = "Hello\nWorld!"; // string to write
		try {
			ReadWriteFile.writeToFile("testfile.txt", s); // write file
			String[] str = ReadWriteFile.readFromFile("testfile.txt"); // read file back in 
			assertEquals(str.length,2); // assert two lines
			assertEquals(str[0],"Hello"); // assert lines are the same as written
			assertEquals(str[1],"World!");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReadPebblesString() {
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		try {
			pebbles = ReadWriteFile.readPebbles(fileName); // read in full list of pebbles
		} catch (NumberFormatException | IOException e) {
			fail(e.getMessage());
		}
		assertEquals(1000,pebbles.size()); // should be 1000 pebbles
		assertEquals(Pebble.class.getName(),pebbles.get(0).getClass().getName()); // assert they are indeed pebbles
	}

	@Test
	public void testReadPebblesStringInt() {
		ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
		try {
			pebbles = ReadWriteFile.readPebbles(fileName,100); // only read in 100 random pebbles
		} catch (NumberFormatException | IOException e) {
			fail(e.getMessage());
		}
		assertEquals(100,pebbles.size()); // make sure only 100 have been read
		assertEquals(Pebble.class.getName(),pebbles.get(0).getClass().getName());
	}

	@Test
	public void testMakePebbleFile() {
		try {
			ReadWriteFile.makePebbleFile("pebbletest.txt", 100); // make a test file of 100 pebbles
			ArrayList<Pebble> pebbles = null; 
			pebbles = ReadWriteFile.readPebbles("pebbletest.txt"); // read in pebbles
			assertEquals(pebbles.size(),100);
			assertEquals(Pebble.class.getName(),pebbles.get(0).getClass().getName());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}

}
