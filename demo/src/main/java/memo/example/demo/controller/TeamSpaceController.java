package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/team-spaces")
@RequiredArgsConstructor
public class TeamSpaceController {

    @PostMapping
    public ResponseEntity<?> createTeamSpace(@RequestBody TeamSpaceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new TeamSpaceCreateResponse(1L, request.name()));
    }

    @GetMapping
    public ResponseEntity<?> getMyTeamSpaces() {
        return ResponseEntity.ok(List.of(new TeamSpaceResponse(1L, "Team A", 3)));
    }

    @GetMapping("/{teamSpaceId}")
    public ResponseEntity<?> getTeamSpaceDetail(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(new TeamSpaceResponse(teamSpaceId, "Team A", 3));
    }

    // --- DTOs ---
    public record TeamSpaceCreateRequest(String name) {}
    public record TeamSpaceCreateResponse(Long teamSpaceId, String name) {}
    public record TeamSpaceResponse(Long teamSpaceId, String name, Integer memberCount) {}
}