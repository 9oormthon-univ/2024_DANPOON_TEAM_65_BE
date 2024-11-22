package com.example.interviewgod.domain.question.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;
    @Lob
    private String question;
    @Lob
    private String option1;
    @Lob
    private String option2;
    @Lob
    private String option3;
    @Lob
    private String option4;
    @Lob
    private String option5;
    @Lob
    private String reason;
    private int answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

}
