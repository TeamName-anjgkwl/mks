package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.TeamTodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamTodoController {

    private final TeamTodoService teamTodoService;

    @PostMapping("/team-spaces/{teamSpaceId}/todos")
    public ResponseEntity<?> createTodo(
            @PathVariable Long teamSpaceId,
            @RequestBody TodoRequest request) {
        // teamTodoService.createTodo(teamSpaceId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TodoIdResponse(1L));
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getTodos(@RequestParam(name = "teamSpaceId") Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new TodoResponse(1L, "Task", "2026-07-30", false)));
    }

    // V10: 알림 타겟 딥링크용 할 일 상세 조회 신설
    @GetMapping("/todos/{todoId}")
    public ResponseEntity<?> getTodoDetail(@PathVariable Long todoId) {
        return ResponseEntity.ok(teamTodoService.getTodoDetail(todoId));
    }

    @PatchMapping("/todos/{todoId}")
    public ResponseEntity<?> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoUpdateRequest request) {
        teamTodoService.updateTodo(todoId, request);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId) {
        teamTodoService.deleteTodo(todoId);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    // V10: dueDate, sendPush 반영
    public record TodoRequest(String title, String content, String dueDate, Boolean sendPush) {}
    public record TodoUpdateRequest(String title, String content, String dueDate, Boolean isChecked, Boolean sendPush) {}
    public record TodoIdResponse(Long todoId) {}
    public record TodoResponse(Long todoId, String title, String dueDate, Boolean isChecked) {}

    // V10: teamSpaceId가 포함된 상세 응답 (Service에서 선언된 DTO 사용)
    public record TodoDetailResponse(Long todoId, Long teamSpaceId, String title, String content, String dueDate, Boolean sendPush, Boolean isChecked, String updatedAt) {}

    public record MessageResponse(String message) {}
}