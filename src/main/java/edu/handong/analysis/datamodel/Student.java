package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;


public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken ;
	private HashMap<String,Integer> semestersByYearAndSemester;
	
	public Student(String studentId) {
		this.studentId = studentId;
		coursesTaken = new ArrayList<Course>();
		semestersByYearAndSemester = new HashMap<String,Integer>();
	}
	
	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public ArrayList<String> getSemestersByYearAndSemester(){

		ArrayList<Integer> year = new ArrayList<Integer>(); 
		ArrayList<Integer> semester = new ArrayList<Integer>();
		ArrayList<String> yearAndSemester = new ArrayList<String>();
		
		for(int i=0; i < coursesTaken.size(); i++ ) {
			if(! year.contains(coursesTaken.get(i).getYearTaken() ) ) {
				year.add(coursesTaken.get(i).getYearTaken());
			} 
		}
		
		
		for(int j=0, k=0; j < year.size(); j ++) {
			for(int i=0; i < coursesTaken.size(); i++ ) {
				if(year.get(j) == coursesTaken.get(i).getYearTaken()) {
					if( ! semester.contains(coursesTaken.get(i).getSemesterCourseTaken()) ) {
						semester.add(coursesTaken.get(i).getSemesterCourseTaken());
						yearAndSemester.add(year.get(j)+"-"+semester.get(k));
						k++;
					}
				}
			}	
			semester.clear();
			k=0;
		}
		
		for(int i=0; i < yearAndSemester.size(); i++) {
			semestersByYearAndSemester.put(yearAndSemester.get(i), i );
		//	System.out.println(yearAndSemester.get(i)+" "+i);
		}
				
		return yearAndSemester;
	}
	
	public int getNumCourseInNthSementer(int semester) {
		String line = null;
		int count= 0;
		
		for(String key:semestersByYearAndSemester.keySet()) {
		//	System.out.println(key);
			if(semestersByYearAndSemester.get(key) == semester ) {
				line = key;
				//System.out.println(line);
			}
		}
		String[] array = line.split("-");

		for(int i=0; i <coursesTaken.size(); i++ ) {
			if( coursesTaken.get(i).getYearTaken() == Integer.parseInt(array[0]) &&   coursesTaken.get(i).getSemesterCourseTaken()  == Integer.parseInt(array[1]) ) {
				count++;
			}
		}
		
		return count;
	}
	

}