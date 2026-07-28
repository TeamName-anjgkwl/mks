package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.TeamTodoController.*;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.TeamTodo;
import memo.example.demo.domain.User;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.TeamTodoRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamTodoService {

    private final TeamTodoRepository teamTodoRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final UserRepository userRepository;

    // V10: title, dueDate, sendPush 반영
    public void createTodo(Long teamSpaceId, Long userId, TodoRequest request) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        TeamTodo todo = TeamTodo.builder()
                .teamSpace(teamSpace)
                .user(user)
                .title(request.title())
                .content(request.content())
                .dueDate(request.dueDate() != null ? LocalDate.parse(request.dueDate()) : null)
                .sendPush(request.sendPush() != null ? request.sendPush() : false)
                .isChecked(false)
                .build();

        teamTodoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodosByTeamSpace(Long teamSpaceId) {
        return teamTodoRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(t -> new TodoResponse(
                        t.getTodoId(),
                        t.getTitle(),
                        t.getIsChecked()
                ))
                .collect(Collectors.toList());
    }

    // V10: 알림 딥링크 라우팅용 상세 조회 신설 (teamSpaceId 반환)
    @Transactional(readOnly = true)
    public TodoDetailResponse getTodoDetail(Long todoId) {
        TeamTodo t = teamTodoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할 일을 찾을 수 없습니다."));

        return new TodoDetailResponse(
                t.getTodoId(),
                t.getTeamSpace().getTeamSpaceId(),
                t.getTitle(),
                t.getContent(),
                t.getDueDate() != null ? t.getDueDate().toString() : null,
                t.getSendPush(),
                t.getIsChecked(),
                t.getUpdatedAt() != null ? t.getUpdatedAt().toString() : t.getCreatedAt().toString()
        );
    }

    public void updateTodo(Long todoId, TodoUpdateRequest request) {
        TeamTodo todo = teamTodoRepository.findById(todoId).orElseThrow();
        if (request.title() != null) todo.setTitle(request.title());
        if (request.content() != null) todo.setContent(request.content());
        if (request.isChecked() != null) todo.setIsChecked(request.isChecked());
        if (request.dueDate() != null) todo.setDueDate(LocalDate.parse(request.dueDate()));
    }

    public void deleteTodo(Long todoId) {
        teamTodoRepository.deleteById(todoId);
    }
}