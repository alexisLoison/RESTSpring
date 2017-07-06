package com.microservices.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigServer
@EnableDiscoveryClient
@EnableAutoConfiguration
@SpringBootApplication
public class ConfigServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(ConfigServer.class, args);
	}

}
