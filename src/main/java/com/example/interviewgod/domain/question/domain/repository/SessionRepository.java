package com.example.interviewgod.domain.question.domain.repository;

import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("select coalesce(max(s.sessionNumber)+1, 1) from Session s where s.selfIntroduce.id = :selfIntroduceId and s.sessionType = :sessionType")
    Integer findNextSessionNumber(@Param("selfIntroduceId") Long selfIntroduceId, @Param("sessionType") SessionType sessionType);

    @Query("select s.id from Session s where s.selfIntroduce.id = :selfIntroduceId and s.sessionType =:sessionType")
    List<Integer> findAllSessionIdBySelfIntroduceId(@Param("selfIntroduceId") Long selfIntroduceId, @Param("sessionType") SessionType sessionType);

}
