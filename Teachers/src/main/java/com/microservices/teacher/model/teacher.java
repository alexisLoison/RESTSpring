package com.microservices.teacher.model;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Document(collection="teachers")
public class teacher {
	@Id
	private String id; 
	
	private String name;
//	private List<Student> students;
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
/*	public void setStudents(List<Student> students){
		this.students = students;
	}*/
/*	public void setStudentList(){
		//solution found in the following link http://stackoverflow.com/questions/23674046/get-list-of-json-objects-with-spring-resttemplate
		ResponseEntity<List<Student>> students = restTemplate.exchange("http://localhost:8080/student/" + this.name,HttpMethod.GET,null,new ParameterizedTypeReference<List<Student>>(){});
		this.students = students.getBody();
	}*/
/*	public List<Student> getStudents(){
		return students;
	}*/
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
}
