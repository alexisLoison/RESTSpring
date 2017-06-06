package com.microservices.teacher.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class config {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	/*
	@Bean
	public AmqpInvokerServiceExporter exporter(String teacherName, AmqpTemplate template){
		AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
		exporter.setServiceInterface(String.class);
		exporter.setService(teacherName);
		exporter.setAmqpTemplate(template);
		return exporter;
	}
	
	@Bean
	public SimpleMessageListenerContainer listener(ConnectionFactory factory, AmqpInvokerServiceExporter exporter, Queue queue){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
		container.setMessageListener(exporter);
		container.setQueueNames(queue.getName());
		return container;
	}*/
	/*
	@Bean
	public DirectExchange exchange(){
		return new DirectExchange("student.rpc");
	}

	@Bean
	public Binding binding(DirectExchange exchange, Queue queue){
		return BindingBuilder.bind(queue).to(exchange).with("rpc");
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
		RabbitTemplate template = new RabbitTemplate(factory);
		template.setRoutingKey("rpc");
		template.setExchange("student.rpc");
		return template;
	}*/
	
	/*@Bean
	public Queue queue(){
		return new Queue("student.rpc.requests");
	}*/
	
}
