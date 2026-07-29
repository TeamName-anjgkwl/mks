package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.ScheduleRequestDto;
import memo.example.demo.DTO.response.ScheduleResponseDto;
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

    public void createSchedule(Long userId, ScheduleRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        TeamSpace teamSpace = request.getTeamSpaceId() != null ?
                teamSpaceRepository.findById(request.getTeamSpaceId()).orElse(null) : null;

        Schedule schedule = Schedule.builder()
                .user(user)
                .teamSpace(teamSpace)
                .sTitle(request.getTitle())
                .sContent(request.getContent())
                .startAt(LocalDateTime.parse(request.getStartAt()))
                .endAt(LocalDateTime.parse(request.getEndAt()))
                .build();
        scheduleRepository.save(schedule);
    }

    // 월별 팀 스케줄 조회
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getTeamSchedulesByMonth(Long teamSpaceId, int year, int month) {
        return scheduleRepository.findByTeamSpaceAndMonth(teamSpaceId, year, month).stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
    }

    // 월별 개인 스케줄 조회
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
        if (request.getStartAt() != null) s.setStartAt(LocalDateTime.parse(request.getStartAt()));
        if (request.getEndAt() != null) s.setEndAt(LocalDateTime.parse(request.getEndAt()));
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}