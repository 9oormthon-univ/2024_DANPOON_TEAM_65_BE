package com.example.interviewgod.domain.user.service;

import com.example.interviewgod.domain.user.User;
import com.example.interviewgod.domain.user.UserType;
import com.example.interviewgod.domain.user.dto.LoginResponseDTO;
import com.example.interviewgod.domain.user.jwt.JwtTokenProvider;
import com.example.interviewgod.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${KAKAO.KEY.CLIENT-ID}")
    private String clientId;

    @Value("${KAKAO.REDIRECT-URI}")
    private String redirectUri;

    //kakao

    public ResponseEntity<LoginResponseDTO> kakaoLogin(String code){
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code, redirectUri);

        System.out.println("accessToken:" + accessToken);
        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponseDTO kakaoUserResponse = kakaoUserLogin(userInfo);

        return new ResponseEntity<>(kakaoUserResponse, HttpStatus.OK);
    }

    public String getAccessToken(String code, String redirectUri){
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    public HashMap<String, Object> getKakaoUserInfo(String accessToken){
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);


        return userInfo;
    }

    public LoginResponseDTO kakaoUserLogin(HashMap<String, Object> userInfo){
        Long uid= Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        User kakaoUser = userRepository.findUserByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {    //회원가입
            kakaoUser = User.builder()
                    .email(kakaoEmail)
                    .name(nickName)
                    .uid(uid)
                    .userType(UserType.KAKAO)
                    .password("-1")
                    .build();
            userRepository.save(kakaoUser);
        }
        //토큰 생성
        return LoginResponseDTO.builder().
                accessToken(jwtTokenProvider.createAccessToken(kakaoEmail))
                .build();
    }
}
