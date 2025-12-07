package com.example.backend.controller;

import com.example.backend.entity.Movie;
import com.example.backend.service.AiRecommendationService;
import com.example.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final AiRecommendationService aiRecommendationService;

    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies(@RequestParam(defaultValue = "10") int limit) {
        List<Movie> recommendedMovies = recommendationService.getTopRatedMovies(limit);
        if (!recommendedMovies.isEmpty()) {
            return new ResponseEntity<>(recommendedMovies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/personalized/{userId}")
    public ResponseEntity<List<String>> getPersonalizedRecommendations(@PathVariable Long userId) {
        List<String> recommendations = aiRecommendationService.recommendForUser(userId);
        if (recommendations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }
}