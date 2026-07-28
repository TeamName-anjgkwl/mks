package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Schedule;
import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleResponseDto {
    private Long scheduleId;
    private Long teamSpaceId;
    private String title;
    private String content;
    private LocalDateTime startAt; // V10 반영
    private LocalDateTime endAt;   // V10 반영

    public static ScheduleResponseDto from(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .teamSpaceId(schedule.getTeamSpace() != null ? schedule.getTeamSpace().getTeamSpaceId() : null)
                .title(schedule.getSTitle())
                .content(schedule.getSContent())
                .startAt(schedule.getStartAt())
                .endAt(schedule.getEndAt())
                .build();
    }
}