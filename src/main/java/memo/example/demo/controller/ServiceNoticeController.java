package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.ServiceNoticeResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/service-notices")
@RequiredArgsConstructor
public class ServiceNoticeController {

    @GetMapping
    public ResponseEntity<?> getServiceNotices() {
        return ResponseEntity.ok(List.of(
                ServiceNoticeResponseDto.builder().noticeId(1L).title("v1.1 업데이트").createdAt(LocalDateTime.now()).build()
        ));
    }
}