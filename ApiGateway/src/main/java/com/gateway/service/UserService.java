package com.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gateway.entity.UserInfo;
import com.gateway.repo.UserInfoRepository;

@Service
public class UserService {

	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String addUser(UserInfo userInfo) {		
		userInfo.setPassword(this.passwordEncoder.encode(userInfo.getPassword()));
		System.out.println("Under user service");
		this.userInfoRepository.save(userInfo);
		return "User is saved successfully";
	}
}
