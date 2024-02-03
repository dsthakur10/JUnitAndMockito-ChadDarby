package com.devendra.component;

import com.devendra.component.models.CollegeStudent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class MvcTestingExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcTestingExampleApplication.class, args);
	}

	// The @Bean annotation is used to indicate that a method instantiates, configures, and initializes a new object
	// to be managed by the Spring IoC

	// A bean with the prototype scope will return a different instance every time it is requested from the container.

	@Bean(name = "collegeStudent")
	@Scope(value = "prototype")
	CollegeStudent getCollegeStudent() {
		return new CollegeStudent();
	}

}
