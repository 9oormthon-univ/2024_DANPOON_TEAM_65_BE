package com.example.interviewgod.domain.selfIntroduce.application;

import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import com.example.interviewgod.domain.selfIntroduce.domain.repository.SelfIntroduceRepository;
import com.example.interviewgod.domain.selfIntroduce.dto.request.SaveSelfIntroduceRequest;
import com.example.interviewgod.domain.selfIntroduce.dto.response.AllSelfIntroduceResponse;
import com.example.interviewgod.domain.selfIntroduce.dto.response.SaveSelfIntroduceResponse;
import com.example.interviewgod.domain.user.User;
import com.example.interviewgod.domain.user.UserRepository;
import com.example.interviewgod.global.error.CommonException;
import com.example.interviewgod.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SelfIntroduceService {

    private final SelfIntroduceRepository selfIntroduceRepository;
    private final UserRepository userRepository;


    @Transactional
    public SaveSelfIntroduceResponse saveSelfIntroduce(SaveSelfIntroduceRequest dto) {
        //추후 user에서 가지고 오는것으로 수정
        User user = userRepository.findById(1L).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        SelfIntroduce selfIntroduce = new SelfIntroduce(null, dto.title(), dto.content(), user);
        SelfIntroduce save = selfIntroduceRepository.save(selfIntroduce);
        return new SaveSelfIntroduceResponse(save.getId());
    }

    public AllSelfIntroduceResponse getAllSelfIntroduce() {
        //추후 user에서 가지고 오는것으로 수정
        //User user = userRepository.findById(1L).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        User user = userRepository.findUserByEmail("email").orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        List<AllSelfIntroduceResponse.SelfIntroduceDto> selfIntroduceSet = user.getSelfIntroductions().stream()
                .map(selfIntroduce -> new AllSelfIntroduceResponse.SelfIntroduceDto(
                        selfIntroduce.getTitle(),
                        selfIntroduce.getContent(),
                        selfIntroduce.getId()))
                .collect(Collectors.toList());
        return new AllSelfIntroduceResponse(selfIntroduceSet);
    }


}
