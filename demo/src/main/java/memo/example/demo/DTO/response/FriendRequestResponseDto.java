package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Friend;
import java.time.LocalDateTime;

@Getter
@Builder
public class FriendRequestResponseDto {
    private Long requestId;
    private String nickname;
    private LocalDateTime createdAt;

    public static FriendRequestResponseDto from(Friend friend, String targetNickname) {
        return FriendRequestResponseDto.builder()
                .requestId(friend.getRequestId())
                .nickname(targetNickname)
                .createdAt(friend.getCreatedAt())
                .build();
    }
}