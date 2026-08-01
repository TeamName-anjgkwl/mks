package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.TeamSpaceCreateRequestDto;
import memo.example.demo.service.TeamSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/team-spaces")
@RequiredArgsConstructor
public class TeamSpaceController {
    private final TeamSpaceService teamSpaceService;

    @PostMapping
    public ResponseEntity<?> createTeamSpace(@RequestBody TeamSpaceCreateRequestDto request) {
        teamSpaceService.createTeamSpace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "생성 완료"));
    }

    @GetMapping
    public ResponseEntity<?> getMyTeamSpaces() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teamSpaceId}")
    public ResponseEntity<?> getTeamSpaceDetail(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(teamSpaceService.getTeamSpace(teamSpaceId));
    }
}