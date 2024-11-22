package com.example.interviewgod.domain.selfIntroduce.dto.response;

import java.util.List;

public record AllSelfIntroduceResponse(List<SelfIntroduceDto> selfIntroduceSet) {

    public record SelfIntroduceDto(String title,
                                   String content,
                                   Long id) {
    }


}
