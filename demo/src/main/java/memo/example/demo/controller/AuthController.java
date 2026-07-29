package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.*;
import memo.example.demo.DTO.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    // private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto request) {
        // authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("userId", 1L));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(LoginResponseDto.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .userId(1L)
                .deviceId(100L)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        return ResponseEntity.ok(Map.of("accessToken", "new_access_token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(@RequestParam(name = "type", defaultValue = "CURRENT") String type) {
        return ResponseEntity.ok(new MessageResponseDto("로그아웃 완료"));
    }

    @GetMapping("/devices")
    public ResponseEntity<?> getDevices() {
        return ResponseEntity.ok(List.of(
                DeviceResponseDto.builder().deviceId(1L).deviceName("iPhone 16 Pro").build()
        ));
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<MessageResponseDto> logoutDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(new MessageResponseDto("해당 기기 로그아웃 완료"));
    }

    @PostMapping("/find")
    public ResponseEntity<?> findIdOrPw(@RequestBody FindIdPwRequestDto request) {
        return ResponseEntity.ok(Map.of("loginId", "user_login_id"));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody VerifyPasswordRequestDto request) {
        return ResponseEntity.ok(Map.of("isMatched", true));
    }

    @PostMapping("/2fa")
    public ResponseEntity<MessageResponseDto> handle2FA(@RequestBody TwoFactorRequestDto request) {
        return ResponseEntity.ok(new MessageResponseDto("2FA 처리 완료"));
    }
}