package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.PresignedUrlResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    // 1. S3 Presigned URL 발급
    @GetMapping("/files/presigned-url")
    public ResponseEntity<PresignedUrlResponseDto> getPresignedUrl(
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "fileType") String fileType,
            @RequestParam(name = "domain") String domain) {

        return ResponseEntity.ok(PresignedUrlResponseDto.builder()
                .presignedUrl("https://s3-put-url...") // 추후 S3 연결 시 수정
                .fileUrl("https://s3-final-url...")
                .build());
    }
}