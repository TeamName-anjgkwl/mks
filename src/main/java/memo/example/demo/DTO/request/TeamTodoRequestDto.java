package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamTodoRequestDto {
    private String title;
    private String content;
    private String dueDate; // (예: "2026-07-30")
    private Boolean sendPush; // 등록 시 팀원 푸시 알림 전송 여부
}