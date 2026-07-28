package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamNoticeController {

    @PostMapping("/team-spaces/{teamSpaceId}/notices")
    public ResponseEntity<?> createNotice(
            @PathVariable Long teamSpaceId,
            @RequestBody NoticeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new NoticeIdResponse(1L));
    }

    @GetMapping("/notices")
    public ResponseEntity<?> getNotices(@RequestParam(name = "teamSpaceId") Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new NoticeListResponse(1L, "Notice Title", false)));
    }

    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable Long noticeId) {
        return ResponseEntity.ok(new NoticeDetailResponse(noticeId, "Notice Title", "Content", false));
    }

    @PatchMapping("/notices/{noticeId}")
    public ResponseEntity<?> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/notices/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    public record NoticeRequest(String title, String content, Boolean isPinned) {}
    public record NoticeIdResponse(Long noticeId) {}
    public record NoticeListResponse(Long noticeId, String title, Boolean isPinned) {}
    public record NoticeDetailResponse(Long noticeId, String title, String content, Boolean isPinned) {}
    public record MessageResponse(String message) {}
}