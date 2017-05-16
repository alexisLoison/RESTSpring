package com.microservices.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.DiscoveryClient;

import com.microservices.student.model.Student;
import com.microservices.student.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;	
	@Autowired
	StudentRepository studentRepository;
	//@Autowired

	//@Autowired

	
	//voir http://ippon.developpez.com/tutoriels/spring/microservices-netflixoss/
	//String teacherUrl = discoveryClient.getNextServerFromEureka("teacher", false).getHomePageUrl();
	
	@RequestMapping(method = RequestMethod.POST)//We fill student dataBase with the datas we post through the method, and save it in our repository
	public Student create(@RequestBody Student student){
		
		Student result = studentRepository.save(student);
		return result;
	}
	@RequestMapping(method = RequestMethod.POST, value="/{studentId}/addMark")//only set the student's mark
	public Student addmark(@PathVariable String studentId, @RequestBody Student student){
		String firstName=studentRepository.findOne(studentId).getFirstName();//we save the current datas from the student DB
		String lastName=studentRepository.findOne(studentId).getLastName();
		int Year=studentRepository.findOne(studentId).getYear();
		student.setFirstName(firstName);//we use the previous saved datas to set a new student
		student.setLastName(lastName);
		student.setId(studentId);
		student.setYear(Year);
		studentRepository.delete(studentId);//We delete the current student
		Student result = studentRepository.save(student);//we add the student with the mark updated
		return result;
	}
	@RequestMapping(method = RequestMethod.GET, value="/teacher")
	public String setTeacher(/*@PathVariable String studentId*/){
		String teacherUrl = discoveryClient.getNextServerFromEureka("TEACHER", false).getHomePageUrl();
		String url = teacherUrl + "/Random";
		//return restTemplate.getForObject(url, String.class);
		return teacherUrl;
		//RestTemplate restTemplate = new RestTemplate();
		//return restTemplate.exchange("http://localhost:27001/teacher/Random", HttpMethod.GET, null, String.class).getBody();
		//studentRepository.findOne(studentId).setTeacher();
		//return studentRepository.findOne(studentId).getTeacher();
	}
	@RequestMapping(method = RequestMethod.GET, value="/{studentId}") //We Use the Id returned by the POST method to look for a student
	public Student get(@PathVariable String studentId){
		return studentRepository.findOne(studentId);
	}
	@RequestMapping(method = RequestMethod.GET, value="/{studentId}/mark")//only return the student's mark
	public int getmark(@PathVariable String studentId){
		return studentRepository.findOne(studentId).getMark();
	}
	@RequestMapping(method = RequestMethod.GET, value="/teacherSearch/{teacherName}")
	public List<Student> getStudentsperTeacher(@PathVariable String teacherName){
		List<Student> allStudents = studentRepository.findAll();
		List<Student> filteredStudents = new ArrayList<>();
		for (Student student: allStudents){
			if(student.getTeacher() == teacherName){
				filteredStudents.add(student);
			}
		}
		return filteredStudents;
	}
	@RequestMapping(method = RequestMethod.GET, value="/")
	public List<Student> getAll(){
		return studentRepository.findAll();
	}
	
}
