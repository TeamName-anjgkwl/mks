package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.UserProfileUpdateRequestDto;
import memo.example.demo.DTO.request.UserSettingsUpdateRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser; // @LoginUser 임포트
import memo.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PatchMapping("/me")
    public ResponseEntity<MessageResponseDto> updateMyProfile(
            @LoginUser Long userId,
            @RequestBody UserProfileUpdateRequestDto request) {
        userService.updateUserProfile(userId, request);
        return ResponseEntity.ok(new MessageResponseDto("프로필 업데이트 완료"));
    }

    @GetMapping("/me/settings")
    public ResponseEntity<?> getMySettings(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.getUserSettings(userId));
    }

    @PatchMapping("/me/settings")
    public ResponseEntity<MessageResponseDto> updateMySettings(
            @LoginUser Long userId,
            @RequestBody UserSettingsUpdateRequestDto request) {
        userService.updateUserSettings(userId, request);
        return ResponseEntity.ok(new MessageResponseDto("설정 업데이트 완료"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<MessageResponseDto> withdrawUser(@LoginUser Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResponseDto("탈퇴 처리 완료"));
    }
}