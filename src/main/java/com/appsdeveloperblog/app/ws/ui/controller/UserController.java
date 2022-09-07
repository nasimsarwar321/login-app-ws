package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Author  Nasim_Sarwar

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {
	
	@GetMapping
	public String getUser() {
		return null;
	}
	
	@PostMapping
	public String createUser() {
		return null;
	}

	@PutMapping
	public String updateUser() {
		return null;
	}
	
	@DeleteMapping
	public String deleteUser() {
		return null;
	}
}
