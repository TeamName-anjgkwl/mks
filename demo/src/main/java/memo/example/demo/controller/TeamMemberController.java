package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamMemberController {

    @PostMapping("/team-spaces/{teamSpaceId}/members")
    public ResponseEntity<?> addTeamMember(
            @PathVariable Long teamSpaceId,
            @RequestBody AddMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberIdResponse(1L));
    }

    @GetMapping("/team-members/team/{teamSpaceId}")
    public ResponseEntity<?> getTeamMembers(@PathVariable Long teamSpaceId) {
        return ResponseEntity.ok(List.of(new TeamMemberResponse(1L, "nickname", "MEMBER")));
    }

    @PatchMapping("/team-members/{memberId}")
    public ResponseEntity<?> changeMemberRole(
            @PathVariable Long memberId,
            @RequestParam(name = "role") String role) {
        return ResponseEntity.ok(List.of(new TeamMemberResponse(1L, "nickname", role)));
    }

    @DeleteMapping("/team-members/{memberId}")
    public ResponseEntity<?> removeMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(List.of(new TeamMemberResponse(1L, "nickname", "MEMBER"))); // 규격상 응답 포맷
    }

    // --- DTOs ---
    public record AddMemberRequest(Long userId, String role) {}
    public record MemberIdResponse(Long teamMemberId) {}
    public record TeamMemberResponse(Long userId, String nickname, String role) {}
}