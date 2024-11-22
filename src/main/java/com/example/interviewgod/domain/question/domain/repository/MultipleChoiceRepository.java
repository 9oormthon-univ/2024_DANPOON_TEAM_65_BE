package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleChoiceRepository extends JpaRepository<MultipleChoiceQuestion, Long> {

    @Query("select mq from MultipleChoiceQuestion mq where mq.session.id = :sessionId order by mq.questionNumber")
    List<MultipleChoiceQuestion> findBySessionId(@Param("sessionId") Long sessionId);

    @Query("select mq from MultipleChoiceQuestion mq where mq.session.id in :sessionIdList order by mq.session.id, mq.questionNumber")
    List<MultipleChoiceQuestion> findBySessionIdList(@Param("sessionIdList") List<Integer> sessionIdList);

}
