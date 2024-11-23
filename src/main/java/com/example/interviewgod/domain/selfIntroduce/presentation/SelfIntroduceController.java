package com.example.interviewgod.domain.selfIntroduce.presentation;

import com.example.interviewgod.domain.selfIntroduce.application.SelfIntroduceService;
import com.example.interviewgod.domain.selfIntroduce.dto.request.SaveSelfIntroduceRequest;
import com.example.interviewgod.domain.selfIntroduce.dto.response.AllSelfIntroduceResponse;
import com.example.interviewgod.domain.selfIntroduce.dto.response.SaveSelfIntroduceResponse;
import com.example.interviewgod.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/selfIntroduce")
@CrossOrigin("*")
public class SelfIntroduceController {

    private final SelfIntroduceService selfIntroduceService;

    @PostMapping
    public ResponseDto<SaveSelfIntroduceResponse> saveSelfIntroduce(@RequestBody SaveSelfIntroduceRequest selfIntroduceRequest, Authentication authentication) {
        SaveSelfIntroduceResponse saveSelfIntroduceResponse = selfIntroduceService.saveSelfIntroduce(selfIntroduceRequest, authentication);
        return ResponseDto.ok(saveSelfIntroduceResponse);
    }

    @GetMapping()
    public ResponseDto<AllSelfIntroduceResponse> getAllSelfIntroduce(Authentication authentication) {
        AllSelfIntroduceResponse allSelfIntroduce = selfIntroduceService.getAllSelfIntroduce(authentication);
        return ResponseDto.ok(allSelfIntroduce);
    }

}
