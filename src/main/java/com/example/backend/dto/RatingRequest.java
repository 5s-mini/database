package com.example.backend.dto; // dto 패키지로!

import lombok.Data; // Lombok: getter, setter, toString 등을 자동으로 생성
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
public class RatingRequest {
    private Long userId;    // 평점을 남기는 사용자의 ID
    private Integer movieId; // 평점을 받는 영화의 ID
    private Double score;   // 평점 점수
}