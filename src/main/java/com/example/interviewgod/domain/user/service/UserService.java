package com.example.interviewgod.domain.user.service;

import com.example.interviewgod.domain.user.User;
import com.example.interviewgod.domain.user.UserType;
import com.example.interviewgod.domain.user.dto.LoginRequestDTO;
import com.example.interviewgod.domain.user.dto.LoginResponseDTO;
import com.example.interviewgod.domain.user.dto.MemberDTO;
import com.example.interviewgod.domain.user.jwt.JwtTokenProvider;
import com.example.interviewgod.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        //AccessToken 만들어서 줘야함
        Optional<User> byEmail = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (byEmail.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!passwordEncoder.matches(loginRequestDTO.getPassword(), byEmail.get().getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder().
                    accessToken(jwtTokenProvider.createAccessToken(loginRequestDTO.getEmail()))
                    .build();
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> signIn(MemberDTO memberDTO) {
        try {
            // 비밀번호 인코딩
            String encoded = passwordEncoder.encode(memberDTO.getPassword());

            // 회원 저장
            User saved = userRepository.save(User.builder()
                    .name(memberDTO.getName())
                    .email(memberDTO.getEmail())
                    .password(encoded)
                    .userType(UserType.STANDARD)
                    .build());

            // 저장된 객체가 null인 경우 처리
            return new ResponseEntity<>("회원가입 완료", HttpStatus.OK); // 성공 시 200 반환
        } catch (Exception e) {
            // 예외 발생 시 처리
            log.info("회원가입 중 오류: " + e);
            return new ResponseEntity<>("회원가입 실패", HttpStatus.BAD_REQUEST); // 이메일 조회 후 회원가입 할것
        }
    }

    public ResponseEntity<Boolean> isEmailEmpty(String email) {
        return new ResponseEntity<>(userRepository.findByEmail(email).isEmpty(), HttpStatus.OK);
    }

    public Optional<User> checkPermission(Authentication authentication){
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
