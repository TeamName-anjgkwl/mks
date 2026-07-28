package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.ScheduleController.*;
import memo.example.demo.domain.Schedule;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.ScheduleRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // V10: startAt, endAt 분리
    public void createSchedule(Long userId, ScheduleCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow();

        TeamSpace teamSpace = request.teamSpaceId() != null ?
                teamSpaceRepository.findById(request.teamSpaceId()).orElse(null) : null;

        Schedule schedule = Schedule.builder()
                .user(user)
                .teamSpace(teamSpace)
                .sTitle(request.title())
                .sContent(request.content())
                .startAt(LocalDateTime.parse(request.startAt()))
                .endAt(LocalDateTime.parse(request.endAt()))
                .build();

        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> getTeamSchedules(Long teamSpaceId) {
        return scheduleRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(s -> new ScheduleListResponse(
                        s.getScheduleId(),
                        s.getSTitle(),
                        s.getStartAt().toString(),
                        s.getEndAt().toString()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDetailResponse getScheduleDetail(Long scheduleId) {
        Schedule s = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        return new ScheduleDetailResponse(s.getScheduleId(), s.getSTitle(), s.getSContent(), s.getStartAt().toString(), s.getEndAt().toString());
    }

    public void updateSchedule(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule s = scheduleRepository.findById(scheduleId).orElseThrow();
        if (request.title() != null) s.setSTitle(request.title());
        if (request.content() != null) s.setSContent(request.content());
        if (request.startAt() != null) s.setStartAt(LocalDateTime.parse(request.startAt()));
        if (request.endAt() != null) s.setEndAt(LocalDateTime.parse(request.endAt()));
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}