package com.microservices.student.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.client.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.*;

import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.auth.*;
import org.apache.http.HttpHost;

@Document(collection="students")
public class Student {
	@Id
	private String id;
	
	private String lastName;
	private String firstName;
	private int year;
	private int scheduleMark;
	private String teacherName;
	
	public void setId(String id){
		this.id=id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setLastName(String lastName){
		this.lastName=lastName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public void setFirstName(String FirstName){
		this.firstName=FirstName;
	}
	
	public String getFirstName(){
		return firstName;
	}
	public void setYear(int year){
		this.year=year;
	}
	
	public int getYear(){
		return year;
	}
	public void setMark(int mark){
		this.scheduleMark=mark;
	}
	public int getMark(){
		return scheduleMark;
	}
	public void setTeacher(String name){
		this.teacherName=name;
	}
	//public void setTeacher(){
		//final String username = "";//fill with your name
		//final String password = "";//fill with your password
		//final String proxyUrl = "";//fill with the url proxy
		//final int port = 1;//replace with the port
		/*CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(proxyUrl, port),
				new UsernamePasswordCredentials(username, password));
		HttpHost myProxy = new HttpHost(proxyUrl, port);
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();
		HttpClient httpClient = clientBuilder.build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate();*///fill with factory if needed
		//this.teacherName = restTemplate.getForObject("http://localhost:8080/teacher/Random", String.class);
		//this.teacherName = restTemplate.exchange("http://localhost:27001/teacher/Random", HttpMethod.GET, null, String.class).getBody();
//	}
	public String getTeacher(){
		return teacherName;
	}
}
