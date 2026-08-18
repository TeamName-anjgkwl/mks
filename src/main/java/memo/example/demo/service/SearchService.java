package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.*;
import memo.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final MemoRepository memoRepository;
    private final TeamTodoRepository teamTodoRepository;
    private final ScheduleRepository scheduleRepository;

    public SearchResponseDto globalSearch(String keyword) {
        List<MemoResponseDto> memos = memoRepository.searchByKeyword(keyword).stream()
                .map(MemoResponseDto::from).collect(Collectors.toList());
        List<TeamTodoResponseDto> todos = teamTodoRepository.searchByKeyword(keyword).stream()
                .map(TeamTodoResponseDto::from).collect(Collectors.toList());
        List<ScheduleResponseDto> schedules = scheduleRepository.searchByKeyword(keyword).stream()
                .map(ScheduleResponseDto::from).collect(Collectors.toList());

        return SearchResponseDto.builder()
                .memos(memos)
                .notices(List.of())
                .todos(todos)
                .schedules(schedules)
                .build();
    }
}