package com.example.interviewgod.domain.question.application;

import com.example.interviewgod.domain.question.domain.MultipleChoiceQuestion;
import com.example.interviewgod.domain.question.domain.Session;
import com.example.interviewgod.domain.question.domain.SessionType;
import com.example.interviewgod.domain.question.domain.repository.MultipleChoiceRepository;
import com.example.interviewgod.domain.question.domain.repository.SessionRepository;
import com.example.interviewgod.domain.question.dto.gpt.GptMultipleChoiceDto;
import com.example.interviewgod.domain.question.dto.request.MakeMultipleChoiceRequest;
import com.example.interviewgod.domain.question.dto.request.SubmitAnswerRequest;
import com.example.interviewgod.domain.question.dto.response.AllMultipleChoiceResponse;
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
        Integer nextSessionNumber = sessionRepository.findNextSessionNumber(request.selfIntroduceId(), SessionType.MULTIPLE_CHOICE);
        Session session = sessionRepository.save(new Session(null, nextSessionNumber, SessionType.MULTIPLE_CHOICE, selfIntroduce));
        List<GptMultipleChoiceDto> questionList = openAiService.gptMakeMultipleChoiceByContent(selfIntroduce.getContent());
        List<MakeMultipleChoiceResponse.Question> questions = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            MultipleChoiceQuestion questionEntity = MultipleChoiceQuestion.builder()
                    .questionNumber((nextSessionNumber*5)+ i + 1)
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
            MakeMultipleChoiceResponse.Question question = new MakeMultipleChoiceResponse.Question(questionList.get(i).question(), save.getQuestionNumber(), questionList.get(i).option1(), questionList.get(i).option2(), questionList.get(i).option3(), questionList.get(i).option4(), questionList.get(i).option5());
            questions.add(question);
        }
        return new MakeMultipleChoiceResponse(session.getId(), questions);
    }

    @Transactional
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request) {
        List<MultipleChoiceQuestion> questionList = multipleChoiceRepository.findBySessionId(request.sessionId());
        List<SubmitAnswerResponse.SubmitAnswerResult> results = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            boolean isCorrect = false;
            if (questionList.get(i).getAnswer() == request.answer().get(i)) {
                isCorrect = true;
            }
            questionList.get(i).updateUserResponse(request.answer().get(i));
            SubmitAnswerResponse.SubmitAnswerResult submitAnswerResult = new SubmitAnswerResponse.SubmitAnswerResult(questionList.get(i).getQuestionNumber(), isCorrect, questionList.get(i).getReason());
            results.add(submitAnswerResult);
        }
        return new SubmitAnswerResponse(results);
    }


    public AllMultipleChoiceResponse findAllMultipleChoiceQuestion(Long selfIntroduceId) {
        SelfIntroduce selfIntroduce = selfIntroduceRepository.findById(selfIntroduceId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SELF_INTRODUCE));
        List<Integer> sessionIdList = sessionRepository.findAllSessionIdBySelfIntroduceId(selfIntroduceId, SessionType.MULTIPLE_CHOICE);
        List<MultipleChoiceQuestion> multipleChoiceList = multipleChoiceRepository.findBySessionIdList(sessionIdList);
        List<AllMultipleChoiceResponse.MultipleChoiceDto> questionSet = new ArrayList<>();
        for (MultipleChoiceQuestion multipleChoice : multipleChoiceList) {
            questionSet.add(AllMultipleChoiceResponse.MultipleChoiceDto.builder()
                    .question(multipleChoice.getQuestion())
                    .questionNumber(multipleChoice.getQuestionNumber())
                    .option1(multipleChoice.getOption1())
                    .option2(multipleChoice.getOption2())
                    .option3(multipleChoice.getOption3())
                    .option4(multipleChoice.getOption4())
                    .option5(multipleChoice.getOption5())
                    .answer(multipleChoice.getAnswer())
                    .myResponse(multipleChoice.getUserResponse())
                    .reason(multipleChoice.getReason())
                    .build()
            );
        }
        return new AllMultipleChoiceResponse(selfIntroduce.getTitle(), selfIntroduce.getContent(), questionSet);
    }

}
