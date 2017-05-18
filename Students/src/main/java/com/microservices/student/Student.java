package com.microservices.student;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;

//@EnableFeignClients
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class Student {
	/*@Autowired
	RandomTeacher client;
	
	@RequestMapping("/getTeacher")
	public String Random(){
		return client.getRandom();
	}*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Student.class, args);
	}
	
	/*@FeignClient("teacher")
	interface RandomTeacher {
		@RequestMapping(value = "/student/getTeacher", method = GET)
		String getRandom();
	}*/
}
/*
@RestController
class ServiceInstanceRestController {
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName){
		return this.discoveryClient.getInstances(applicationName);
	}
}*/