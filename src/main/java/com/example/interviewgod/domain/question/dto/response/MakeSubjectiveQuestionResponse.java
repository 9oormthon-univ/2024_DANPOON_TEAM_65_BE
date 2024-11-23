package com.example.interviewgod.domain.question.dto.response;


import java.util.List;

public record MakeSubjectiveQuestionResponse(Long sessionId, List<MadeSubjectiveQuestion> questionSet) {


    public record MadeSubjectiveQuestion(String question, Integer questionNumber) {
    }

}
