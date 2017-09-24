package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class MixLines {

	static List<String> data = new ArrayList<String>();
	
	public static void readFromFile(String filePath)
	{
		try{
		File f = new File(filePath);
		Scanner scan = new Scanner(f);
		
		while(scan.hasNext())
			data.add(scan.nextLine());
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void suffle()
	{
		Collections.shuffle(data);
	}
	
	public static void writeToFile ()
	{
		try{
			BufferedWriter wr =createFile("NewData.data");
			Iterator<String> iter = data.iterator();
			while(iter.hasNext())
			{
				writeToFile(iter.next(),wr);
				//wr.newLine();
			}
			wr.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	private static BufferedWriter createFile(String name)
	{
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(name);
			}
		catch (IOException e)
		{	
			e.printStackTrace();
		}
		
	      BufferedWriter out = new BufferedWriter(fstream);
		return out;
	}
	
	private static void writeToFile(String s, BufferedWriter file)
	{
		try
		{
			file.write(s);
			file.newLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		readFromFile("c:\\akamsi\\source_codes\\Data_Mining_WEKA\\fc45-1.5.0.tar\\fc45-1.5.0\\dataset\\UCI\\covtype-new\\covtype.data");
		suffle();
		writeToFile();
		System.out.println("DONE");
	}
	
}
