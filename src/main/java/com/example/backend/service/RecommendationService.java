// src/main/java/com/example/backend/service/RecommendationService.java

package com.example.backend.service;

import com.example.backend.entity.Movie;
import com.example.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest; // ✨✨ 이 라인 추가!
import org.springframework.data.domain.Pageable;   // ✨✨ 이 라인 추가!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<Movie> getTopRatedMovies(int limit) {
        // ⭐⭐ 이 메서드 내부도 PageRequest를 사용하도록 수정!
        Pageable pageable = PageRequest.of(0, limit);
        return movieRepository.findByOrderByAvgRatingDesc(pageable);
    }
}