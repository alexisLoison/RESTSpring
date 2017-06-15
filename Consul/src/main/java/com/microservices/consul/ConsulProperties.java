package com.microservices.consul;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("consul")
public class ConsulProperties {

	private String prop = "default value";
	
}
