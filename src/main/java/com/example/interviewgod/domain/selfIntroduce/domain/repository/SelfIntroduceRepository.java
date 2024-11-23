package com.example.interviewgod.domain.selfIntroduce.domain.repository;

import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfIntroduceRepository extends JpaRepository<SelfIntroduce, Long> {


}
