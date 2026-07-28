package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/service-notices")
@RequiredArgsConstructor
public class ServiceNoticeController {

    @GetMapping
    public ResponseEntity<?> getServiceNotices() {
        return ResponseEntity.ok(List.of(new ServiceNoticeResponse(1L, "v1.1 업데이트 공지", "2026-07-23T10:00:00")));
    }

    // --- DTOs ---
    public record ServiceNoticeResponse(Long noticeId, String title, String createdAt) {}
}