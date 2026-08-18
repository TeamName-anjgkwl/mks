package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.InquiryRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> createInquiry(
            @LoginUser Long userId,
            @RequestBody InquiryRequestDto request) {
        inquiryService.createInquiry(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("문의 접수 완료"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInquiries(@LoginUser Long userId) {
        return ResponseEntity.ok(inquiryService.getUserInquiries(userId));
    }
}