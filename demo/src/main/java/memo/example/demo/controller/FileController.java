package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    @GetMapping("/files/presigned-url")
    public ResponseEntity<?> getPresignedUrl(
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "fileType") String fileType,
            @RequestParam(name = "domain") String domain) {
        return ResponseEntity.ok(new PresignedUrlResponse("https://s3-put-url...", "https://s3-final-url..."));
    }

    @PostMapping("/memos/{memoId}/images")
    public ResponseEntity<?> saveMemoImage(
            @PathVariable Long memoId,
            @RequestBody MemoImageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ImageIdResponse(1L));
    }

    @PostMapping("/team-spaces/{teamSpaceId}/files")
    public ResponseEntity<?> saveTeamFile(
            @PathVariable Long teamSpaceId,
            @RequestBody TeamFileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new FileIdResponse(1L));
    }

    @GetMapping("/team-spaces/{teamSpaceId}/files")
    public ResponseEntity<?> getTeamFiles(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new TeamFileResponse(1L, "file.pdf", "https://s3...")));
    }

    @PatchMapping("/team-files/{fileId}")
    public ResponseEntity<?> renameTeamFile(
            @PathVariable Long fileId,
            @RequestBody FileRenameRequest request) {
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/team-files/{fileId}")
    public ResponseEntity<?> deleteTeamFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    public record PresignedUrlResponse(String presignedUrl, String fileUrl) {}
    public record MemoImageRequest(String imageUrl) {}
    public record ImageIdResponse(Long imageId) {}
    public record TeamFileRequest(String fileName, String fileUrl, String fileSize) {}
    public record FileIdResponse(Long fileId) {}
    public record TeamFileResponse(Long fileId, String fileName, String fileUrl) {}
    public record FileRenameRequest(String newFileName) {}
    public record MessageResponse(String message) {}
}