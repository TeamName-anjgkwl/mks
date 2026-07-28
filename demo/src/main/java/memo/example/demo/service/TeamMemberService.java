package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.TeamMemberController.TeamMemberResponse;
import memo.example.demo.domain.TeamMember;
import memo.example.demo.domain.TeamMember.Role;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.TeamMemberRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final UserRepository userRepository;

    public void addMember(Long teamSpaceId, Long userId, Role role) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        TeamMember teamMember = TeamMember.builder()
                .teamSpace(teamSpace)
                .user(user)
                .role(role)
                .build();
        teamMemberRepository.save(teamMember);
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamSpaceId) {
        return teamMemberRepository.findAll().stream()
                .filter(tm -> tm.getTeamSpace().getTeamSpaceId().equals(teamSpaceId))
                .map(tm -> new TeamMemberResponse(tm.getTeamMemberId(), tm.getUser().getUserId(), tm.getUser().getNickname(), tm.getRole(), tm.getJoinedAt().toString()))
                .collect(Collectors.toList());
    }

    public void changeRole(Long teamMemberId, Role role) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId).orElseThrow();
        teamMember.setRole(role);
    }

    public void removeMember(Long teamMemberId) {
        teamMemberRepository.deleteById(teamMemberId);
    }
}