package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("select s.sessionNumber from Session s where s.selfIntroduce.id = :selfIntroduceId and s.sessionType = :sessionType order by s.sessionNumber desc")
    List<Integer> findSessionNumberBySelfIntroduceId(@Param("selfIntroduceId") Long selfIntroduceId, @Param("sessionType") SessionType sessionType);

}
