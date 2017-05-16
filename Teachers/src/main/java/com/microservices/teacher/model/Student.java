package com.microservices.teacher.model;

public class Student {
	private String Id;
	private String lastName;
	private String firstName;
	private int year;
	private int scheduleMark;
	private String teacherName;
	
	public void setId(String id){
		this.Id = id;
	}
	
	public String getId(){
		return Id;
	}
	
	public void setlName(String lName){
		this.lastName = lName;
	}
	
	public String getlName(){
		return lastName;
	}
	
	public void setfName(String fName){
		this.firstName = fName;
	}
	
	public String getfName(){
		return firstName;
	}
	
	public void setYear(int y){
		this.year = y;
	}
	
	public int getYear(){
		return year;
	}
	
	public void setMark(int mark){
		this.scheduleMark = mark;
	}
	
	public int getMark(){
		return scheduleMark;
	}
	
	public void setTeacher(String tName){
		this.teacherName = tName;
	}
	
	public String getTeacher(){
		return teacherName;
	}
}
