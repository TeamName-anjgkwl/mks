package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamTodoRequestDto;
import memo.example.demo.DTO.request.TeamTodoUpdateRequestDto;
import memo.example.demo.DTO.response.TeamTodoResponseDto;
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

    public void createTodo(Long teamSpaceId, Long userId, TeamTodoRequestDto request) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        TeamTodo todo = TeamTodo.builder()
                .teamSpace(teamSpace)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .dueDate(request.getDueDate() != null ? LocalDate.parse(request.getDueDate()) : null)
                .sendPush(request.getSendPush() != null ? request.getSendPush() : false)
                .isChecked(false)
                .build();
        teamTodoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<TeamTodoResponseDto> getTodosByTeamSpace(Long teamSpaceId) {
        return teamTodoRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(TeamTodoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamTodoResponseDto getTodoDetail(Long todoId) {
        TeamTodo t = teamTodoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할 일을 찾을 수 없습니다."));
        return TeamTodoResponseDto.from(t);
    }

    public void updateTodo(Long todoId, TeamTodoUpdateRequestDto request) {
        TeamTodo todo = teamTodoRepository.findById(todoId).orElseThrow();
        if (request.getTitle() != null) todo.setTitle(request.getTitle());
        if (request.getContent() != null) todo.setContent(request.getContent());
        if (request.getIsChecked() != null) todo.setIsChecked(request.getIsChecked());
        if (request.getDueDate() != null) todo.setDueDate(LocalDate.parse(request.getDueDate()));
        if (request.getSendPush() != null) todo.setSendPush(request.getSendPush());
    }

    public void deleteTodo(Long todoId) {
        teamTodoRepository.deleteById(todoId);
    }
}