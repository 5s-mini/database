// src/main/java/com/example/backend/service/UserService.java

package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

// ✨✨ DTO 클래스명을 'UserLoginRequest'와 'UserResponse'로 변경! ✨✨
//    (파일 이름을 바꿨으니 여기에 맞춰서 임포트도 변경해야 해!)
import com.example.backend.dto.UserLoginRequest; // UserLoginRequestDto -> UserLoginRequest로 변경!
import com.example.backend.dto.UserResponse;     // UserResponseDto -> UserResponse로 변경!

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder; // 비밀번호 암호화는 나중에!

    // 새로운 사용자 등록 (회원가입)
    @Transactional
    public User registerUser(User user) {
        // userRepository.findByUsername이 Optional<User>를 반환하므로 .isPresent() 사용!
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }
        // userRepository.findByEmail이 Optional<User>를 반환하므로 .isPresent() 사용!
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        // 비밀번호 암호화 로직은 보안을 위해 나중에 추가하는 것이 좋아.
        // 예: user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user); // 데이터베이스에 사용자 정보를 저장
    }


    // ⭐⭐⭐ 로그인 로직: 반환 타입 UserResponse로 변경! ⭐⭐⭐
    @Transactional(readOnly = true)
    public UserResponse login(String username, String password) { // UserResponseDto -> UserResponse로 변경!
        // 1. username으로 사용자 찾기 (Optional<User>를 반환하므로 .orElse(null)로 처리)
        User user = userRepository.findByUsername(username).orElse(null);

        // 2. 사용자가 존재하고, 비밀번호가 일치하는지 확인
        //    현재는 평문 비밀번호 비교를 사용하지만, 실제로는 passwordEncoder.matches()를 사용해야 합니다.
        if (user != null && user.getPassword().equals(password)) {
            // 3. 로그인 성공 시 UserResponse로 변환하여 반환
            //    (UserResponse의 생성자가 userId, username, email을 받도록 가정)
            return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
        }
        // 4. 로그인 실패
        return null;
    }
    // ⭐⭐⭐ 로그인 로직 끝! ⭐⭐⭐


    // 사용자 ID로 사용자 정보 조회
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // 사용자 이름으로 사용자 정보 조회 (반환 타입을 Optional<User>로 맞춰 줬어!)
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username); // UserRepository의 반환 타입과 맞춤!
    }

    // 모든 사용자 조회
    @Transactional(readOnly = true)
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 사용자 정보 업데이트
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                                         .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        // 비밀번호 업데이트 로직은 별도로 관리하는 것이 더 안전하고 명확함.

        return userRepository.save(existingUser);
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}