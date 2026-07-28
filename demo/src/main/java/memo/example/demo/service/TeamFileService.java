package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.TeamFileController.*;
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

    public void saveFileInfo(TeamFileRequest request) {
        TeamSpace teamSpace = teamSpaceRepository.findById(request.teamSpaceId()).orElseThrow();
        User user = userRepository.findById(request.userId()).orElseThrow();

        TeamFile teamFile = TeamFile.builder()
                .teamSpace(teamSpace)
                .user(user)
                .fileName(request.fileName())
                .fileUrl(request.fileUrl())
                .fileSize(request.fileSize())
                .build();

        teamFileRepository.save(teamFile);
    }

    @Transactional(readOnly = true)
    public List<TeamFileResponse> getTeamFiles(Long teamSpaceId) {
        return teamFileRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(f -> new TeamFileResponse(
                        f.getFileId(),
                        f.getUser().getUserId(),
                        f.getFileName(),
                        f.getFileUrl(),
                        f.getFileSize(),
                        f.getUploadedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    public void deleteFile(Long fileId) {
        teamFileRepository.deleteById(fileId);
    }
}