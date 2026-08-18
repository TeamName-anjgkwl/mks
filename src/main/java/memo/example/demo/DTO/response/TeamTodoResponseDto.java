package memo.example.demo.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String title;
    private String content;

    // LocalDate는 시간대가 없으므로 timezone 설정을 제거하여 날짜 밀림(Off-by-one) 현상을 방지합니다.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private Boolean sendPush;
    private Boolean isChecked;

    // LocalDateTime은 시간 정보가 있으므로 그대로 KST 시간대를 유지합니다.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
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