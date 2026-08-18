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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
    }

    @Transactional(readOnly = true)
    public UserSettingsResponseDto getUserSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String maskedDestination = null;
        if (Boolean.TRUE.equals(user.getUse2fa())) {
            if ("EMAIL".equalsIgnoreCase(user.getTwoFactorMethod()) && user.getEmail() != null) {
                maskedDestination = maskEmail(user.getEmail());
            } else if ("PHONE".equalsIgnoreCase(user.getTwoFactorMethod()) && user.getPhoneNumber() != null) {
                maskedDestination = maskPhone(user.getPhoneNumber());
            }
        }

        return UserSettingsResponseDto.from(user, maskedDestination);
    }

    public void updateUserSettings(Long userId, UserSettingsUpdateRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        if (request.getTimezone() != null) user.setTimezone(request.getTimezone());
        if (request.getDateFormat() != null) user.setDateFormat(request.getDateFormat());
        if (request.getLanguage() != null) user.setLanguage(request.getLanguage());
        if (request.getUse2fa() != null) user.setUse2fa(request.getUse2fa());
        if (request.getAllowPush() != null) user.setAllowPush(request.getAllowPush());
        if (request.getAllowEvent() != null) user.setAllowEvent(request.getAllowEvent());
        if (request.getTwoFactorMethod() != null) user.setTwoFactorMethod(request.getTwoFactorMethod().toUpperCase());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setDeletedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponseDto> searchUsersByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return userRepository.searchByKeyword(keyword.trim()).stream()
                .map(UserProfileResponseDto::from)
                .collect(Collectors.toList());
    }

    // 마스킹 헬퍼 메서드
    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }

    private String maskPhone(String phone) {
        String[] parts = phone.split("-");
        if (parts.length == 3) {
            return parts[0] + "-****-" + parts[2];
        }
        return "***"; // 예외 형식 처리
    }
}