package com.example.interviewgod.domain.selfIntroduce.presentation;

import com.example.interviewgod.domain.selfIntroduce.application.SelfIntroduceService;
import com.example.interviewgod.domain.selfIntroduce.dto.request.SaveSelfIntroduceRequest;
import com.example.interviewgod.domain.selfIntroduce.dto.response.SaveSelfIntroduceResponse;
import com.example.interviewgod.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/selfIntroduce")
public class SelfIntroduceController {

    private final SelfIntroduceService selfIntroduceService;

    @PostMapping
    public ResponseDto<SaveSelfIntroduceResponse> saveSelfIntroduce(@RequestBody SaveSelfIntroduceRequest selfIntroduceRequest) {
        SaveSelfIntroduceResponse saveSelfIntroduceResponse = selfIntroduceService.saveSelfIntroduce(selfIntroduceRequest);
        return ResponseDto.ok(saveSelfIntroduceResponse);
    }

}
