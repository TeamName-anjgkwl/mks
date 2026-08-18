package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.DeleteTrashRequestDto;
import memo.example.demo.DTO.request.MemoRequestDto;
import memo.example.demo.DTO.request.MemoUpdateRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;

    @PostMapping("/memos")
    public ResponseEntity<?> createMemo(
            @LoginUser Long userId,
            @RequestBody MemoRequestDto request) {
        Long memoId = memoService.createMemo(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("memoId", memoId));
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
        return ResponseEntity.ok(new MessageResponseDto("메모가 업데이트되었습니다."));
    }

    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<MessageResponseDto> moveMemoToTrash(@PathVariable Long memoId) {
        memoService.moveMemoToTrash(memoId);
        return ResponseEntity.ok(new MessageResponseDto("메모가 휴지통으로 이동되었습니다."));
    }

    @PatchMapping("/memos/{memoId}/status")
    public ResponseEntity<MessageResponseDto> changeMemoStatus(
            @PathVariable Long memoId,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "value", required = false) String value) { // 필수값 해제 후 직접 검증

        // 수정됨: Null 방어 로직 (NPE 500 에러 방지 -> 400 에러 유도)
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("변경할 value 값이 누락되었습니다.");
        }

        if ("PIN".equalsIgnoreCase(action)) {
            memoService.updatePin(memoId, Boolean.parseBoolean(value));
        } else if ("STATUS".equalsIgnoreCase(action)) {
            try {
                memoService.updateStatus(memoId, MemoStatus.valueOf(value.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 메모 상태값입니다.");
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 action 입니다.");
        }
        return ResponseEntity.ok(new MessageResponseDto("메모 상태가 변경되었습니다."));
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getTrashList(@LoginUser Long userId) {
        return ResponseEntity.ok(memoService.getTrashList(userId));
    }

    @PatchMapping("/trash/{memoId}/restore")
    public ResponseEntity<MessageResponseDto> restoreMemo(@PathVariable Long memoId) {
        memoService.updateStatus(memoId, MemoStatus.NORMAL);
        return ResponseEntity.ok(new MessageResponseDto("메모가 복구되었습니다."));
    }

    @DeleteMapping("/trash")
    public ResponseEntity<MessageResponseDto> deleteMemosPermanently(@RequestBody DeleteTrashRequestDto request) {
        memoService.deleteMemosPermanently(request.getMemoIds());
        return ResponseEntity.ok(new MessageResponseDto("메모가 영구 삭제되었습니다."));
    }
}