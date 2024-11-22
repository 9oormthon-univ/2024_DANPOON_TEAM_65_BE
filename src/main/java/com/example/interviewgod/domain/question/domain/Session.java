package com.example.interviewgod.domain.question.domain;

import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sessionNumber;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELF_INTRODUCE_ID")
    private SelfIntroduce selfIntroduce;

}