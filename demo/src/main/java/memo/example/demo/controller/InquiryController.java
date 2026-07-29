package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.InquiryRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;
    private final Long CURRENT_USER_ID = 1L; // 임시 유저 ID

    @PostMapping
    public ResponseEntity<MessageResponseDto> createInquiry(@RequestBody InquiryRequestDto request) {
        inquiryService.createInquiry(CURRENT_USER_ID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("문의 접수 완료"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInquiries() {
        return ResponseEntity.ok(inquiryService.getUserInquiries(CURRENT_USER_ID));
    }
}