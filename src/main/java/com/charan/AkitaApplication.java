package com.charan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@RestController
@SpringBootApplication
public class AkitaApplication {

//	@RequestMapping("/home")
//	public String index() {
//		return "Greetings from Spring Boot!";
//	}
	public static void main(String[] args) {
		SpringApplication.run(AkitaApplication.class, args);
	}
}
