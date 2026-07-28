package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    @GetMapping
    public ResponseEntity<?> globalSearch(@RequestParam(name = "keyword") String keyword) {
        // teamSpaceId 라우팅 매핑 포함된 통합 응답
        return ResponseEntity.ok(new SearchResponse(
                List.of(new MemoSearchDto(1L, "Memo", "content", "2026-07-23T10:00:00")),
                List.of(new NoticeSearchDto(1L, 2L, "Notice", "content", "2026-07-23T10:00:00")),
                List.of(new TodoSearchDto(1L, 2L, "Task", "2026-07-30", false)),
                List.of(new ScheduleSearchDto(1L, 2L, "Meeting", "2026-07-23T10:00:00", "2026-07-23T11:00:00"))
        ));
    }

    // --- DTOs ---
    public record MemoSearchDto(Long memoId, String title, String content, String updatedAt) {}
    public record NoticeSearchDto(Long noticeId, Long teamSpaceId, String title, String content, String updatedAt) {}
    public record TodoSearchDto(Long todoId, Long teamSpaceId, String title, String dueDate, Boolean isChecked) {}
    public record ScheduleSearchDto(Long scheduleId, Long teamSpaceId, String title, String startAt, String endAt) {}

    public record SearchResponse(
            List<MemoSearchDto> memos,
            List<NoticeSearchDto> notices,
            List<TodoSearchDto> todos,
            List<ScheduleSearchDto> schedules
    ) {}
}