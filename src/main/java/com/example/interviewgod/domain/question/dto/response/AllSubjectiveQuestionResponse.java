package com.example.interviewgod.domain.question.dto.response;

import com.example.interviewgod.domain.question.domain.SubjectiveQuestion;

import java.util.List;

public record AllSubjectiveQuestionResponse(String title,
                                            String content,
                                            List<SubjectiveQuestionDto> questionSet
                                            ) {

    public record SubjectiveQuestionDto(String question, Integer questionNumber, String myResponse) {


    }

}
