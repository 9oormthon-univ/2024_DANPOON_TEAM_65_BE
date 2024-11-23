package com.example.interviewgod.domain.selfIntroduce.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SaveSelfIntroduceRequest(String title, String content) {

}
