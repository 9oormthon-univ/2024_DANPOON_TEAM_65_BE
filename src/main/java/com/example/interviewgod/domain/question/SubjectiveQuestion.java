package com.example.interviewgod.domain.question;

import jakarta.persistence.*;

@Entity
public class SubjectiveQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;

    @Lob
    private String question;

    @Lob
    private String userResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

}