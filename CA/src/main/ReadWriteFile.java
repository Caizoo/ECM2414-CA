package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadWriteFile {
	
	public static String[] readFromFile(String fileName) {
		if(!getFileExtension(fileName).matches("txt")) return null;
		ArrayList<String> lines = new ArrayList<String>();
		int i = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = "";
			while((line=buffer.readLine())!=null) {
				lines.add(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String[] strLines = new String[lines.size()];
		for(String line:lines) {
			strLines[i] = line;
			i++;
		}
		return strLines;
	}
	
	public static void writeToFile(String fileName, ArrayList<String> message) throws IOException {
		if(!getFileExtension(fileName).matches("txt")) throw new IOException("Bad file format");
		FileWriter fileWriter = new FileWriter(fileName);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		String line = "";
		Iterator iter = message.iterator();
		while(iter.hasNext()) {
			buffer.write((String)(iter.next()));
			buffer.newLine();
		}
		buffer.flush();
		fileWriter.close();
	}
	
	private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
	public static void main(String[] args) {
		ArrayList<String> str = new ArrayList<String>();
		str.add("Hello");
		str.add("World");
		try {
			writeToFile("hello.txt",str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strArr = readFromFile("hello.txt");
		for(String s:strArr) {
			System.out.println(s);
		}
	}

}
