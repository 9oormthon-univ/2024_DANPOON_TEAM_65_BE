package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
}
