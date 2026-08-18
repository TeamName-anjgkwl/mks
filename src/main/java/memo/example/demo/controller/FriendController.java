package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.FriendRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.service.FriendService;
import memo.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getFriends(
            @LoginUser Long userId,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "keyword", required = false) String keyword) {
        if ("SEARCH".equalsIgnoreCase(type) || "RECOMMEND".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(userService.searchUsersByKeyword(keyword));
        }
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    @PostMapping("/requests")
    public ResponseEntity<?> sendFriendRequest(
            @LoginUser Long userId,
            @RequestBody FriendRequestDto request) {
        Long requestId = friendService.sendFriendRequest(userId, request.getReceiverId());
        return ResponseEntity.ok(Map.of(
                "requestId", requestId,
                "message", "친구 요청이 전송되었습니다."
        ));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getRequests(@LoginUser Long userId, @RequestParam(name = "type", defaultValue = "RECEIVED") String type) {
        if ("SENT".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(friendService.getSentRequests(userId));
        }
        return ResponseEntity.ok(friendService.getPendingRequests(userId));
    }

    @PatchMapping("/requests/{requestId}")
    public ResponseEntity<MessageResponseDto> respondToRequest(
            @PathVariable Long requestId,
            @RequestParam(name = "action") String action) {
        if ("ACCEPT".equalsIgnoreCase(action)) {
            friendService.acceptRequest(requestId);
        } else if ("REJECT".equalsIgnoreCase(action)) {
            friendService.rejectRequest(requestId);
        }
        return ResponseEntity.ok(new MessageResponseDto("처리 완료되었습니다."));
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<MessageResponseDto> cancelRequest(@PathVariable Long requestId) {
        friendService.cancelRequest(requestId);
        return ResponseEntity.ok(new MessageResponseDto("처리 완료되었습니다."));
    }
}