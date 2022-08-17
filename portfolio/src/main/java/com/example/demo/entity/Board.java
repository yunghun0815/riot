package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

/*@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Data
@Entity*/
public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Column(nullable = false)
    private String title;
    @Column
    private String content;
    @Column(nullable = false)
    private String writer;
    @CreatedDate
    @Column(nullable = false)
    private LocalDate createdDate;
}
