package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.UserController.*;
import memo.example.demo.domain.User;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // V10: name, phoneNumber 추가 반영
    public void createUser(UserCreateRequest request) {
        User user = User.builder()
                .loginId(request.loginId())
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .nickname(request.nickname())
                .phoneNumber(request.phoneNumber())
                .provider(request.provider())
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserProfileResponse(user.getUserId(), user.getNickname(), user.getEmail(), user.getProfileImageUrl());
    }

    public void updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (request.nickname() != null) user.setNickname(request.nickname());
        if (request.profileImageUrl() != null) user.setProfileImageUrl(request.profileImageUrl());
    }

    // V10: 사용자 설정 업데이트 (timezone, use2fa 등 추가)
    public void updateUserSettings(Long userId, UserSettingsResponse request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (request.timezone() != null) user.setTimezone(request.timezone());
        if (request.allowPush() != null) user.setAllowPush(request.allowPush());
        // 추가 설정 항목 매핑 필요시 여기에 작성 (dateFormat, language, use2fa, allowEvent 등)
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setDeletedAt(LocalDateTime.now()); // Soft Delete
    }
}