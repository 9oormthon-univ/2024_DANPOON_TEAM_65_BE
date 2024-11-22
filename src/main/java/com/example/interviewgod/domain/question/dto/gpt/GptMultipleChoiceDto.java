package com.example.interviewgod.domain.question.dto.gpt;

public record GptMultipleChoiceDto(String question,
                                   int answer,
                                   String option1,
                                   String option2,
                                   String option3,
                                   String option4,
                                   String option5) {

}
