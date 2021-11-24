package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.RiotService;
import com.example.demo.service.impl.RiotServiceImpl;

@Controller
public class RiotController {
	@Autowired
	RiotService riotService;
	
	@GetMapping("/search")//@RequestParam(value="title")String title,
	public String search(@RequestParam(value = "name")String name, Model model) {
		riotService.search(name, model);
		return "/riot/search";
	}
}
