package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/memos")
    public ResponseEntity<?> createMemo(@RequestBody MemoCreateRequest request) {
        // memoService.createMemo(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemoIdResponse(1L));
    }

    @GetMapping("/memos")
    public ResponseEntity<?> getMemos(@RequestParam(name = "teamSpaceId", required = false) Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new MemoListResponse(1L, "title", "NORMAL", false)));
    }

    @GetMapping("/memos/{memoId}")
    public ResponseEntity<?> getMemoDetail(@PathVariable Long memoId) {
        return ResponseEntity.ok(new MemoDetailResponse(memoId, "title", "content", "NORMAL"));
    }

    @PatchMapping("/memos/{memoId}")
    public ResponseEntity<?> updateMemo(@PathVariable Long memoId, @RequestBody MemoUpdateRequest request) {
        memoService.updateMemo(memoId, request);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<?> moveMemoToTrash(@PathVariable Long memoId) {
        memoService.moveMemoToTrash(memoId);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // V10: PIN과 STATUS 분기 및 타입(value) 명확화
    @PatchMapping("/memos/{memoId}/status")
    public ResponseEntity<?> changeMemoStatus(
            @PathVariable Long memoId,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "value") String value) {

        if ("PIN".equalsIgnoreCase(action)) {
            boolean isPinned = Boolean.parseBoolean(value);
            memoService.updatePin(memoId, isPinned);
        } else if ("STATUS".equalsIgnoreCase(action)) {
            MemoStatus status = MemoStatus.valueOf(value.toUpperCase());
            memoService.updateStatus(memoId, status);
        }

        return ResponseEntity.ok(new MessageResponse("변경 완료"));
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getTrashList() {
        return ResponseEntity.ok(List.of(new TrashResponse(1L, "title", "2026-07-23T12:00:00")));
    }

    @PatchMapping("/trash/{memoId}/restore")
    public ResponseEntity<?> restoreMemo(@PathVariable Long memoId) {
        return ResponseEntity.ok(new MessageResponse("복구 완료"));
    }

    @DeleteMapping("/trash")
    public ResponseEntity<?> deleteMemosPermanently(@RequestBody DeleteTrashRequest request) {
        return ResponseEntity.ok(new MessageResponse("영구 삭제 완료"));
    }

    // --- DTOs ---
    // V10: expiredAt 추가
    public record MemoCreateRequest(Long teamSpaceId, String title, String content, String expiredAt, String status) {}
    public record MemoIdResponse(Long memoId) {}
    public record MemoListResponse(Long memoId, String title, String status, Boolean isPinned) {}
    public record MemoDetailResponse(Long memoId, String title, String content, String status) {}
    public record MemoUpdateRequest(String title, String content) {}
    public record MessageResponse(String message) {}
    public record TrashResponse(Long memoId, String title, String deletedAt) {}
    public record DeleteTrashRequest(List<Long> memoIds) {}
}