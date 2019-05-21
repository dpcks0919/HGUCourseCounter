package edu.handong.analysis;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws IOException 
	 */
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
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
		} //lines의 모든 라인을 CourseList에 Course 객체화 시켜서 넣는다.
		
		
		
		for(int i=0,j=-1; i < lines.size(); i++) {
			if( !resultLines.contains(courseList.get(i).getStudentId()) ) {
				j++;
				resultLines.add(courseList.get(i).getStudentId());
				studentList.add( new Student(courseList.get(i).getStudentId()) );
				//System.out.println(j); 
			} 
			//System.out.println(studentList.get(j).getStudentId());

			//System.out.println(studentList.get(j)+" "+courseList.get(i).getCourseName() );
			studentList.get(j).addCourse(courseList.get(i));
			
		} // student 학번만 중복안되게 resultLines에 넣어둠
		
		

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
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		ArrayList<String> line = new ArrayList<String>();
		line.add("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester\r\n");
		
		//String s = "0001";
		//System.out.println(sortedStudents.get(s) );
		for(String key:sortedStudents.keySet()) {
			for(int j=0, l=1; j < sortedStudents.get(key).getSemestersByYearAndSemester().size(); j++ ,l++ ) {
				//line.add(key+","+sortedStudents.get(key).getSemestersByYearAndSemester().size()+","+","+ sortedStudents.get(key).getNumCourseInNthSementer(j+1) );
				line.add(key+","+sortedStudents.get(key).getSemestersByYearAndSemester().size()+","+
						l+","+sortedStudents.get(key).getNumCourseInNthSementer(j) +"\r\n");
				
			}
		} 
		
		//edu.handong.analysis.datamodel.Student@77468bd9 학생 1번
		return line;
	}  
}
