package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamSpaceCreateRequestDto;
import memo.example.demo.DTO.response.TeamSpaceResponseDto;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.repository.TeamMemberRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamSpaceService {
    private final TeamSpaceRepository teamSpaceRepository;
    private final TeamMemberRepository teamMemberRepository;

    public void createTeamSpace(TeamSpaceCreateRequestDto request) {
        TeamSpace teamSpace = TeamSpace.builder()
                .name(request.getName())
                .build();
        teamSpaceRepository.save(teamSpace);
    }

    @Transactional(readOnly = true)
    public TeamSpaceResponseDto getTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("팀 공간을 찾을 수 없습니다."));

        // 해당 팀스페이스의 멤버 수 카운트
        Integer memberCount = teamMemberRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).size();
        return TeamSpaceResponseDto.from(teamSpace, memberCount);
    }

    public void deleteTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("팀 공간을 찾을 수 없습니다."));
        teamSpace.setDeletedAt(LocalDateTime.now());
    }
}