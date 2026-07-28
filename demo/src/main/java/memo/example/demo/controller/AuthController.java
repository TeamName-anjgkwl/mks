package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserIdResponse(1L));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new LoginResponse("access_token", "refresh_token", 1L, 100L));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(new AccessTokenResponse("new_access_token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam(name = "type", defaultValue = "CURRENT") String type) {
        return ResponseEntity.ok(new MessageResponse("로그아웃 완료"));
    }

    // V10: 기기 목록 조회 API 신설
    @GetMapping("/devices")
    public ResponseEntity<?> getDevices() {
        // return ResponseEntity.ok(authService.getDevices(userId));
        return ResponseEntity.ok(List.of(
                new DeviceResponse(1L, "iPhone 16 Pro", "2026-07-28T15:00:00")
        ));
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> logoutDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(new MessageResponse("해당 기기 로그아웃 완료"));
    }

    @PostMapping("/find")
    public ResponseEntity<?> findIdOrPw(@RequestBody FindRequest request) {
        return ResponseEntity.ok(new FindResponse("user_login_id"));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody VerifyPasswordRequest request) {
        return ResponseEntity.ok(new VerifyPasswordResponse(true));
    }

    @PostMapping("/2fa")
    public ResponseEntity<?> handle2FA(@RequestBody TwoFactorRequest request) {
        return ResponseEntity.ok(new MessageResponse("2FA 처리 완료"));
    }

    // --- DTOs ---
    // V10: name, email, phoneNumber 추가
    public record SignupRequest(String loginId, String password, String name, String nickname, String email, String phoneNumber, String provider) {}
    public record UserIdResponse(Long userId) {}
    public record LoginRequest(String loginId, String password, String deviceName) {}
    public record LoginResponse(String accessToken, String refreshToken, Long userId, Long deviceId) {}
    public record RefreshRequest(String refreshToken) {}
    public record AccessTokenResponse(String accessToken) {}

    // V10: 신규 DeviceResponse DTO
    public record DeviceResponse(Long deviceId, String deviceName, String lastLoginAt) {}

    public record MessageResponse(String message) {}
    public record FindRequest(String type, String email) {}
    public record FindResponse(String loginId) {}
    public record VerifyPasswordRequest(String password) {}
    public record VerifyPasswordResponse(Boolean isMatched) {}
    public record TwoFactorRequest(String action, String phoneNumber, String code) {}
}