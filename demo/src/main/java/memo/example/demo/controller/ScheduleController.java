package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.ScheduleRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final Long CURRENT_USER_ID = 1L; // 임시

    @PostMapping
    public ResponseEntity<MessageResponseDto> createSchedule(@RequestBody ScheduleRequestDto request) {
        scheduleService.createSchedule(CURRENT_USER_ID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("일정 생성 완료"));
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(
            @RequestParam(name = "year") int year,
            @RequestParam(name = "month") int month,
            @RequestParam(name = "teamSpaceId", required = false) Long teamSpaceId) {

        // V10 명세에 맞춘 월별 일정 조회
        if (teamSpaceId != null) {
            return ResponseEntity.ok(scheduleService.getTeamSchedulesByMonth(teamSpaceId, year, month));
        }
        return ResponseEntity.ok(scheduleService.getUserSchedulesByMonth(CURRENT_USER_ID, year, month));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleDetail(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleDetail(scheduleId));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<MessageResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequestDto request) {
        scheduleService.updateSchedule(scheduleId, request);
        return ResponseEntity.ok(new MessageResponseDto("일정 수정 완료"));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<MessageResponseDto> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(new MessageResponseDto("일정 삭제 완료"));
    }
}