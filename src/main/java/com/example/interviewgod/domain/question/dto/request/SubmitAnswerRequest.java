package com.example.interviewgod.domain.question.dto.request;


import java.util.List;

public record SubmitAnswerRequest(Long selfIntroduceId, Integer sessionId, List<Integer> answer) {
}
