package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;

	public Student(String studentId) {
		this.studentId = studentId;
		coursesTaken = new ArrayList<Course>();
		semestersByYearAndSemester = new HashMap<String, Integer>();
	}

	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}

	public String getStudentId() {
		return studentId;
	}

	public ArrayList<Course> getCoursesTaken() {
		return coursesTaken;
	}

	public ArrayList<String> getSemestersByYearAndSemester() {

		ArrayList<Integer> year = new ArrayList<Integer>();
		ArrayList<Integer> semester = new ArrayList<Integer>();
		ArrayList<String> yearAndSemester = new ArrayList<String>();

		for (int i = 0; i < coursesTaken.size(); i++) {
			if (!year.contains(coursesTaken.get(i).getYearTaken())) {
				year.add(coursesTaken.get(i).getYearTaken());
			}
		}

		for (int j = 0, k = 0; j < year.size(); j++) {
			for (int i = 0; i < coursesTaken.size(); i++) {
				if (year.get(j) == coursesTaken.get(i).getYearTaken()) {
					if (!semester.contains(coursesTaken.get(i).getSemesterCourseTaken())) {
						semester.add(coursesTaken.get(i).getSemesterCourseTaken());
						yearAndSemester.add(year.get(j) + "-" + semester.get(k));
						k++;
					}
				}
			}
			semester.clear();
			k = 0;
		}

		for (int i = 0; i < yearAndSemester.size(); i++) {
			semestersByYearAndSemester.put(yearAndSemester.get(i), i);
		}

		return yearAndSemester;
	}

	public int getNumCourseInNthSementer(int semester) {
		String line = null;
		int count = 0;

		for (String key : semestersByYearAndSemester.keySet()) {
			if (semestersByYearAndSemester.get(key) == semester) {
				line = key;
			}
		}
		String[] array = line.split("-");

		for (int i = 0; i < coursesTaken.size(); i++) {
			if (coursesTaken.get(i).getYearTaken() == Integer.parseInt(array[0])
					&& coursesTaken.get(i).getSemesterCourseTaken() == Integer.parseInt(array[1])) {
				count++;
			}
		}

		return count;
	}

	public static ArrayList<String> countPerCourseNameAndYear(ArrayList<String> lines, String courseCode, int startYear,
			int endYear) {

		ArrayList<String> line = new ArrayList<String>();
		ArrayList<Course> courseList = new ArrayList<Course>();

		String courseName = null;

		for (int i = 0; i < lines.size(); i++) {
			courseList.add(new Course(lines.get(i)));
		}

		for (int k = 0; k < courseList.size(); k++) {
			if (courseList.get(k).getcourseCode().equals(courseCode)) {
				courseName = courseList.get(k).getCourseName();
				break;
			}
		}

		line.add("Year,Semester,CourseCode, CourseName,TotalStudents,StudentsTaken,Rate" + "\r\n");

		for (; startYear <= endYear; startYear++) {

			for (int i = 1; i < 5; i++) {
				ArrayList<String> studentname = new ArrayList<String>();
				int countStudent = 0;
				for (int k = 0; k < courseList.size(); k++) {
					if (courseList.get(k).getYearTaken() == startYear
							&& courseList.get(k).getSemesterCourseTaken() == i) {

						if (!studentname.contains(courseList.get(k).getStudentId())) {
							studentname.add(courseList.get(k).getStudentId());
						}

						if (courseList.get(k).getcourseCode().equals(courseCode)) {
							courseName = courseList.get(k).getCourseName();
							countStudent++;
						}
					}

				}
				float dividefloat = (float) countStudent / studentname.size() * 100;
				String divide = String.format("%.1f", dividefloat);
				line.add(startYear + "," + i + "," + courseCode + "," + courseName + "," + studentname.size() + ","
						+ countStudent + "," + divide+"%" + "\r\n");
			}

		}
		return line;
	}

}
