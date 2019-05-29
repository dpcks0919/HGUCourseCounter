package edu.handong.analysis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	public static ArrayList<String> getLines(String file, boolean removeHeader, int startYear, int endYear) {
		
		/*
		 * ArrayList<String> name = null; BufferedReader br =null; try { name = new
		 * ArrayList<>(); br = new BufferedReader (new FileReader(file));
		 * if(removeHeader) { br.readLine(); } String line = null; while( (line =
		 * br.readLine()) != null) { name.add(line); } }catch(IOException e ) {
		 * System.out.
		 * println("The file path does not exist. Please check your CLI argument!");
		 * System.exit(0); } return name;
		 */

		ArrayList<String> name = new ArrayList<String>();

		if (removeHeader) {
			
			  
			  try { Reader in = new FileReader(file); CSVParser csvParser =
			  CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			  
			  for (CSVRecord record : csvParser) { if
			  (Integer.parseInt(record.get(7).trim()) >= startYear &&
			  Integer.parseInt(record.get(7).trim()) <= endYear) { String line =
			  record.get(0); for (int i = 1; i < 9; i++) { line += "," + " " +
			  record.get(i); } name.add(line);
			  
			  }
			  
			  } } catch (IOException e) { System.out.
			  println("The file path does not exist. Please check your CLI argument!");
			  System.exit(0); }
			  
			 } else {
			try {
				Reader in = new FileReader(file);
				CSVParser csvParser = CSVFormat.EXCEL.parse(in);
				boolean firstLine = true;
				

				for (CSVRecord record : csvParser) {
					
					if(firstLine == true) {
						String line = record.get(0);
						for (int i = 1; i < 9; i++) {
							line += "," + " " + record.get(i);
						}
						name.add(line);
						firstLine = false;
					}
					
					if (Integer.parseInt(record.get(7).trim()) >= startYear
							&& Integer.parseInt(record.get(7).trim()) <= endYear) {
						String line = record.get(0);
						for (int i = 1; i < 9; i++) {
							line += "," + " " + record.get(i);
						}
						name.add(line);
					}
				}
			} catch (IOException e) {
				System.out.println("The file path does not exist. Please check your CLI argument!");
				System.exit(0);
			}
		}
		return name;
	}

	public static void writeAFile(ArrayList<String> lines, String targetFileName) {

		try {
			File realFile = null;

			if (targetFileName.charAt(0) == 'C') {
				String[] array = targetFileName.split("\\\\");
				String name = array[0];
				for (int i = 1; i < array.length - 1; i++) {
					name += "\\\\" + array[i];
				}

				File file = new File(name);

				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.mkdirs();
				}
				realFile = new File(targetFileName);
			}

			if (targetFileName.charAt(1) == '/') {
				String[] array = targetFileName.split("/");
				String name = "C:\\\\Users\\\\dpcks\\\\git\\\\HGUCourseCounter";
				for (int i = 1; i < array.length - 1; i++) {
					name += "\\\\" + array[i];
				}

				File file = new File(name);

				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.mkdirs();
				}

				realFile = new File(name + "\\\\" + array[array.length - 1]);

			} else {
				realFile = new File(targetFileName);
			}

			FileWriter fw = new FileWriter(realFile);

			for (int i = 0; i < lines.size(); i++) {
				fw.write(lines.get(i));
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}