package com.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.entity.UserInfo;
import com.gateway.service.UserService;

@RestController
@RequestMapping("/api")
public class HomeController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/welcome")
	public String index() {
		return "Index method";
	}
	
	@GetMapping("/test1")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String test1() {
		return "Test method 1 security";
	}
	
	@GetMapping("/test2")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String test2() {
		return "Test method 2 security";
	}
	
	@PostMapping("/addUser")
	public String addUser(@RequestBody UserInfo userInfo) {
		return this.userService.addUser(userInfo);
	}
}
