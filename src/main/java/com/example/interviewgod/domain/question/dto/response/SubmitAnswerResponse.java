package com.example.interviewgod.domain.question.dto.response;

import com.example.interviewgod.domain.question.dto.request.SubmitAnswerRequest;

import java.util.List;

public record SubmitAnswerResponse(List<SubmitAnswerRequest> result) {

    public record SubmitAnswerResult(Integer questionNumber, boolean isCorrect, String reason) {
    }


}
