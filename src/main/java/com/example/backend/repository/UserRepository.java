// src/main/java/com/example/backend/repository/UserRepository.java

package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 이전에 이미 잘 되어 있었어!

    // ✨✨ 여기에 Optional<User> findByEmail 메서드를 추가해 줘야 해! ✨✨
    Optional<User> findByEmail(String email); // <<-- 이 라인 추가!

    boolean existsByUsername(String username); // 사용자 이름 중복 확인용 (회원가입 시 유용)
    boolean existsByEmail(String email);       // 이메일 중복 확인용 (회원가입 시 유용)
}