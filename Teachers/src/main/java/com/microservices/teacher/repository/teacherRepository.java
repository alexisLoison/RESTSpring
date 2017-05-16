package com.microservices.teacher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.microservices.teacher.model.teacher;

public interface teacherRepository extends MongoRepository<teacher, String> {

}
