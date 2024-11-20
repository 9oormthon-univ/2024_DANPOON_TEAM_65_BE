package com.example.interviewgod.domain.user;

import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfIntroduce> selfIntroductions = new ArrayList<>();

}