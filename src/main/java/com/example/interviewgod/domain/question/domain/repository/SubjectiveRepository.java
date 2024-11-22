package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.SubjectiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectiveRepository extends JpaRepository<SubjectiveQuestion, Long> {

    @Query("select sq from SubjectiveQuestion sq where sq.session.id =:sessionId and sq.questionNumber =:questionNumber")
    Optional<SubjectiveQuestion> findByQuestionNumber(@Param("sessionId") Long sessionId, @Param("questionNumber") Integer questionNumber);

}
