package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.User;

@Getter
@Builder
public class FriendResponseDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;

    public static FriendResponseDto from(User user) {
        return FriendResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}