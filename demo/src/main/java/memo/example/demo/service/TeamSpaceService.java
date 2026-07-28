package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.TeamSpaceController.TeamSpaceResponse;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.repository.TeamSpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamSpaceService {

    private final TeamSpaceRepository teamSpaceRepository;

    public void createTeamSpace(String name) {
        TeamSpace teamSpace = TeamSpace.builder()
                .name(name)
                .build();
        teamSpaceRepository.save(teamSpace);
    }

    @Transactional(readOnly = true)
    public TeamSpaceResponse getTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀 스페이스입니다."));
        return new TeamSpaceResponse(teamSpace.getTeamSpaceId(), teamSpace.getName(), teamSpace.getCreatedAt().toString());
    }

    public void deleteTeamSpace(Long teamSpaceId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀 스페이스입니다."));
        teamSpace.setDeletedAt(LocalDateTime.now()); // Soft Delete
    }
}