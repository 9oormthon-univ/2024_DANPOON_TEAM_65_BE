package com.example.interviewgod.domain.user.repository;

import com.example.interviewgod.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//
//    @Query("select u from User u join fetch u.selfIntroductions s where u.email =:email")
//    Optional<User> findUserByEmail(@Param("email") String email);


    @Query("select u from User u where u.email =:email")
    Optional<User> findUserByEmail(@Param("email") String email);

}
