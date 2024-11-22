package com.example.interviewgod.domain.question.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MakeMultipleChoiceResponse(Integer sessionId, List<Question> questionSet) {

    public record Question(String question,
                              Integer question_number,
                              String option1,
                              String option2,
                              String option3,
                              String option4,
                              String option5) {
    }

}
