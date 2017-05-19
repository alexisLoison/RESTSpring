package com.microservices.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;

import com.microservices.student.model.Student;
import com.microservices.student.repository.StudentRepository;
import com.microservices.student.configuration.config;

@RestController
@RequestMapping("/student")
@FeignClient("teacher")
public class StudentController {
	@Autowired
	private RestTemplate restTemplate;
	
	DiscoveryClient discoveryClient;	
	
	@Autowired
	StudentRepository studentRepository;


	
	
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
		String teacher=studentRepository.findOne(studentId).getTeacher();
		student.setFirstName(firstName);//we use the previous saved datas to set a new student
		student.setLastName(lastName);
		student.setId(studentId);
		student.setYear(Year);
		student.setTeacher(teacher);
		studentRepository.delete(studentId);//We delete the current student
		Student result = studentRepository.save(student);//we add the student with the mark updated
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/teacherUrl")
	public String getTeacherUrl(){
		discoveryClient = DiscoveryManager.getInstance().getDiscoveryClient();
		String teacherUrl = discoveryClient.getNextServerFromEureka("teacher", false).getHomePageUrl();
		
		String url = teacherUrl + "teacher/Random";
		return url;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getRandomTeacher")
	public String getRandomTeacher(){
		String teacherName = restTemplate.exchange("http://teacher/teacher/Random", HttpMethod.GET,null,String.class).getBody();
		return teacherName;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{studentId}/teacher")
	public Student setTeacher(@PathVariable String studentId, @RequestBody Student student){
		if (studentRepository.findOne(studentId).getTeacher() == null){
			String teacherName = getRandomTeacher();
			String firstName=studentRepository.findOne(studentId).getFirstName();//we save the current datas from the student DB
			String lastName=studentRepository.findOne(studentId).getLastName();
			int Year=studentRepository.findOne(studentId).getYear();
			int mark=studentRepository.findOne(studentId).getMark();
			student.setFirstName(firstName);//we use the previous saved datas to set a new student
			student.setLastName(lastName);
			student.setId(studentId);
			student.setYear(Year);
			student.setMark(mark);
			student.setTeacher(teacherName);
			studentRepository.delete(studentId);//We delete the current student
			Student result = studentRepository.save(student);//we add the student with the mark updated
			return result;
		}else
			return studentRepository.findOne(studentId);
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
		List<Student> allStudents = getAll();
		List<Student> filteredStudents = new ArrayList<>();
		int i;
		for(i=0;i<allStudents.size();i++){
			if(allStudents.get(i).getTeacher().equals(teacherName))
				filteredStudents.add(allStudents.get(i));
		}
		return filteredStudents;
	}
	@RequestMapping(method = RequestMethod.GET, value="/")
	public List<Student> getAll(){
		return studentRepository.findAll();
	}
	
}
