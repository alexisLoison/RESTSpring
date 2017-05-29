package com.microservices.student;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.microservices.student.controller.*;
import org.springframework.web.bind.annotation.RestController;

@EnableCircuitBreaker
@RestController
@EnableEurekaClient
@SpringBootApplication
public class StudentBoot {
	@Autowired
	private StudentController controller;
	
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
	
	
}
