package com.example.interviewgod.domain.question.presentation;

import com.example.interviewgod.domain.question.application.MultipleChoiceService;
import com.example.interviewgod.domain.question.dto.request.MakeMultipleChoiceRequest;
import com.example.interviewgod.domain.question.dto.response.MakeMultipleChoiceResponse;
import com.example.interviewgod.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
