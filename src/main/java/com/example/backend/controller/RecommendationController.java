package com.example.backend.controller;

import com.example.backend.entity.Movie;
import com.example.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 나타내
@RequestMapping("/api/recommendations") // 이 컨트롤러의 모든 API 요청은 "/api/recommendations" 경로로 시작
@RequiredArgsConstructor // Lombok: final 필드들을 사용하여 생성자를 자동으로 생성
public class RecommendationController {

    private final RecommendationService recommendationService; // RecommendationService를 주입받아 사용

    // GET /api/recommendations/top-rated?limit=5 : 가장 평점 높은 영화 추천 (기본 5개)
    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies(@RequestParam(defaultValue = "10") int limit) { // limit을 쿼리 파라미터로 받고, 기본값은 10개로 설정
        List<Movie> recommendedMovies = recommendationService.getTopRatedMovies(limit);
        if (!recommendedMovies.isEmpty()) {
            return new ResponseEntity<>(recommendedMovies, HttpStatus.OK); // 200 OK 응답
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 추천할 영화가 없으면 404 Not Found
        }
    }

    // 여기에 향후 다른 추천 방식의 API들을 추가할 수 있어.
    // 예: @GetMapping("/for-user/{userId}") 특정 사용자를 위한 추천 영화
    // 예: @GetMapping("/latest") 최신 개봉 영화
}