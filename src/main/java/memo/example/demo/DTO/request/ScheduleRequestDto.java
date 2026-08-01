package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    private Long teamSpaceId; // Null 허용 (개인 일정 시)
    private String title;
    private String content;
    private String startAt;
    private String endAt;
}