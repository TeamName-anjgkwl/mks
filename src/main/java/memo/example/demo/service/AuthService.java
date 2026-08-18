package memo.example.demo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import memo.example.demo.DTO.request.LoginRequestDto;
import memo.example.demo.DTO.request.FindIdPwRequestDto;
import memo.example.demo.DTO.request.SignUpRequestDto;
import memo.example.demo.DTO.request.SocialLoginRequestDto;
import memo.example.demo.DTO.request.TwoFactorRequestDto;
import memo.example.demo.DTO.response.LoginResponseDto;
import memo.example.demo.Exception.ExpiredCodeException;
import memo.example.demo.Exception.InvalidCodeException;
import memo.example.demo.config.jwt.JwtTokenProvider;
import memo.example.demo.domain.Device;
import memo.example.demo.domain.User;
import memo.example.demo.repository.DeviceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, TwoFactorSession> mfaSessions = new ConcurrentHashMap<>();

    @Getter
    @Setter
    public static class TwoFactorSession {
        private String code;
        private LocalDateTime expiresAt;
        private LocalDateTime lastSentAt;
        private int failureCount;
        private boolean isVerified = false;
    }

    public Long signup(SignUpRequestDto request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        User user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .provider(request.getProvider())
                .build();
        return userRepository.save(user).getUserId();
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return createLoginSession(user, request.getDeviceName());
    }

    public Map<String, Object> socialLogin(SocialLoginRequestDto request) {
        boolean isNewUser = false;
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if ("LOCAL".equals(user.getProvider())) {
                user.setProvider(request.getProvider());
                user.setProviderId(request.getProviderId());
            }
        } else {
            isNewUser = true;
            String uniqueLoginId = request.getProvider().toLowerCase() + "_" + UUID.randomUUID().toString().substring(0, 8);

            user = User.builder()
                    .loginId(uniqueLoginId)
                    .password(null)
                    .name(request.getName() != null ? request.getName() : "소셜회원")
                    .nickname(request.getName() != null ? request.getName() : "소셜회원")
                    .email(request.getEmail())
                    .provider(request.getProvider())
                    .providerId(request.getProviderId())
                    .profileImageUrl(request.getProfileImageUrl())
                    .build();
            userRepository.save(user);
        }

        LoginResponseDto loginResponse = createLoginSession(user, request.getDeviceName());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", loginResponse.getAccessToken());
        response.put("refreshToken", loginResponse.getRefreshToken());
        response.put("userId", loginResponse.getUserId());
        response.put("deviceId", loginResponse.getDeviceId());
        response.put("isNewUser", isNewUser);

        return response;
    }

    private LoginResponseDto createLoginSession(User user, String deviceName) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
        Device device = Device.builder()
                .user(user)
                .deviceName(deviceName != null ? deviceName : "Unknown Device")
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(14))
                .build();
        deviceRepository.save(device);
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .deviceId(device.getDeviceId())
                .build();
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Device device = deviceRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        if (device.getExpiresAt().isBefore(LocalDateTime.now())) {
            deviceRepository.delete(device);
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(device.getUser().getUserId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(device.getUser().getUserId());
        device.setRefreshToken(newRefreshToken);
        device.setExpiresAt(LocalDateTime.now().plusDays(14));
        deviceRepository.save(device);
        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(device.getUser().getUserId())
                .deviceId(device.getDeviceId())
                .build();
    }

    public void logout(Long userId, String type, String refreshToken) {
        if ("ALL".equalsIgnoreCase(type)) {
            deviceRepository.deleteByUser_UserId(userId);
        } else {
            if (refreshToken != null) {
                deviceRepository.findByRefreshToken(refreshToken)
                        .ifPresent(device -> {
                            if (device.getUser().getUserId().equals(userId)) {
                                deviceRepository.delete(device);
                            }
                        });
            }
        }
    }

    @Transactional(readOnly = true)
    public String findLoginId(FindIdPwRequestDto request) {
        if (!"ID".equalsIgnoreCase(request.getType())) {
            throw new IllegalArgumentException("아이디 찾기 요청만 지원합니다.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해 주세요.");
        }

        return userRepository.findByEmail(request.getEmail().trim())
                .map(User::getLoginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 사용자를 찾을 수 없습니다."));
    }

    public Object handle2FA(TwoFactorRequestDto request) {
        String method = request.getMethod() != null ? request.getMethod().toUpperCase() : "PHONE";
        String destination = "EMAIL".equals(method) ? request.getEmail() : request.getPhoneNumber();

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("인증 대상 정보가 누락되었습니다.");
        }

        if ("SEND".equalsIgnoreCase(request.getAction())) {
            TwoFactorSession session = mfaSessions.getOrDefault(destination, new TwoFactorSession());
            if (session.getLastSentAt() != null && session.getLastSentAt().plusMinutes(1).isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("인증번호는 1분마다 재발송할 수 있습니다.");
            }
            String code = String.format("%06d", new Random().nextInt(1000000));
            session.setCode(code);
            session.setExpiresAt(LocalDateTime.now().plusMinutes(3));
            session.setLastSentAt(LocalDateTime.now());
            session.setFailureCount(0);
            session.setVerified(false);
            mfaSessions.put(destination, session);
            System.out.println("[" + destination + "] 발송된 2FA 코드: " + code);

            return null;

        } else if ("VERIFY".equalsIgnoreCase(request.getAction())) {
            TwoFactorSession session = mfaSessions.get(destination);
            if (session == null) throw new IllegalArgumentException("인증번호 발송 이력이 없습니다.");
            if (session.getFailureCount() >= 5) {
                mfaSessions.remove(destination);
                throw new IllegalStateException("인증 실패 횟수(5회)를 초과했습니다. 재발송해 주세요.");
            }
            if (session.getExpiresAt().isBefore(LocalDateTime.now())) throw new ExpiredCodeException("인증번호가 만료되었습니다.");
            if (!session.getCode().equals(request.getCode())) {
                session.setFailureCount(session.getFailureCount() + 1);
                throw new InvalidCodeException("잘못된 인증번호입니다. (남은 횟수: " + (5 - session.getFailureCount()) + ")");
            }

            session.setVerified(true);

            User user = "EMAIL".equals(method)
                    ? userRepository.findByEmail(destination).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."))
                    : userRepository.findByPhoneNumber(destination).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            return createLoginSession(user, "2FA Verified Device");

        } else if ("SETUP".equalsIgnoreCase(request.getAction())) {
            TwoFactorSession session = mfaSessions.get(destination);
            if (session == null || !session.isVerified()) {
                throw new SecurityException("인증번호 검증(VERIFY)이 선행되어야 합니다.");
            }

            User user = "EMAIL".equals(method)
                    ? userRepository.findByEmail(destination).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."))
                    : userRepository.findByPhoneNumber(destination).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            user.setUse2fa(true);
            user.setTwoFactorMethod(method);

            mfaSessions.remove(destination);
            return null;
        } else {
            throw new IllegalArgumentException("지원하지 않는 인증 액션입니다.");
        }
    }
}