package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.FriendRequestDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import memo.example.demo.config.jwt.LoginUser;
import memo.example.demo.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    public ResponseEntity<?> getFriends(@LoginUser Long userId) {
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    @PostMapping("/requests")
    public ResponseEntity<MessageResponseDto> sendFriendRequest(
            @LoginUser Long userId,
            @RequestBody FriendRequestDto request) {
        friendService.sendFriendRequest(userId, request.getReceiverId());
        return ResponseEntity.ok(new MessageResponseDto("친구 요청이 전송되었습니다."));
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<?> getPendingRequests(@LoginUser Long userId) {
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
        return ResponseEntity.ok(new MessageResponseDto("친구 요청이 처리되었습니다."));
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<MessageResponseDto> cancelRequest(@PathVariable Long requestId) {
        friendService.cancelRequest(requestId);
        return ResponseEntity.ok(new MessageResponseDto("친구 요청이 취소/삭제되었습니다."));
    }
}