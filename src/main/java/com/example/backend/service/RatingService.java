package com.example.backend.service;

import com.example.backend.entity.Movie;
import com.example.backend.entity.Rating;
import com.example.backend.entity.User;
import com.example.backend.repository.MovieRepository;
import com.example.backend.repository.RatingRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 나타내
@RequiredArgsConstructor // Lombok: final 필드들을 사용하여 생성자를 자동으로 생성
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository; // 사용자 존재 여부 확인용
    private final MovieRepository movieRepository; // 영화 존재 여부 확인용

    // 새로운 평점 등록
    @Transactional
    public Rating addRating(Long userId, Integer movieId, Double score) {
        // 1. 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 2. 영화 존재 여부 확인
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: " + movieId));

        // 3. 이미 해당 사용자가 해당 영화에 평점을 남겼는지 확인 (선택 사항: 덮어쓰기 허용 여부에 따라)
        // Optional<Rating> existingRating = ratingRepository.findByUserUserIdAndMovieMovieId(userId, movieId);
        // if (existingRating.isPresent()) {
        //     // 이미 평점이 있으면, 업데이트 로직을 호출하거나 예외를 던질 수 있어.
        //     // 여기서는 일단 새로운 평점을 등록하는 것으로 진행.
        //     // 만약 중복 평점 불가라면: throw new IllegalArgumentException("이미 평점을 남긴 영화입니다.");
        // }

        // 4. 새로운 평점 엔티티 생성
        Rating newRating = new Rating();
        newRating.setUser(user);
        newRating.setMovie(movie);
        newRating.setScore(score);

        // 5. 평점 저장
        return ratingRepository.save(newRating);
    }

    // 특정 ID로 평점 조회
    @Transactional(readOnly = true)
    public Optional<Rating> findRatingById(Long id) {
        return ratingRepository.findById(id);
    }

    // 특정 사용자가 남긴 모든 평점 조회
    @Transactional(readOnly = true)
    public List<Rating> findRatingsByUser(Long userId) {
        return ratingRepository.findByUserUserId(userId);
    }

    // 특정 영화에 대한 모든 평점 조회
    @Transactional(readOnly = true)
    public List<Rating> findRatingsByMovie(Integer movieId) {
        return ratingRepository.findByMovieMovieId(movieId);
    }

    // 평점 업데이트
    @Transactional
    public Rating updateRating(Long ratingId, Double newScore) {
        Rating existingRating = ratingRepository.findById(ratingId)
                                            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 평점을 찾을 수 없습니다."));

        existingRating.setScore(newScore); // 점수만 업데이트

        return ratingRepository.save(existingRating); // 변경된 평점 저장
    }

    // 평점 삭제
    @Transactional
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}