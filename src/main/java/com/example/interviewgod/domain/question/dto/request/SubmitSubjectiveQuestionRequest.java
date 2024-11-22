package com.example.interviewgod.domain.question.dto.request;

public record SubmitSubjectiveQuestionRequest(Long selfIntroduceId, Long sessionId, Integer questionNumber, String answer) {
}
