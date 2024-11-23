package com.example.interviewgod.domain.question.presentation;

import com.example.interviewgod.domain.question.application.SubjectiveQuestionService;
import com.example.interviewgod.domain.question.dto.response.AllSubjectiveQuestionResponse;
import com.example.interviewgod.domain.question.dto.request.MakeSubjectiveQuestionRequest;
import com.example.interviewgod.domain.question.dto.request.SubmitSubjectiveQuestionRequest;
import com.example.interviewgod.domain.question.dto.response.MakeSubjectiveQuestionResponse;
import com.example.interviewgod.domain.question.dto.response.SubmitSubjectiveQuestionResponse;
import com.example.interviewgod.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/subjective")
@RestController
@CrossOrigin("*")
public class SubjectiveQuestionController {

    private final SubjectiveQuestionService subjectiveQuestionService;


    @PostMapping
    public ResponseDto<MakeSubjectiveQuestionResponse> makeSubjectiveQuestion(@RequestBody MakeSubjectiveQuestionRequest request) {
        MakeSubjectiveQuestionResponse makeSubjectiveQuestionResponse = subjectiveQuestionService.makeSubjectiveQuestion(request);
        return ResponseDto.ok(makeSubjectiveQuestionResponse);
    }

    @PostMapping("/answer")
    public ResponseDto<SubmitSubjectiveQuestionResponse> submitSubjectiveQuestion(@RequestBody SubmitSubjectiveQuestionRequest request) {
        SubmitSubjectiveQuestionResponse response = subjectiveQuestionService.submitSubjectiveQuestion(request);
        return ResponseDto.ok(response);
    }

    @GetMapping("/{selfIntroduceId}")
    public ResponseDto<AllSubjectiveQuestionResponse> findAllSubjectiveQuestion(@PathVariable("selfIntroduceId") Long selfIntroduceId) {
        AllSubjectiveQuestionResponse response = subjectiveQuestionService.findAllSubjectiveQuestion(selfIntroduceId);
        return ResponseDto.ok(response);
    }
}
