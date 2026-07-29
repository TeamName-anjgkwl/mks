package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.UserProfileUpdateRequestDto;
import memo.example.demo.DTO.request.UserSettingsUpdateRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Long CURRENT_USER_ID = 1L; // 임시 유저 ID

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(userService.getUserProfile(CURRENT_USER_ID));
    }

    @PatchMapping("/me")
    public ResponseEntity<MessageResponseDto> updateMyProfile(@RequestBody UserProfileUpdateRequestDto request) {
        userService.updateUserProfile(CURRENT_USER_ID, request);
        return ResponseEntity.ok(new MessageResponseDto("프로필 업데이트 완료"));
    }

    @GetMapping("/me/settings")
    public ResponseEntity<?> getMySettings() {
        return ResponseEntity.ok(userService.getUserSettings(CURRENT_USER_ID));
    }

    @PatchMapping("/me/settings")
    public ResponseEntity<MessageResponseDto> updateMySettings(@RequestBody UserSettingsUpdateRequestDto request) {
        userService.updateUserSettings(CURRENT_USER_ID, request);
        return ResponseEntity.ok(new MessageResponseDto("설정 업데이트 완료"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<MessageResponseDto> withdrawUser() {
        userService.deleteUser(CURRENT_USER_ID);
        return ResponseEntity.ok(new MessageResponseDto("탈퇴 처리 완료"));
    }
}