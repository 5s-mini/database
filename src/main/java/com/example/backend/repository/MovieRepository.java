// src/main/java/com/example/backend/repository/MovieRepository.java

package com.example.backend.repository;

import com.example.backend.entity.Movie;
import org.springframework.data.domain.Pageable; // ✨✨ 이 라인 추가!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByTitleContaining(String title);
    List<Movie> findByReleaseYearGreaterThanEqual(Integer year);
    List<Movie> findByDirector(String director);

    // ⭐⭐ 이 라인을 이전의 'findTopNByOrderByAvgRatingDesc(int limit);' 대신 아래 코드로 변경해줘!
    List<Movie> findByOrderByAvgRatingDesc(Pageable pageable); // Pageable 객체를 매개변수로 받음
}