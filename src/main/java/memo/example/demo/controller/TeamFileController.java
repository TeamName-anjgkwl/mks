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
@RequestMapping("/api/team-files")
@RequiredArgsConstructor
public class TeamFileController {
    private final TeamFileService teamFileService;

    @PostMapping("/team/{teamSpaceId}")
    public ResponseEntity<MessageResponseDto> uploadFileInfo(
            @PathVariable Long teamSpaceId,
            @LoginUser Long userId,
            @RequestBody TeamFileRequestDto request) {
        teamFileService.saveFileInfo(teamSpaceId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("파일이 업로드되었습니다."));
    }

    @GetMapping("/team/{teamSpaceId}")
    public ResponseEntity<?> getTeamFiles(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(teamFileService.getTeamFiles(teamSpaceId));
    }

    @PatchMapping("/{fileId}")
    public ResponseEntity<MessageResponseDto> renameTeamFile(
            @PathVariable Long fileId,
            @RequestBody FileRenameRequestDto request) {
        teamFileService.renameFile(fileId, request.getNewFileName());
        return ResponseEntity.ok(new MessageResponseDto("파일 이름이 성공적으로 변경되었습니다."));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<MessageResponseDto> deleteFile(@PathVariable Long fileId) {
        teamFileService.deleteFile(fileId);
        return ResponseEntity.ok(new MessageResponseDto("파일이 삭제되었습니다."));
    }
}