package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.SignUpRequestDto;
import memo.example.demo.DTO.request.UserProfileUpdateRequestDto;
import memo.example.demo.DTO.request.UserSettingsUpdateRequestDto;
import memo.example.demo.DTO.response.UserProfileResponseDto;
import memo.example.demo.DTO.response.UserSettingsResponseDto;
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

    public void createUser(SignUpRequestDto request) {
        User user = User.builder()
                .loginId(request.getLoginId())
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .provider(request.getProvider())
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserProfileResponseDto.from(user);
    }

    public void updateUserProfile(Long userId, UserProfileUpdateRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
    }

    @Transactional(readOnly = true)
    public UserSettingsResponseDto getUserSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserSettingsResponseDto.from(user);
    }

    public void updateUserSettings(Long userId, UserSettingsUpdateRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        if (request.getTimezone() != null) user.setTimezone(request.getTimezone());
        if (request.getDateFormat() != null) user.setDateFormat(request.getDateFormat());
        if (request.getLanguage() != null) user.setLanguage(request.getLanguage());
        if (request.getUse2fa() != null) user.setUse2fa(request.getUse2fa());
        if (request.getAllowPush() != null) user.setAllowPush(request.getAllowPush());
        if (request.getAllowEvent() != null) user.setAllowEvent(request.getAllowEvent());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setDeletedAt(LocalDateTime.now());
    }
}