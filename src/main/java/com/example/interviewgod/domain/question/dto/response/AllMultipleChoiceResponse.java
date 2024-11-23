package com.example.interviewgod.domain.question.dto.response;

import lombok.Builder;

import java.util.List;

public record AllMultipleChoiceResponse(String title,
                                        String content,
                                        List<MultipleChoiceDto> questionSet) {

    @Builder
    public record MultipleChoiceDto(String question,
                                    Integer questionNumber,
                                    String option1,
                                    String option2,
                                    String option3,
                                    String option4,
                                    String option5,
                                    Integer answer,
                                    Integer myResponse,
                                    String reason
                                    ) {
    }

}
