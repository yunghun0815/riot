package com.example.demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	
	USER("USER","회원"),
	SILVER("VIP","실버"),
	GOLD("GOLD","골드"),
	VIP("VIP","VIP"),
	ADMIN("ADMIN","관리자");
	
	private final String role;
	private final String title;
}
