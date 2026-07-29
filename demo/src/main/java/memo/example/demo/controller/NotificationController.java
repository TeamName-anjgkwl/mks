package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final Long CURRENT_USER_ID = 1L; // 임시 유저 ID

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        return ResponseEntity.ok(notificationService.getUserNotifications(CURRENT_USER_ID));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageResponseDto> readNotification(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(new MessageResponseDto("알림 읽음 처리 완료"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new MessageResponseDto("알림 삭제 완료"));
    }
}