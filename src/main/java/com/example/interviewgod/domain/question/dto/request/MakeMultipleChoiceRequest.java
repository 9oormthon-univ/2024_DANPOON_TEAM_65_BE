package com.example.interviewgod.domain.question.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MakeMultipleChoiceRequest(Long selfIntroduceId) {
}
