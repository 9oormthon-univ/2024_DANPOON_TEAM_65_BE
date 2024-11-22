package com.example.interviewgod.domain.question.application;

import com.example.interviewgod.domain.question.dto.gpt.GptMultipleChoiceDto;
import com.example.interviewgod.domain.question.dto.gpt.GptSubjectiveDto;
import com.example.interviewgod.global.error.CommonException;
import com.example.interviewgod.global.error.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final WebClient webClient;

    @Value("${openai.api_key}")
    private String apiKey;

    @Value("${openai.multiple_choice_prompt}")
    private String multipleChoicePrompt;

    @Value("${openai.subjective_prompt}")
    private String subjectivePrompt;

    public OpenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public List<GptMultipleChoiceDto> gptMakeMultipleChoiceByContent(String content) {
        System.out.println("Request Content: " + content);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "user", "content", content),
                        Map.of("role", "system", "content", multipleChoicePrompt))
        );

        String rawResponse = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //System.out.println("Raw Response from GPT: " + rawResponse);

        return parseMulitpleChoiceDto(rawResponse);
    }

    private static List<GptMultipleChoiceDto> parseMulitpleChoiceDto(String rawResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(rawResponse, new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            String contentJson = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            List<GptMultipleChoiceDto> questionList = objectMapper.readValue(contentJson, new TypeReference<List<GptMultipleChoiceDto>>() {});
            return questionList;
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INVALID_GPT_RESPONSE);
        }
    }

    public List<GptSubjectiveDto> gptMakeSubjectiveQuestion(String content) {
        System.out.println("Request Content: " + content);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "user", "content", content),
                        Map.of("role", "system", "content", subjectivePrompt))
        );

        String rawResponse = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Raw Response from GPT: " + rawResponse);
        return parseSubjectiveDto(rawResponse);
    }

    private static List<GptSubjectiveDto> parseSubjectiveDto(String rawResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(rawResponse, new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            String contentJson = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            List<GptSubjectiveDto> questionList = objectMapper.readValue(contentJson, new TypeReference<List<GptSubjectiveDto>>() {});
            return questionList;
        }
        catch (Exception e) {
            throw new CommonException(ErrorCode.INVALID_GPT_RESPONSE);
        }
    }

}
