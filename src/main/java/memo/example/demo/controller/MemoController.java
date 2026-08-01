package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.DeleteTrashRequestDto;
import memo.example.demo.DTO.request.MemoRequestDto;
import memo.example.demo.DTO.request.MemoUpdateRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser; // @LoginUser 임포트
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;
    // CURRENT_USER_ID = 1L 하드코딩 완전 제거!

    @PostMapping("/memos")
    public ResponseEntity<MessageResponseDto> createMemo(
            @LoginUser Long userId,
            @RequestBody MemoRequestDto request) {
        memoService.createMemo(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("메모 생성 완료"));
    }

    @GetMapping("/memos")
    public ResponseEntity<?> getMemos(
            @LoginUser Long userId,
            @RequestParam(name = "teamSpaceId", required = false) Long teamSpaceId) {
        if (teamSpaceId != null) {
            return ResponseEntity.ok(memoService.getTeamMemos(teamSpaceId));
        }
        return ResponseEntity.ok(memoService.getUserMemos(userId));
    }

    @GetMapping("/memos/{memoId}")
    public ResponseEntity<?> getMemoDetail(@PathVariable Long memoId) {
        return ResponseEntity.ok(memoService.getMemoDetail(memoId));
    }

    @PatchMapping("/memos/{memoId}")
    public ResponseEntity<MessageResponseDto> updateMemo(
            @PathVariable Long memoId,
            @RequestBody MemoUpdateRequestDto request) {
        memoService.updateMemo(memoId, request);
        return ResponseEntity.ok(new MessageResponseDto("메모 수정 완료"));
    }

    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<MessageResponseDto> moveMemoToTrash(@PathVariable Long memoId) {
        memoService.moveMemoToTrash(memoId);
        return ResponseEntity.ok(new MessageResponseDto("휴지통 이동 완료"));
    }

    @PatchMapping("/memos/{memoId}/status")
    public ResponseEntity<MessageResponseDto> changeMemoStatus(
            @PathVariable Long memoId,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "value") String value) {
        if ("PIN".equalsIgnoreCase(action)) {
            memoService.updatePin(memoId, Boolean.parseBoolean(value));
        } else if ("STATUS".equalsIgnoreCase(action)) {
            memoService.updateStatus(memoId, MemoStatus.valueOf(value.toUpperCase()));
        }
        return ResponseEntity.ok(new MessageResponseDto("상태 변경 완료"));
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getTrashList(@LoginUser Long userId) {
        return ResponseEntity.ok(memoService.getTrashList(userId));
    }

    @PatchMapping("/trash/{memoId}/restore")
    public ResponseEntity<MessageResponseDto> restoreMemo(@PathVariable Long memoId) {
        memoService.updateStatus(memoId, MemoStatus.NORMAL);
        return ResponseEntity.ok(new MessageResponseDto("메모 복구 완료"));
    }

    @DeleteMapping("/trash")
    public ResponseEntity<MessageResponseDto> deleteMemosPermanently(@RequestBody DeleteTrashRequestDto request) {
        memoService.deleteMemosPermanently(request.getMemoIds());
        return ResponseEntity.ok(new MessageResponseDto("영구 삭제 완료"));
    }
}