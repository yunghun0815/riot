package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.MemberRepository;


@Service
public class UserService implements UserDetailsService {
	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return memberRepository.findByUserId(userId).map(UserDto::new).orElseThrow();
	}
	
}