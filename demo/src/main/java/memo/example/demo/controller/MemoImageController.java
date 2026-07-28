package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.MemoImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memo-images")
@RequiredArgsConstructor
public class MemoImageController {

    private final MemoImageService memoImageService;

    // 1. 메모에 이미지 URL 등록
    @PostMapping
    public ResponseEntity<Void> addMemoImage(@RequestBody MemoImageRequest request) {
        memoImageService.addImageToMemo(request.memoId(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 특정 메모의 이미지 목록 조회
    @GetMapping("/memo/{memoId}")
    public ResponseEntity<List<MemoImageResponse>> getMemoImages(@PathVariable Long memoId) {
        List<MemoImageResponse> images = memoImageService.getImagesByMemo(memoId);
        return ResponseEntity.ok(images);
    }

    // 3. 메모 이미지 개별 삭제
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteMemoImage(@PathVariable Long imageId) {
        memoImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    // --- DTO ---
    public record MemoImageRequest(Long memoId, String imageUrl) {}
    public record MemoImageResponse(Long imageId, String imageUrl) {}
}