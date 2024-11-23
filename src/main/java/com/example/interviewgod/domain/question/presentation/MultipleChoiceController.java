package com.example.interviewgod.domain.question.presentation;

import com.example.interviewgod.domain.question.application.MultipleChoiceService;
import com.example.interviewgod.domain.question.dto.response.AllMultipleChoiceResponse;
import com.example.interviewgod.domain.question.dto.response.AllSubjectiveQuestionResponse;
import com.example.interviewgod.domain.question.dto.request.MakeMultipleChoiceRequest;
import com.example.interviewgod.domain.question.dto.request.SubmitAnswerRequest;
import com.example.interviewgod.domain.question.dto.response.MakeMultipleChoiceResponse;
import com.example.interviewgod.domain.question.dto.response.SubmitAnswerResponse;
import com.example.interviewgod.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/multipleChoice")
public class MultipleChoiceController {

    private final MultipleChoiceService multipleChoiceService;

    @PostMapping
    public ResponseDto<MakeMultipleChoiceResponse> makeMultipleChoiceQuestion(@RequestBody MakeMultipleChoiceRequest request) {
        MakeMultipleChoiceResponse makeMultipleChoiceResponse = multipleChoiceService.makeMultipleChoice(request);
        return ResponseDto.ok(makeMultipleChoiceResponse);
    }


    @PostMapping("/answer")
    public ResponseDto<SubmitAnswerResponse> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        SubmitAnswerResponse submitAnswerResponse = multipleChoiceService.submitAnswer(request);
        return ResponseDto.ok(submitAnswerResponse);
    }

    @GetMapping("/{selfIntroduceId}")
    public ResponseDto<AllMultipleChoiceResponse> findAllMultipleChoice(@PathVariable("selfIntroduceId") Long selfIntroduceId) {
        AllMultipleChoiceResponse response = multipleChoiceService.findAllMultipleChoiceQuestion(selfIntroduceId);
        return ResponseDto.ok(response);
    }

}
