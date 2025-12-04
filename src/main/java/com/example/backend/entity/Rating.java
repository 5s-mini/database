package com.example.backend.entity;

import jakarta.persistence.*; // JPA 관련 어노테이션
import lombok.*; // Lombok 어노테이션
import org.hibernate.annotations.CreationTimestamp; // 생성 시간 자동 생성 어노테이션

import java.time.LocalDateTime; // 자바 8 날짜/시간 API

@Entity // 이 클래스가 JPA 엔티티임을 나타내 (데이터베이스 테이블과 매핑됨)
@Table(name = "Ratings") // 매핑할 테이블의 이름을 지정
@Getter // Lombok: getter 메서드를 자동으로 생성
@Setter // Lombok: setter 메서드를 자동으로 생성
@NoArgsConstructor // Lombok: 기본 생성자를 자동으로 생성
@AllArgsConstructor // Lombok: 모든 필드를 인자로 받는 생성자를 자동으로 생성
@ToString(exclude = {"user", "movie"}) // Lombok: toString 시 순환 참조 방지 (user, movie는 제외)
public class Rating {

    @Id // 이 필드가 Primary Key (기본 키)임을 나타내
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임 (AUTO_INCREMENT)
    @Column(name = "rating_id") // 데이터베이스 컬럼 이름을 지정
    private Long ratingId;

    // User 엔티티와 다대일(Many-to-One) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정 (필요할 때만 User 정보를 로딩)
    @JoinColumn(name = "user_id", nullable = false) // Ratings 테이블의 user_id 컬럼이 User 엔티티의 기본 키를 참조
    private User user; // 평점을 남긴 사용자

    // Movie 엔티티와 다대일(Many-to-One) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정
    @JoinColumn(name = "movie_id", nullable = false) // Ratings 테이블의 movie_id 컬럼이 Movie 엔티티의 기본 키를 참조
    private Movie movie; // 평점을 받은 영화

    @Column(name = "score", nullable = false) // 평점 점수 (0.5 단위, 0.5 ~ 5.0)
    private Double score;

    @CreationTimestamp // 평점이 생성될 때 현재 시간을 자동으로 설정
    @Column(name = "rating_date", updatable = false) // 평점 날짜는 생성 후 업데이트 안 되게
    private LocalDateTime ratingDate;
}