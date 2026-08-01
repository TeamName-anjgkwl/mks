package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.*;
import memo.example.demo.DTO.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto request) {
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
        return ResponseEntity.ok(new MessageResponseDto("성공적으로 로그아웃 되었습니다."));
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
        return ResponseEntity.ok(new MessageResponseDto("2FA 처리 성공"));
    }
}