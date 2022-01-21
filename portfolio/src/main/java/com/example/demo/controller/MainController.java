package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String index() {
		//메인페이지
		return "/main";
	}
	
	@GetMapping("/sign/login")
	public String login() {
		//로그인페이지로 이동
		return "/sign/login";
	}

	@GetMapping("/board")
	public String board(){
		return "/board/board";
	}
}
