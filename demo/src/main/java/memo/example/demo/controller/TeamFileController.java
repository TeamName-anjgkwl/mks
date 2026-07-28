package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.service.TeamFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-files")
@RequiredArgsConstructor
public class TeamFileController {

    private final TeamFileService teamFileService;

    // 1. 팀 파일 정보 등록 (실제 파일 업로드 후 S3 URL 등을 DB에 저장할 때 사용)
    @PostMapping
    public ResponseEntity<Void> uploadFileInfo(@RequestBody TeamFileRequest request) {
        teamFileService.saveFileInfo(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 특정 팀 스페이스의 공유 파일 목록 조회
    @GetMapping("/team/{teamSpaceId}")
    public ResponseEntity<List<TeamFileResponse>> getTeamFiles(@PathVariable Long teamSpaceId) {
        List<TeamFileResponse> files = teamFileService.getTeamFiles(teamSpaceId);
        return ResponseEntity.ok(files);
    }

    // 3. 팀 파일 정보 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        teamFileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    // --- DTO ---
    public record TeamFileRequest(Long teamSpaceId, Long userId, String fileName, String fileUrl, String fileSize) {}
    public record TeamFileResponse(Long fileId, Long userId, String fileName, String fileUrl, String fileSize, String uploadedAt) {}
}