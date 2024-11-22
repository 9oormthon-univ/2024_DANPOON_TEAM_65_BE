package com.example.interviewgod.domain.question.application;

import com.example.interviewgod.domain.question.domain.MultipleChoiceQuestion;
import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import com.example.interviewgod.domain.question.domain.repository.MultipleChoiceRepository;
import com.example.interviewgod.domain.question.domain.repository.SessionRepository;
import com.example.interviewgod.domain.question.dto.gpt.GptMultipleChoiceDto;
import com.example.interviewgod.domain.question.dto.request.MakeMultipleChoiceRequest;
import com.example.interviewgod.domain.question.dto.request.SubmitAnswerRequest;
import com.example.interviewgod.domain.question.dto.response.MakeMultipleChoiceResponse;
import com.example.interviewgod.domain.question.dto.response.SubmitAnswerResponse;
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
public class MultipleChoiceService {

    private final MultipleChoiceRepository multipleChoiceRepository;
    private final OpenAiService openAiService;
    private final SelfIntroduceRepository selfIntroduceRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public MakeMultipleChoiceResponse makeMultipleChoice(MakeMultipleChoiceRequest request) {
        SelfIntroduce selfIntroduce = selfIntroduceRepository.findById(request.selfIntroduceId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SELF_INTRODUCE));
        Integer sessionNumber = sessionRepository.findNextSessionNumber(request.selfIntroduceId(), SessionType.MULTIPLE_CHOICE);
        Session session = sessionRepository.save(new Session(null, sessionNumber, SessionType.MULTIPLE_CHOICE, selfIntroduce));
        List<GptMultipleChoiceDto> questionList = openAiService.gptMakeMultipleChoiceByContent(selfIntroduce.getContent());
        List<MakeMultipleChoiceResponse.Question> questions = new ArrayList<>();
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
        return new MakeMultipleChoiceResponse(session.getId(), questions);
    }

    @Transactional
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request) {
        List<MultipleChoiceQuestion> questionList = multipleChoiceRepository.findMultipleChoiceQuestionBySessionId(request.sessionId());
        List<SubmitAnswerResponse.SubmitAnswerResult> results = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            boolean isCorrect = false;
            if (questionList.get(i).getAnswer() == request.answer().get(i)) {
                isCorrect = true;
            }
            SubmitAnswerResponse.SubmitAnswerResult submitAnswerResult = new SubmitAnswerResponse.SubmitAnswerResult(questionList.get(i).getQuestionNumber(), isCorrect, questionList.get(i).getReason());
            results.add(submitAnswerResult);
        }
        return new SubmitAnswerResponse(results);
    }

}
