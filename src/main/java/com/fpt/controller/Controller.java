package com.fpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.annotation.CustomSecurityAnnotation;
import com.fpt.model.Message;
import com.fpt.model.User;
import com.fpt.service.JwtService;

@RestController
public class Controller {
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user){
		if(user.getUsername().equals("admin") && user.getPassword().equals("admin"))
		{
			String token = jwtService.generateTokenLogin("admin", "ADMIN");
			return ResponseEntity.ok(new Message("Login success", token));
		}
		
		if(user.getUsername().equals("user") && user.getPassword().equals("user"))
		{
			String token = jwtService.generateTokenLogin("user", "USER");
			return ResponseEntity.ok(new Message("Login success", token));
		}

		return ResponseEntity.ok(new Message("Login fail", "Wrong username or password!"));
	}
	@GetMapping("/welcome")
	@CustomSecurityAnnotation("USER")//allow role user access
	public ResponseEntity<?> welcome() {
		return ResponseEntity.ok("Access!");
	}
}
