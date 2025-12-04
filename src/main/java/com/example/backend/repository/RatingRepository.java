package com.example.backend.repository;

import com.example.backend.entity.Rating; // Rating 엔티티를 임포트
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 이 인터페이스가 Spring의 Repository 컴포넌트임을 나타내
public interface RatingRepository extends JpaRepository<Rating, Long> { // Rating 엔티티와 기본 키 타입(Long) 지정

    // 기본적인 CRUD (save, findById, findAll, delete) 기능을 JpaRepository가 제공해!

    // 특정 사용자가 남긴 모든 평점 조회
    List<Rating> findByUserUserId(Long userId);

    // 특정 영화에 대한 모든 평점 조회
    List<Rating> findByMovieMovieId(Integer movieId);

    // 특정 사용자가 특정 영화에 남긴 평점 조회 (이미 남겼는지 확인할 때 유용)
    Optional<Rating> findByUserUserIdAndMovieMovieId(Long userId, Integer movieId);

    // 특정 영화의 평균 평점을 업데이트할 때 사용 (JPQL 예시) - 나중에 필요하면 추가!
    // @Query("SELECT AVG(r.score) FROM Rating r WHERE r.movie.movieId = :movieId")
    // Double findAverageScoreByMovieId(@Param("movieId") Integer movieId);
}