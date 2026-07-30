package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.FriendRequestResponseDto; // 정식 DTO로 임포트 변경!
import memo.example.demo.domain.Friend;
import memo.example.demo.domain.Friend.FriendStatus;
import memo.example.demo.domain.User;
import memo.example.demo.repository.FriendRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        Friend friend = Friend.builder()
                .requester(requester)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();
        friendRepository.save(friend);
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponseDto> getPendingRequests(Long userId) {
        return friendRepository.findAll().stream()
                .filter(f -> f.getReceiver().getUserId().equals(userId) && f.getStatus() == FriendStatus.PENDING)
                .map(f -> FriendRequestResponseDto.from(f, f.getRequester().getNickname()))
                .collect(Collectors.toList());
    }

    public void acceptRequest(Long requestId) {
        Friend friend = friendRepository.findById(requestId).orElseThrow();
        friend.setStatus(FriendStatus.ACCEPTED);
    }

    public void rejectRequest(Long requestId) {
        Friend friend = friendRepository.findById(requestId).orElseThrow();
        friend.setStatus(FriendStatus.REJECTED);
    }
}