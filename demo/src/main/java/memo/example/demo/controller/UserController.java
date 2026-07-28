package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(new UserProfileResponse(1L, "nickname", "email@test.com", "url"));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(new UserProfileResponse(1L, request.nickname(), "email@test.com", request.profileImageUrl()));
    }

    @GetMapping("/me/settings")
    public ResponseEntity<?> getMySettings() {
        return ResponseEntity.ok(new UserSettingsResponse("Asia/Seoul", "YYYY-MM-DD", "ko-KR", false, true, false));
    }

    @PatchMapping("/me/settings")
    public ResponseEntity<?> updateMySettings(@RequestBody UserSettingsResponse request) {
        // userService.updateUserSettings(userId, request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> withdrawUser() {
        return ResponseEntity.ok(new MessageResponse("탈퇴 처리 완료"));
    }

    // --- DTOs ---
    public record UserProfileResponse(Long userId, String nickname, String email, String profileImageUrl) {}
    public record UserProfileUpdateRequest(String nickname, String profileImageUrl) {}

    // V10: 전체 설정 필드 반영
    public record UserSettingsResponse(String timezone, String dateFormat, String language, Boolean use2fa, Boolean allowPush, Boolean allowEvent) {}
    public record MessageResponse(String message) {}
}