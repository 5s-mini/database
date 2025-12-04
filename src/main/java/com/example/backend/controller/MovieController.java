package com.example.backend.controller;

import com.example.backend.entity.Movie;
import com.example.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 나타내
@RequestMapping("/api/movies") // 이 컨트롤러의 모든 API 요청은 "/api/movies" 경로로 시작
@RequiredArgsConstructor // Lombok: final 필드들을 사용하여 생성자를 자동으로 생성
public class MovieController {

    private final MovieService movieService; // MovieService를 주입받아 사용

    // POST /api/movies : 새로운 영화 등록 (CREATE)
    @PostMapping
    public ResponseEntity<Movie> registerMovie(@RequestBody Movie movie) {
        try {
            Movie registeredMovie = movieService.registerMovie(movie);
            return new ResponseEntity<>(registeredMovie, HttpStatus.CREATED); // 201 Created 응답
        } catch (IllegalArgumentException e) {
            // 예를 들어, 영화 제목 중복 등 유효성 검사 실패 시
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400 Bad Request 응답
        }
    }

    // GET /api/movies : 모든 영화 조회 (READ)
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.findAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK); // 200 OK 응답
    }

    // GET /api/movies/{id} : 특정 ID로 영화 조회 (READ)
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        return movieService.findMovieById(id)
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK)) // 찾았으면 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 없으면 404 Not Found
    }

    // GET /api/movies/search?title={title} : 제목으로 영화 검색 (READ)
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieService.searchMoviesByTitle(title);
        if (!movies.isEmpty()) {
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 검색 결과가 없으면 404
        }
    }

    // GET /api/movies/director/{director} : 감독으로 영화 검색 (READ)
    @GetMapping("/director/{director}")
    public ResponseEntity<List<Movie>> getMoviesByDirector(@PathVariable String director) {
        List<Movie> movies = movieService.findMoviesByDirector(director);
        if (!movies.isEmpty()) {
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 검색 결과가 없으면 404
        }
    }


    // PUT /api/movies/{id} : 영화 정보 업데이트 (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie updatedMovie) {
        try {
            Movie movie = movieService.updateMovie(id, updatedMovie);
            return new ResponseEntity<>(movie, HttpStatus.OK); // 200 OK 응답
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404 Not Found (ID 없음)
        }
    }

    // DELETE /api/movies/{id} : 영화 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content 응답 (삭제 성공)
    }
}