package com.microservices.teacher.controller;
//voir le site https://github.com/spring-cloud-samples/customers-stores
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.teacher.model.teacher;
//import com.microservices.teacher.model.Student;
import com.microservices.teacher.repository.teacherRepository;

import com.netflix.discovery.DiscoveryClient;

import com.microservices.teacher.model.Student;

@RestController
@RequestMapping("/teacher")
public class teacherController {
	private DiscoveryClient dc;
	
	@Autowired
	private RestTemplate rt;
	
	@Autowired
	teacherRepository teacherRepository;
	
	@RequestMapping(method = RequestMethod.POST)
	public teacher create(@RequestBody teacher teacher){
		teacher result = teacherRepository.save(teacher);
		return result;
	}
	
/*	@RequestMapping(method = RequestMethod.POST, value="/{teacherId}/setStudents")
	public teacher setStudents(@PathVariable String teacherId){
		teacherRepository.findOne(teacherId).setStudentList();//is looking for students linked to the teacher and update the teacher database with those students 
		return teacherRepository.findOne(teacherId);
	}*/
	
	@RequestMapping(method = RequestMethod.GET, value="/{teacherId}")
	public teacher get(@PathVariable String teacherId){
		return teacherRepository.findOne(teacherId);
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value="/{teacherId}/students")
	public Student getStudents(@PathVariable String teacherId){
		return rt.getForObject(url, Student.class);
	}*/
	//or
	
	@RequestMapping(method = RequestMethod.POST, value="/{teacherId}/students")
	public teacher getStudents(@PathVariable String teacherId, @RequestBody teacher teacher){
		if(teacherRepository.findOne(teacherId).getStudents() == null){
			String teacherName=teacherRepository.findOne(teacherId).getName();
			ResponseEntity<List<Student>> students = rt.exchange("http://student/student/teacherSearch/"+ teacherName, HttpMethod.GET,null,new ParameterizedTypeReference<List<Student>>(){});
			List<Student> studentsList = students.getBody();
			String name=teacherRepository.findOne(teacherId).getName();//we save the current datas from the student DB
			teacher.setName(name);
			teacher.setStudents(studentsList);
			teacherRepository.delete(teacherId);//We delete the current student
			teacher result = teacherRepository.save(teacher);//we add the student with the mark updated
			return result;
		}else
			return teacherRepository.findOne(teacherId);
	}
	 

	
	@RequestMapping(method = RequestMethod.GET, value="/")
	public List<teacher> getAll(){
		return teacherRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/Random")
	public String getRandom(){
		int taille=teacherRepository.findAll().size();
		int i=(int)(Math.random()*100);
		int numb=i*taille/100;
		return teacherRepository.findAll().get(numb).getName();
	}
}
