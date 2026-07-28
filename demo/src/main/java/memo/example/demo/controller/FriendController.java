package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    @GetMapping
    public ResponseEntity<?> getFriends(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // List<FriendResponse> friends = friendService.getFriends(type, keyword);
        return ResponseEntity.ok(List.of(new FriendResponse(2L, "friend_nick", "url")));
    }

    @PostMapping("/requests")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest request) {
        return ResponseEntity.ok(new MessageResponse("요청 전송 완료"));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getFriendRequests(@RequestParam(name = "type") String type) {
        // type: SENT / RECEIVED
        return ResponseEntity.ok(List.of(new FriendRequestResponse(1L, "friend_nick", "2026-07-23T12:00:00")));
    }

    @PatchMapping("/requests/{requestId}")
    public ResponseEntity<?> respondToRequest(
            @PathVariable Long requestId,
            @RequestParam(name = "action") String action) { // ACCEPT or REJECT
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<?> cancelRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(new MessageResponse("처리 완료"));
    }

    // --- DTOs ---
    public record FriendResponse(Long userId, String nickname, String profileImageUrl) {}
    public record FriendRequest(Long receiverId) {}
    public record FriendRequestResponse(Long requestId, String nickname, String createdAt) {}
    public record MessageResponse(String message) {}
}