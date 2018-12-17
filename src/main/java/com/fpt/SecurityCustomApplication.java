package com.fpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAspectJAutoProxy
@Configuration
public class SecurityCustomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityCustomApplication.class, args);
	}
	@GetMapping("/welcome")
//	@CustomSecurityAnnotation("ABC")
	public ResponseEntity<String> welcome() {
		return ResponseEntity.ok("OK");
	}

}

