package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        return ResponseEntity.ok(List.of(new NotificationResponse(1L, "MEMO", 123L, "알림 메시지", false)));
    }

    // V10: 명시적인 /read 경로 확정
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> readNotification(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    public record NotificationResponse(Long notificationId, String type, Long targetId, String message, Boolean isRead) {}
    public record MessageResponse(String message) {}
}