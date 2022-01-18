package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignController {
    @Autowired
    SignUpService signUpService;

    @GetMapping("/sign/signup")
    public String signUpPage(){
        return "/sign/signup";
    }

	@PostMapping("/sign")
    public String signUp(MemberDto memberDto){
        signUpService.signup(memberDto);
        System.out.println(memberDto);
        return "/sign/login";
    }
}
