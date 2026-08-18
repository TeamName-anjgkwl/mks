package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.ScheduleRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> createSchedule(
            @LoginUser Long userId,
            @RequestBody ScheduleRequestDto request) {
        scheduleService.createSchedule(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("일정 생성 완료"));
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(
            @LoginUser Long userId,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "month") int month,
            @RequestParam(name = "teamSpaceId", required = false) Long teamSpaceId) {

        if (teamSpaceId != null) {
            return ResponseEntity.ok(scheduleService.getTeamSchedulesByMonth(teamSpaceId, year, month));
        }
        return ResponseEntity.ok(scheduleService.getUserSchedulesByMonth(userId, year, month));
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