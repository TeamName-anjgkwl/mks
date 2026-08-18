package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.MemoImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
public class MemoImageController {
    private final MemoImageService memoImageService;

    @PostMapping("/{memoId}/images")
    public ResponseEntity<?> addMemoImage(@PathVariable Long memoId, @RequestBody Map<String, String> request) {
        Long realImageId = memoImageService.addImageToMemo(memoId, request.get("imageUrl"));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("imageId", realImageId));
    }

    @GetMapping("/{memoId}/images")
    public ResponseEntity<?> getMemoImages(@PathVariable Long memoId) {
        return ResponseEntity.ok(memoImageService.getImagesByMemo(memoId));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteMemoImage(@PathVariable Long imageId) {
        memoImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}