package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamTodoRequestDto;
import memo.example.demo.DTO.request.TeamTodoUpdateRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.service.TeamTodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamTodoController {
    private final TeamTodoService teamTodoService;
    private final Long CURRENT_USER_ID = 1L; // 임시 유저 ID

    @PostMapping("/team-spaces/{teamSpaceId}/todos")
    public ResponseEntity<MessageResponseDto> createTodo(
            @PathVariable Long teamSpaceId,
            @RequestBody TeamTodoRequestDto request) {
        teamTodoService.createTodo(teamSpaceId, CURRENT_USER_ID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("할 일 생성 완료"));
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getTodos(@RequestParam(name = "teamSpaceId") Long teamSpaceId) {
        return ResponseEntity.ok(teamTodoService.getTodosByTeamSpace(teamSpaceId));
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<?> getTodoDetail(@PathVariable Long todoId) {
        return ResponseEntity.ok(teamTodoService.getTodoDetail(todoId));
    }

    @PatchMapping("/todos/{todoId}")
    public ResponseEntity<MessageResponseDto> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TeamTodoUpdateRequestDto request) {
        teamTodoService.updateTodo(todoId, request);
        return ResponseEntity.ok(new MessageResponseDto("할 일 업데이트 완료"));
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<MessageResponseDto> deleteTodo(@PathVariable Long todoId) {
        teamTodoService.deleteTodo(todoId);
        return ResponseEntity.ok(new MessageResponseDto("할 일 삭제 완료"));
    }
}