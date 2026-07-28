package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCreateRequest request) {
        // scheduleService.createSchedule(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ScheduleIdResponse(1L));
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(
            @RequestParam(name = "year") String year,
            @RequestParam(name = "month") String month,
            @RequestParam(name = "teamSpaceId", required = false) Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new ScheduleListResponse(1L, "Meeting", "2026-07-23T10:00:00", "2026-07-23T11:00:00")));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleDetail(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleDetail(scheduleId));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleUpdateRequest request) {
        scheduleService.updateSchedule(scheduleId, request);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    // V10: content 추가 및 startAt/endAt 분리
    public record ScheduleCreateRequest(Long teamSpaceId, String title, String content, String startAt, String endAt) {}
    public record ScheduleIdResponse(Long scheduleId) {}
    public record ScheduleListResponse(Long scheduleId, String title, String startAt, String endAt) {}
    public record ScheduleDetailResponse(Long scheduleId, String title, String content, String startAt, String endAt) {}
    public record ScheduleUpdateRequest(String title, String content, String startAt, String endAt) {}
    public record MessageResponse(String message) {}
}