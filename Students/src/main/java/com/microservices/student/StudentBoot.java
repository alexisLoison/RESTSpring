package com.microservices.student;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/*import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;*/
//import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import com.microservices.student.controller.*;
import com.netflix.hystrix.Hystrix;

import org.springframework.web.bind.annotation.RestController;

@EnableCircuitBreaker
@RestController
@EnableEurekaClient
@SpringBootApplication
@EnableRabbit
//@EnableScheduling
public class StudentBoot{
	@Autowired
	private StudentController controller;

	final static String queueName = "springQueue";
	final static String exchangeName = "springExchange";
	
	@Bean
	public RestTemplate rest(RestTemplateBuilder builder){
		return builder.build();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/student/randomTeacher")
	public String toRandom(){
		return controller.getRandomTeacher();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(StudentBoot.class, args);
	}
	
	@Bean
	DirectExchange exchange(){
		return new DirectExchange(exchangeName);
	}
	
}
