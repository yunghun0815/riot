package com.example.demo.dto;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.demo.entity.Member;

import lombok.Getter;

@Getter
public class UserDto extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String userId;
	
	public UserDto(Member user) {
		super(user.getUserId(), user.getPassword(),
			user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet()));
		
		userId=user.getUserId();
		name=user.getName();
		
	}
}
