package com.example.backend.controller;

import com.example.backend.dto.RatingRequest; // 새로 만든 DTO 임포트!
import com.example.backend.entity.Rating;
import com.example.backend.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 나타내
@RequestMapping("/api/ratings") // 이 컨트롤러의 모든 API 요청은 "/api/ratings" 경로로 시작
@RequiredArgsConstructor // Lombok: final 필드들을 사용하여 생성자를 자동으로 생성
public class RatingController {

    private final RatingService ratingService; // RatingService를 주입받아 사용

    // POST /api/ratings : 새로운 평점 등록 (CREATE)
    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody RatingRequest ratingDto) { // DTO를 통해 요청 본문 받음
        try {
            Rating newRating = ratingService.addRating(
                ratingDto.getUserId(),
                ratingDto.getMovieId(),
                ratingDto.getScore()
            );
            return new ResponseEntity<>(newRating, HttpStatus.CREATED); // 201 Created 응답
        } catch (IllegalArgumentException e) {
            // 사용자나 영화 ID가 유효하지 않을 때 등
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400 Bad Request 응답
        }
    }

    // GET /api/ratings/{id} : 특정 ID로 평점 조회 (READ)
    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        return ratingService.findRatingById(id)
                .map(rating -> new ResponseEntity<>(rating, HttpStatus.OK)) // 찾았으면 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 없으면 404 Not Found
    }

    // GET /api/ratings/user/{userId} : 특정 사용자가 남긴 모든 평점 조회 (READ)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable Long userId) {
        List<Rating> ratings = ratingService.findRatingsByUser(userId);
        if (!ratings.isEmpty()) {
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 평점이 없으면 404
        }
    }

    // GET /api/ratings/movie/{movieId} : 특정 영화에 대한 모든 평점 조회 (READ)
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getRatingsByMovie(@PathVariable Integer movieId) {
        List<Rating> ratings = ratingService.findRatingsByMovie(movieId);
        if (!ratings.isEmpty()) {
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 평점이 없으면 404
        }
    }

    // PUT /api/ratings/{id} : 평점 업데이트 (점수만 변경) (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long id, @RequestBody Double score) { // 점수만 받아서 업데이트
        try {
            Rating updatedRating = ratingService.updateRating(id, score);
            return new ResponseEntity<>(updatedRating, HttpStatus.OK); // 200 OK 응답
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404 Not Found (ID 없음)
        }
    }

    // DELETE /api/ratings/{id} : 평점 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content 응답 (삭제 성공)
    }
}