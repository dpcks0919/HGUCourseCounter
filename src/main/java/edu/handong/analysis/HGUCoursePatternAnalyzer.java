package edu.handong.analysis;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {
	
	String inputPath; 
	String outputPath; 
	int analysis; 
	String courseCode;
	int startYear;
	int endYear; 
	boolean help; 

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws IOException 
	 */
	public void run(String[] args) {
	/*	
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		*/
		Options options = createOptions();
		
		if(parseOptions(options,args)) {
			if(help) {
				printHelp(options);
				return;
			}
			
			if(analysis == 1) {
				ArrayList<String> lines = Utils.getLines(inputPath, true ,startYear, endYear);
				
				students = loadStudentCourseRecords(lines);
				
				// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
				
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents );
				
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, outputPath);
				
			}
			else if(analysis == 2) {
				if( courseCode == null ) {
					printHelp(options);
					return;
				}
				ArrayList<String> lines = Utils.getLines(inputPath, true ,startYear, endYear);
				
				students = loadStudentCourseRecords(lines);
				
				// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
				
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, outputPath);
			}		
		
		}
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		
		ArrayList<Course> courseList = new ArrayList<Course>() ;
		ArrayList<Student> studentList = new ArrayList<Student>();
		ArrayList<String> resultLines = new ArrayList<String>();
		students = new HashMap<String,Student>();

		for(int i=0; i < lines.size(); i++) {
			courseList.add ( new Course(lines.get(i)) ); 
		}
		
		
		for(int i=0,j=-1; i < lines.size(); i++) {
			if( !resultLines.contains(courseList.get(i).getStudentId()) ) {
				j++;
				resultLines.add(courseList.get(i).getStudentId());
				studentList.add( new Student(courseList.get(i).getStudentId()) );
			} 

			studentList.get(j).addCourse(courseList.get(i));
			
		}
		
		

		for(int i=0; i<resultLines.size(); i++) {
			students.put(resultLines.get(i),studentList.get(i));
		}
		
		return students;
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8if
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents ) {
		
		ArrayList<String> line = new ArrayList<String>();
		line.add("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester\r\n");
		


		for(String key:sortedStudents.keySet()) {
			for(int j=0, l=1; j < sortedStudents.get(key).getSemestersByYearAndSemester().size(); j++ ) {		
						line.add(key+","
								+sortedStudents.get(key).getSemestersByYearAndSemester().size()+","
								+ l++ +","
								+sortedStudents.get(key).getNumCourseInNthSementer(j) +"\r\n");							
			}
		} 
		
		

		return line;
	}
	
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			inputPath = cmd.getOptionValue("i");
			outputPath = cmd.getOptionValue("o");
			analysis = Integer.parseInt(cmd.getOptionValue("a"));
			courseCode = cmd.getOptionValue("c");
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		return true;
	}
	
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("course code")
				//.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());

		
		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Help")
		        .build());
		
		// add options by using OptionBuilder

		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
}
