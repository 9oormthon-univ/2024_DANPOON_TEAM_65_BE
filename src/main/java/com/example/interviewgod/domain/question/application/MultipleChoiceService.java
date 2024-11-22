package com.example.interviewgod.domain.question.application;

import com.example.interviewgod.domain.question.domain.MultipleChoiceQuestion;
import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import com.example.interviewgod.domain.question.domain.repository.MultipleChoiceRepository;
import com.example.interviewgod.domain.question.domain.repository.SessionRepository;
import com.example.interviewgod.domain.question.dto.gpt.GptMultipleChoiceDto;
import com.example.interviewgod.domain.question.dto.request.MakeMultipleChoiceRequest;
import com.example.interviewgod.domain.question.dto.response.MakeMultipleChoiceResponse;
import com.example.interviewgod.domain.selfIntroduce.domain.SelfIntroduce;
import com.example.interviewgod.domain.selfIntroduce.domain.repository.SelfIntroduceRepository;
import com.example.interviewgod.global.dto.ResponseDto;
import com.example.interviewgod.global.error.CommonException;
import com.example.interviewgod.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MultipleChoiceService {

    private final MultipleChoiceRepository multipleChoiceRepository;
    private final OpenAiService openAiService;
    private final SelfIntroduceRepository selfIntroduceRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public MakeMultipleChoiceResponse makeMultipleChoice(MakeMultipleChoiceRequest request) {
        SelfIntroduce selfIntroduce = selfIntroduceRepository.findById(request.selfIntroduceId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SELF_INTRODUCE));
        List<Integer> sessionNumberList = sessionRepository.findSessionNumberBySelfIntroduceId(request.selfIntroduceId(), SessionType.MULTIPLE_CHOICE);
        int sessionNumber = 1;
        if (sessionNumberList.size() != 0) {
            sessionNumber = sessionNumberList.get(0) + 1;
        }
        Session session = sessionRepository.save(new Session(null, sessionNumber, SessionType.MULTIPLE_CHOICE, selfIntroduce));
        List<GptMultipleChoiceDto> questionList = openAiService.gptMakeMultipleChoiceByContent(selfIntroduce.getContent());
        List<MakeMultipleChoiceResponse.Question> questions = new ArrayList<>();
        System.out.println(questionList.toString());
        for (int i = 0; i < questionList.size(); i++) {
            MultipleChoiceQuestion questionEntity = MultipleChoiceQuestion.builder()
                    .questionNumber(i + 1)
                    .question(questionList.get(i).question())
                    .option1(questionList.get(i).option1())
                    .option2(questionList.get(i).option2())
                    .option3(questionList.get(i).option3())
                    .option4(questionList.get(i).option4())
                    .option5(questionList.get(i).option5())
                    .answer(questionList.get(i).answer())
                    .reason(questionList.get(i).reason())
                    .session(session)
                    .build();
            MultipleChoiceQuestion save = multipleChoiceRepository.save(questionEntity);
            MakeMultipleChoiceResponse.Question question = new MakeMultipleChoiceResponse.Question(questionList.get(i).question(), i + 1, questionList.get(i).option1(), questionList.get(i).option2(), questionList.get(i).option3(), questionList.get(i).option4(), questionList.get(i).option5());
            questions.add(question);
        }
        return new MakeMultipleChoiceResponse(sessionNumber, questions);
    }

}
