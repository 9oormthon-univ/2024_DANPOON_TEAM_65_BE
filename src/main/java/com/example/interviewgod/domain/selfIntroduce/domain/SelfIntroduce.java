package com.example.interviewgod.domain.selfIntroduce.domain;

import com.example.interviewgod.domain.user.User;
import jakarta.persistence.*;

@Entity
public class SelfIntroduce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}