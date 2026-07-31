package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.ScheduleRequestDto;
import memo.example.demo.DTO.response.ScheduleResponseDto;
import memo.example.demo.domain.Schedule;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.ScheduleRepository; import memo.example.demo.repository.TeamSpaceRepository; import memo.example.demo.repository.UserRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TeamSpaceRepository teamSpaceRepository;

    public void createSchedule(Long userId, ScheduleRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        TeamSpace teamSpace = request.getTeamSpaceId() != null ?
                teamSpaceRepository.findById(request.getTeamSpaceId()).orElse(null) : null;
        Schedule schedule = Schedule.builder()
                .user(user)
                .teamSpace(teamSpace)
                .sTitle(request.getTitle())
                .sContent(request.getContent())
                .startAt(parseDateTimeSafe(request.getStartAt())) // ★ 안전한 파싱 적용
                .endAt(parseDateTimeSafe(request.getEndAt()))     // ★ 안전한 파싱 적용
                .build();
        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getTeamSchedulesByMonth(Long teamSpaceId, int year, int month) {
        return scheduleRepository.findByTeamSpaceAndMonth(teamSpaceId, year, month).stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getUserSchedulesByMonth(Long userId, int year, int month) {
        return scheduleRepository.findByUserAndMonth(userId, year, month).stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleDetail(Long scheduleId) {
        Schedule s = scheduleRepository.findById(scheduleId).orElseThrow();
        return ScheduleResponseDto.from(s);
    }

    public void updateSchedule(Long scheduleId, ScheduleRequestDto request) {
        Schedule s = scheduleRepository.findById(scheduleId).orElseThrow();
        if (request.getTitle() != null) s.setSTitle(request.getTitle());
        if (request.getContent() != null) s.setSContent(request.getContent());
        if (request.getStartAt() != null) s.setStartAt(parseDateTimeSafe(request.getStartAt()));
        if (request.getEndAt() != null) s.setEndAt(parseDateTimeSafe(request.getEndAt()));
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    // ★ 프론트엔드 ISO-8601 방어용 파싱 유틸 메서드
    private LocalDateTime parseDateTimeSafe(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
        String cleaned = dateTimeStr.length() >= 19 ? dateTimeStr.substring(0, 19) : dateTimeStr;
        return LocalDateTime.parse(cleaned);
    }
}