package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.TeamTodo;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TeamTodoResponseDto {
    private Long todoId;
    private Long teamSpaceId;
    private Long userId;
    private String title;     // V10 반영
    private String content;
    private LocalDate dueDate;  // V10 반영
    private Boolean sendPush; // V10 반영
    private Boolean isChecked;
    private LocalDateTime updatedAt;

    public static TeamTodoResponseDto from(TeamTodo teamTodo) {
        return TeamTodoResponseDto.builder()
                .todoId(teamTodo.getTodoId())
                .teamSpaceId(teamTodo.getTeamSpace() != null ? teamTodo.getTeamSpace().getTeamSpaceId() : null)
                .userId(teamTodo.getUser() != null ? teamTodo.getUser().getUserId() : null)
                .title(teamTodo.getTitle())
                .content(teamTodo.getContent())
                .dueDate(teamTodo.getDueDate())
                .sendPush(teamTodo.getSendPush())
                .isChecked(teamTodo.getIsChecked())
                .updatedAt(teamTodo.getUpdatedAt() != null ? teamTodo.getUpdatedAt() : teamTodo.getCreatedAt())
                .build();
    }
}