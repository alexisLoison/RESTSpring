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
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/teacher")
public class teacherController {
	private DiscoveryClient dc;
	
	@Autowired
	private RestTemplate rt;
	
	private final static String QUEUE_NAME = "teacherName_queue";
	
	@Autowired
	teacherRepository teacherRepository;
	
	@RequestMapping(method = RequestMethod.POST)
	public teacher create(@RequestBody teacher teacher){
		teacher result = teacherRepository.save(teacher);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{teacherId}")
	public teacher get(@PathVariable String teacherId){
		return teacherRepository.findOne(teacherId);
	}
	 

	
	@RequestMapping(method = RequestMethod.GET, value="/")
	public List<teacher> getAll(){
		return teacherRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/Random")
	public String getRandom(){// throws IOException, TimeoutException
		/*ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();*/
		
		int taille=teacherRepository.findAll().size();
		int i=(int)(Math.random()*100);
		int numb=i*taille/100;
		
		/*String message = teacherRepository.findAll().get(numb).getName();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();*/
		
		return teacherRepository.findAll().get(numb).getName();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/clear")
	public void reset(){
		teacherRepository.deleteAll();
	}
}
