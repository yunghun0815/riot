package com.example.demo.dto;

import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Data
public class MemberDto {

    private String userId;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Set<Role> roles = new HashSet<Role>();

    public Member signUp() {
      return Member.builder()
                .userId(userId)
                .password(password)
                .address(address)
                .phone(phone)
                .name(name).build();
    }
}
