package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamSpaceCreateRequestDto;
import memo.example.demo.DTO.response.TeamSpaceResponseDto;
import memo.example.demo.domain.TeamMember;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.TeamMemberRepository;
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
public class TeamSpaceService {
    private final TeamSpaceRepository teamSpaceRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public Long createTeamSpace(Long userId, TeamSpaceCreateRequestDto request) {
        TeamSpace teamSpace = TeamSpace.builder()
                .name(request.getName())
                .build();
        teamSpace = teamSpaceRepository.save(teamSpace);

        User user = userRepository.findById(userId).orElseThrow();
        TeamMember teamMember = TeamMember.builder()
                .teamSpace(teamSpace)
                .user(user)
                .role(TeamMember.Role.LEADER)
                .build();
        teamMemberRepository.save(teamMember);

        return teamSpace.getTeamSpaceId();
    }

    @Transactional(readOnly = true)
    public List<TeamSpaceResponseDto> getMyTeamSpaces(Long userId) {
        return teamMemberRepository.findByUser_UserId(userId).stream()
                .map(tm -> TeamSpaceResponseDto.from(tm.getTeamSpace(), teamMemberRepository.findByTeamSpace_TeamSpaceId(tm.getTeamSpace().getTeamSpaceId()).size()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamSpaceResponseDto getTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("팀 스페이스를 찾을 수 없습니다."));
        Integer memberCount = teamMemberRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).size();
        return TeamSpaceResponseDto.from(teamSpace, memberCount);
    }

    public void deleteTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("팀 스페이스를 찾을 수 없습니다."));
        teamSpace.setDeletedAt(LocalDateTime.now());
    }
}