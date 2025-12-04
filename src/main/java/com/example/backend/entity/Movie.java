package com.example.backend.entity;

import jakarta.persistence.*; // JPA 관련 어노테이션
import lombok.*; // Lombok 어노테이션

import java.math.BigDecimal; // 평점 저장을 위한 BigDecimal

@Entity // 이 클래스가 JPA 엔티티임을 나타내 (데이터베이스 테이블과 매핑됨)
@Table(name = "Movies") // 매핑할 테이블의 이름을 지정
@Getter // Lombok: getter 메서드를 자동으로 생성
@Setter // Lombok: setter 메서드를 자동으로 생성
@NoArgsConstructor // Lombok: 기본 생성자를 자동으로 생성
@AllArgsConstructor // Lombok: 모든 필드를 인자로 받는 생성자를 자동으로 생성
@ToString // Lombok: toString 메서드를 자동으로 생성
public class Movie {

    @Id // 이 필드가 Primary Key (기본 키)임을 나타내
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임 (AUTO_INCREMENT)
    @Column(name = "movie_id") // 데이터베이스 컬럼 이름을 지정
    private Integer movieId; // MySQL의 INT에 매핑

    @Column(name = "title", nullable = false, length = 255) // 영화 제목 (NOT NULL)
    private String title;

    @Column(name = "release_year") // 개봉 연도
    private Integer releaseYear; // YEAR 타입은 Java에서 보통 Integer로 매핑

    @Column(name = "genre_ids", length = 255) // 장르 ID (쉼표로 구분된 문자열) - 임시
    private String genreIds;

    @Column(name = "director", length = 255) // 감독
    private String director;

    @Column(name = "actors", columnDefinition = "TEXT") // 배우들 (TEXT 타입)
    private String actors;

    @Column(name = "synopsis", columnDefinition = "TEXT") // 영화 줄거리 (TEXT 타입)
    private String synopsis;

    @Column(name = "avg_rating", precision = 2, scale = 1) // 평균 평점 (DECIMAL(2,1))
    private BigDecimal avgRating; // DECIMAL 타입은 Java에서 BigDecimal로 매핑

    @Column(name = "poster_url", length = 500) // 포스터 이미지 URL
    private String posterUrl;
}