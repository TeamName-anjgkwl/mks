package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamNoticeRequestDto;
import memo.example.demo.DTO.response.TeamNoticeResponseDto;
import memo.example.demo.domain.TeamNotice;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.TeamNoticeRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamNoticeService {

    private final TeamNoticeRepository teamNoticeRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final UserRepository userRepository;

    public void createNotice(Long teamSpaceId, Long userId, TeamNoticeRequestDto request) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        TeamNotice notice = TeamNotice.builder()
                .teamSpace(teamSpace)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .isPinned(request.getIsPinned() != null ? request.getIsPinned() : false)
                .build();

        teamNoticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public List<TeamNoticeResponseDto> getNotices(Long teamSpaceId) {
        return teamNoticeRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(TeamNoticeResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamNoticeResponseDto getNoticeDetail(Long noticeId) {
        TeamNotice notice = teamNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지를 찾을 수 없습니다."));
        return TeamNoticeResponseDto.from(notice);
    }

    public void updateNotice(Long noticeId, TeamNoticeRequestDto request) {
        TeamNotice notice = teamNoticeRepository.findById(noticeId).orElseThrow();
        if (request.getTitle() != null) notice.setTitle(request.getTitle());
        if (request.getContent() != null) notice.setContent(request.getContent());
        if (request.getIsPinned() != null) notice.setIsPinned(request.getIsPinned());
    }

    public void deleteNotice(Long noticeId) {
        teamNoticeRepository.deleteById(noticeId);
    }
}