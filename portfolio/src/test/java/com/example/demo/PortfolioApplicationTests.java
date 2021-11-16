package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.repository.MemberRepository;

@SpringBootTest
class PortfolioApplicationTests {

	@Autowired
	MemberRepository memeberRepostiory;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//@Test
	void contextLoads() {
		Member member = Member.builder()
				.userId("admin")
				.password(passwordEncoder.encode("admin"))
				.address("도봉구 노해로 60가길 16-12")
				.phone("010-3158-9508")
				.name("정영훈").build();
		
		member.addRole(Role.ADMIN);
		member.addRole(Role.USER);
		member.addRole(Role.GOLD);
		member.addRole(Role.SILVER);
		member.addRole(Role.VIP);
		
		memeberRepostiory.save(member);
	}

}
