package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Member {
	@Id
	@Column(unique = true, nullable = false)
	private String userId;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String phone;
	
	@Column
	private String address;
	
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedDate;
	
	@Column
	private String cupon;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	public void addRole(Role role) {
		roles.add(role);
	}
}
