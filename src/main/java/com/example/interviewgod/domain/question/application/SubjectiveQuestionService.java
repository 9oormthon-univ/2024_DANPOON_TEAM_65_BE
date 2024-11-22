package com.example.interviewgod.domain.question.application;

import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import com.example.interviewgod.domain.question.domain.SubjectiveQuestion;
import com.example.interviewgod.domain.question.domain.repository.SessionRepository;
import com.example.interviewgod.domain.question.domain.repository.SubjectiveRepository;
import com.example.interviewgod.domain.question.dto.gpt.GptSubjectiveDto;
import com.example.interviewgod.domain.question.dto.request.MakeSubjectiveQuestionRequest;
import com.example.interviewgod.domain.question.dto.request.SubmitSubjectiveQuestionRequest;
import com.example.interviewgod.domain.question.dto.response.MakeSubjectiveQuestionResponse;
import com.example.interviewgod.domain.question.dto.response.SubmitSubjectiveQuestionResponse;
import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import com.example.interviewgod.domain.selfIntroduce.domain.repository.SelfIntroduceRepository;
import com.example.interviewgod.global.error.CommonException;
import com.example.interviewgod.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubjectiveQuestionService {

    private final SessionRepository sessionRepository;
    private final SubjectiveRepository subjectiveRepository;
    private final OpenAiService openAiService;
    private final SelfIntroduceRepository selfIntroduceRepository;

    public MakeSubjectiveQuestionResponse makeSubjectiveQuestion(MakeSubjectiveQuestionRequest request) {
        SelfIntroduce selfIntroduce = selfIntroduceRepository.findById(request.selfIntroduceId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SELF_INTRODUCE));
        Integer nextSessionNumber = sessionRepository.findNextSessionNumber(request.selfIntroduceId(), SessionType.SUBJECTIVE);
        List<MakeSubjectiveQuestionResponse.MadeSubjectiveQuestion> questionSet = new ArrayList<>();
        List<GptSubjectiveDto> gptSubjectiveDtoList = openAiService.gptMakeSubjectiveQuestion(selfIntroduce.getContent());
        Session session = sessionRepository.save(new Session(null, nextSessionNumber, SessionType.SUBJECTIVE, selfIntroduce));
        for (int i = 0; i < gptSubjectiveDtoList.size(); i++) {
            SubjectiveQuestion subjectiveQuestion = SubjectiveQuestion.builder()
                    .questionNumber(i + 1)
                    .question(gptSubjectiveDtoList.get(i).question())
                    .session(session)
                    .build();
            SubjectiveQuestion save = subjectiveRepository.save(subjectiveQuestion);
            questionSet.add(new MakeSubjectiveQuestionResponse.MadeSubjectiveQuestion(gptSubjectiveDtoList.get(i).question(), i + 1));
        }
        return new MakeSubjectiveQuestionResponse(session.getId(), questionSet);
    }

    @Transactional
    public SubmitSubjectiveQuestionResponse submitSubjectiveQuestion(SubmitSubjectiveQuestionRequest request) {
        SubjectiveQuestion subjectiveQuestion = subjectiveRepository.findByQuestionNumber(request.sessionId(), request.questionNumber()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QUESTION));
        subjectiveQuestion.updateUserResponse(request.answer());
        return new SubmitSubjectiveQuestionResponse(true);
    }
}
