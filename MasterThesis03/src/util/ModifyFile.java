package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import metadata.Problem;



public class ModifyFile {

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
	
	
	public static void readTrainPlusTestData(String filePath)
	{
		try{
		File ftrain = new File(filePath+".data");
		File ftest = new File(filePath+".test");
		Scanner scantrain = new Scanner(ftrain);
		Scanner scantest = new Scanner(ftest);
		
		while(scantrain.hasNext())
			data.add(scantrain.nextLine());
		
		while(scantest.hasNext())
			data.add(scantest.nextLine());
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void shuffle()
	{
		Collections.shuffle(data);
	}
	
	public static void writeToFile ()
	{
		try{
			BufferedWriter wr =createFile("Data.data");
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
	
	
	public static void writeToFile (String name)
	{
		try{
			BufferedWriter wr =createFile(name);
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
			//s.indexOf(".|");
			//s = s.substring(0, s.indexOf(".|"));
			file.write(s);
			file.newLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static List<String[]> getSuffledData (String[][] data1, String[][] data2)
	{
		List<String[]> array = new ArrayList<String[]>();
		for(String[] a: data1)
			array.add(a);
		for(String[] b: data2)
			array.add(b);
		Collections.shuffle(array);
		Collections.shuffle(array);
		
		return array;
	}
	
	
	public static List<String[]> getFromArrayToList(String[][] data){
		List<String[]> array = new ArrayList<String[]>();
		for(String[]a :data)
			array.add(a);
		Collections.shuffle(array);
		return array;
	}
	
	
	public static List<List<String[]>> getParts(List<String[]> data, int part)
	{
		List<List<String[]>> resSet = new ArrayList<List<String[]>>();
		int size = data.size();
		int partsize = size/part;
		int start=0;
		int end=partsize;
		int count =0;
		while(start!=end){
			count++;
			resSet.add(data.subList(start, end));
			start+=partsize;
			end+=partsize;
			
			if(end>size)
			{
				partsize = size-start;
				end = size;  
				
			}
		}
		
		return resSet;
	}
	
	
	public static void getTrainPlusTestShuffledData(String filePath, String fileName)
	{
		readTrainPlusTestData(filePath);
		shuffle();
		writeToFile(fileName);
	}
	
	public static void main(String[] args) {
		readFromFile("c:\\akamsi\\source_codes\\Data_Mining_WEKA\\fc45-1.5.0.tar\\fc45-1.5.0\\dataset\\UCI\\allhypo\\allhypo.data");
		shuffle();
		writeToFile();
		System.out.println("DONE");
	}
	
}
