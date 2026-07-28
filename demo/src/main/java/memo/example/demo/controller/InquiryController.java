package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<?> createInquiry(@RequestBody InquiryRequest request) {
        // inquiryService.createInquiry(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InquiryIdResponse(1L));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInquiries() {
        return ResponseEntity.ok(List.of(new InquiryResponse(1L, "WAITING", "문의 제목", "답변 대기중")));
    }

    // --- DTOs ---
    // V10: S3 첨부파일을 위한 attachmentUrl 추가
    public record InquiryRequest(String type, String title, String content, String attachmentUrl) {}
    public record InquiryIdResponse(Long inquiryId) {}
    public record InquiryResponse(Long inquiryId, String status, String title, String response) {}
}