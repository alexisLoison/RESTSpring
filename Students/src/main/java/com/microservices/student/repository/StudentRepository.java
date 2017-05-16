package com.microservices.student.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.microservices.student.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
	
}
