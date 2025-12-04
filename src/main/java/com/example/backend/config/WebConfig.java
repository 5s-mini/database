// src/main/java/com/example/backend/config/WebConfig.java

package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 이 클래스가 스프링 설정 클래스임을 명시
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000") // ⭐️ 중요: 프론트엔드 URL 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 자격 증명 (쿠키, HTTP 인증 등) 허용
                .maxAge(3600); // Pre-flight 요청 결과를 캐시할 시간 (초)
    }
}