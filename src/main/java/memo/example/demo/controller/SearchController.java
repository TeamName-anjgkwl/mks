package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    @GetMapping
    public ResponseEntity<SearchResponseDto> globalSearch(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok(SearchResponseDto.builder()
                .memos(List.of(MemoResponseDto.builder().memoId(1L).title("Memo").content("content").build()))
                .notices(List.of(TeamNoticeResponseDto.builder().noticeId(1L).title("Notice").content("content").build()))
                .todos(List.of(TeamTodoResponseDto.builder().todoId(1L).title("Task").isChecked(false).build()))
                .schedules(List.of(ScheduleResponseDto.builder().scheduleId(1L).title("Meeting").build()))
                .build());
    }
}