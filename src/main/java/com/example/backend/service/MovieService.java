package com.example.backend.service;

import com.example.backend.entity.Movie;
import com.example.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 나타내
@RequiredArgsConstructor // Lombok: final 필드들을 사용하여 생성자를 자동으로 생성
public class MovieService {

    private final MovieRepository movieRepository; // MovieRepository를 주입받아 사용

    // 새로운 영화 등록
    @Transactional
    public Movie registerMovie(Movie movie) {
        // 여기에 영화 제목 중복 확인 등 비즈니스 로직을 추가할 수 있어.
        // 예를 들어, 제목과 개봉 연도가 같은 영화는 등록 못하게 할 수도 있겠지?
        // if (movieRepository.findByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear()).isPresent()) {
        //    throw new IllegalArgumentException("동일한 제목과 개봉 연도를 가진 영화가 이미 존재합니다.");
        // }
        return movieRepository.save(movie); // 데이터베이스에 영화 정보 저장
    }

    // 모든 영화 조회
    @Transactional(readOnly = true)
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    // 특정 ID로 영화 조회
    @Transactional(readOnly = true)
    public Optional<Movie> findMovieById(Integer id) {
        return movieRepository.findById(id);
    }

    // 영화 제목으로 검색 (일부 일치)
    @Transactional(readOnly = true)
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContaining(title);
    }

    // 특정 감독의 영화 검색
    @Transactional(readOnly = true)
    public List<Movie> findMoviesByDirector(String director) {
        return movieRepository.findByDirector(director);
    }

    // 영화 정보 업데이트
    @Transactional
    public Movie updateMovie(Integer id, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(id)
                                         .orElseThrow(() -> new IllegalArgumentException("해당 ID의 영화를 찾을 수 없습니다."));

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setGenreIds(updatedMovie.getGenreIds());
        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setActors(updatedMovie.getActors());
        existingMovie.setSynopsis(updatedMovie.getSynopsis());
        existingMovie.setAvgRating(updatedMovie.getAvgRating());
        existingMovie.setPosterUrl(updatedMovie.getPosterUrl());

        return movieRepository.save(existingMovie); // 변경된 정보 저장
    }

    // 영화 삭제
    @Transactional
    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }
}