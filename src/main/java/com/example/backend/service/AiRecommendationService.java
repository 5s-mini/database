package com.example.backend.service;

import com.example.backend.entity.Rating;
import com.example.backend.repository.RatingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private static final String OPENAI_CHAT_COMPLETION_URL = "https://api.openai.com/v1/chat/completions";

    private final RatingRepository ratingRepository;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    @Transactional(readOnly = true)
    public List<String> recommendForUser(Long userId) {
        List<Rating> ratings = ratingRepository.findByUserUserId(userId);
        String prompt = buildPrompt(ratings);

        String requestBody = createRequestBody(prompt);
        Request request = new Request.Builder()
                .url(OPENAI_CHAT_COMPLETION_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, JSON_MEDIA_TYPE))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() == null) {
                return Collections.emptyList();
            }
            if (!response.isSuccessful()) {
                return Collections.emptyList();
            }
            String responseBody = response.body().string();
            return parseRecommendations(responseBody);
        } catch (IOException e) {
            throw new IllegalStateException("LLM 추천 호출에 실패했습니다.", e);
        }
    }

    private String buildPrompt(List<Rating> ratings) {
        if (ratings.isEmpty()) {
            return "이 사용자는 아직 영화를 평가하지 않았어. 최근 인기 있는 영화를 5편 추천해줘. 응답은 영화 제목만 JSON 배열로 돌려줘.";
        }

        String movieSummaries = ratings.stream()
                .map(rating -> String.format("%s (점수: %.1f)", rating.getMovie().getTitle(), rating.getScore()))
                .collect(Collectors.joining(", "));

        return "사용자가 시청하고 평가한 영화 목록은 다음과 같아: " + movieSummaries
                + ". 이 취향을 기반으로 새로운 영화를 5편 추천하고, 영화 제목만 포함된 JSON 배열로 반환해줘.";
    }

    private String createRequestBody(String prompt) {
        Map<String, Object> payload = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a movie recommendation assistant."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("LLM 요청 본문을 만들지 못했습니다.", e);
        }
    }

    private List<String> parseRecommendations(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            if (contentNode.isMissingNode()) {
                return Collections.emptyList();
            }

            String content = contentNode.asText();
            return objectMapper.readValue(content, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return fallbackTitles(responseBody);
        }
    }

    private List<String> fallbackTitles(String responseBody) {
        List<String> fallback = new ArrayList<>();
        fallback.add("추천 결과를 처리하는 중 문제가 발생했습니다.");
        fallback.add(responseBody);
        return fallback;
    }
}