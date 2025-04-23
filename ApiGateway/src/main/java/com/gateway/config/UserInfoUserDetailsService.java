package com.gateway.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gateway.entity.UserInfo;
import com.gateway.repo.UserInfoRepository;

public class UserInfoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = userInfoRepository.findByName(username);
		return userDetail.map(UserInfoUserDetails::new)
					.orElseThrow(()->new UsernameNotFoundException("User not found with : " + username));
	}

}
