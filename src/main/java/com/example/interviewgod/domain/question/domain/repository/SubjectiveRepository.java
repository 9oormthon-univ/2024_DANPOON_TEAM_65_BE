package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.SubjectiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectiveRepository extends JpaRepository<SubjectiveQuestion, Long> {
}
