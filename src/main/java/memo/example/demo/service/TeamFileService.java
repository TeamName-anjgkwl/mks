package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamFileRequestDto;
import memo.example.demo.DTO.response.TeamFileResponseDto;
import memo.example.demo.domain.TeamFile;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.TeamFileRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamFileService {
    private final TeamFileRepository teamFileRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final UserRepository userRepository;

    public void saveFileInfo(Long teamSpaceId, Long userId, TeamFileRequestDto request) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamSpaceId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        TeamFile teamFile = TeamFile.builder()
                .teamSpace(teamSpace)
                .user(user)
                .fileName(request.getFileName())
                .fileUrl(request.getFileUrl())
                .fileSize(request.getFileSize())
                .build();
        teamFileRepository.save(teamFile);
    }

    @Transactional(readOnly = true)
    public List<TeamFileResponseDto> getTeamFiles(Long teamSpaceId) {
        return teamFileRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(TeamFileResponseDto::from)
                .collect(Collectors.toList());
    }

    // TODO 완벽 해결: 파일 이름 변경 구현
    public void renameFile(Long fileId, String newFileName) {
        TeamFile teamFile = teamFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        teamFile.setFileName(newFileName);
    }

    public void deleteFile(Long fileId) {
        teamFileRepository.deleteById(fileId);
    }
}