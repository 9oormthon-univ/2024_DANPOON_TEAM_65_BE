package com.example.interviewgod.domain.user;

import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Column(nullable = true)
    private Long uid;

    @Enumerated(EnumType.STRING)
    private UserType userType; // 기본값 STANDARD

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfIntroduce> selfIntroductions = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, Long uid, String userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.userType = UserType.valueOf(userType);
    }
}