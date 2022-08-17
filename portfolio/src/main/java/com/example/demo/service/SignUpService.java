package com.example.demo.service;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public void signup(MemberDto memberDto) {

        Member entity = memberDto.signUp();
        entity.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        entity.addRole(Role.USER);


        memberRepository.save(entity);
    }
}
