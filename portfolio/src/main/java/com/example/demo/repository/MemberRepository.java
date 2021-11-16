package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
	Optional<Member> findByUserId(String userId);


}
