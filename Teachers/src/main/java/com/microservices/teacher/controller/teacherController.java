package com.microservices.teacher.controller;
//voir le site https://github.com/spring-cloud-samples/customers-stores
import java.util.List;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.microservices.teacher.model.teacher;
import com.microservices.teacher.configuration.config;
import com.microservices.teacher.repository.teacherRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.DiscoveryClient;
import com.esotericsoftware.minlog.Log;
import com.microservices.teacher.Teacher;
import com.microservices.teacher.model.Student;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/teacher")
public class teacherController {
	//RabbitMQ RPC example
	final static String queueName = "spring.rpc.requests";

	//Kafka integration
	/*@Value(value = "testKafka")
	private String topicName;*/
	
	private DiscoveryClient dc;
	
	private static final Logger log = LoggerFactory.getLogger(teacherController.class);
	
	@Autowired
	private RestTemplate rt;
	
	@Autowired
	teacherRepository teacherRepository;
	
	//KAFKA integration
	/*@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;*/
	
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
	public String getRandom(){
		int taille=teacherRepository.findAll().size();
		int i=(int)(Math.random()*100);
		int numb=i*taille/100;
		return teacherRepository.findAll().get(numb).getName();
	}
	
	//RabbitMQ RPC example
	@RabbitListener(queues = queueName)
	private String HandleMessage(String request){
		System.out.println(" [x] Received  teacher name request " );
		String response = getRandom();
		System.out.println(" [.] response is " + response);
		try{//testing
			Thread.sleep(800);
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}
		return response;
	}
	
	//Kafka Integration
	/*@RequestMapping(method = RequestMethod.GET, value="/sendTeacher")
	public String sendTeacher(){
		String data = getRandom();
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, data);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>(){
			@Override
			public void onSuccess(SendResult<String, String> result){
				String logMessage = "sent message='" + data + "' with offset=" + result.getRecordMetadata().offset();
				log.info("sent message='{}' with offset={}", data, result.getRecordMetadata().offset());
			}
			
			@Override
			public void onFailure(Throwable ex){
				String logMessage = "unable to send message '" + data + "'";
				log.error("unable to send message='{}'", data, ex);
			}
		});
		return data;
	}*/
	
	private String serializeToJson(teacher Teacher){//taking a teacher object to serialize it to json like teacher: { id: <teacherId>, name: <teacherName> }
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		
		final Map<String, teacher> dataMap = new HashMap<>();
		dataMap.put("teacher", Teacher);
		
		try{
			jsonInString = mapper.writeValueAsString(dataMap);
		}catch (JsonProcessingException e){
			log.info(String.valueOf(e));
		}
		log.debug(jsonInString);
		
		return jsonInString;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/clear")
	public void reset(){
		teacherRepository.deleteAll();
	}
}
