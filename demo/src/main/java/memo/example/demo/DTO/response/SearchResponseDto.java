package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class SearchResponseDto {
    private List<MemoResponseDto> memos;
    private List<TeamNoticeResponseDto> notices;
    private List<TeamTodoResponseDto> todos;
    private List<ScheduleResponseDto> schedules;
}