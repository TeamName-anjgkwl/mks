package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.FileRenameRequestDto;
import memo.example.demo.DTO.request.TeamFileRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.service.TeamFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // 기존 /api/team-files 에서 공통 경로 /api 로 변경
@RequiredArgsConstructor
public class TeamFileController {

    private final TeamFileService teamFileService;

    // 팀스페이스 파일 정보 저장
    @PostMapping("/team-spaces/{teamSpaceId}/files")
    public ResponseEntity<MessageResponseDto> uploadFileInfo(
            @PathVariable Long teamSpaceId,
            @LoginUser Long userId,
            @RequestBody TeamFileRequestDto request) {
        teamFileService.saveFileInfo(teamSpaceId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("파일 정보가 저장되었습니다."));
    }

    // 팀스페이스 파일 목록 조회 (프론트엔드 요청 URL에 완벽히 매핑됨)
    @GetMapping("/team-spaces/{teamSpaceId}/files")
    public ResponseEntity<?> getTeamFiles(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(teamFileService.getTeamFiles(teamSpaceId));
    }

    // 파일 이름 변경
    @PatchMapping("/team-files/{fileId}")
    public ResponseEntity<MessageResponseDto> renameTeamFile(
            @PathVariable Long fileId,
            @RequestBody FileRenameRequestDto request) {
        teamFileService.renameFile(fileId, request.getNewFileName());
        return ResponseEntity.ok(new MessageResponseDto("파일 이름이 변경되었습니다."));
    }

    // 파일 삭제
    @DeleteMapping("/team-files/{fileId}")
    public ResponseEntity<MessageResponseDto> deleteFile(@PathVariable Long fileId) {
        teamFileService.deleteFile(fileId);
        return ResponseEntity.ok(new MessageResponseDto("파일이 삭제되었습니다."));
    }
}