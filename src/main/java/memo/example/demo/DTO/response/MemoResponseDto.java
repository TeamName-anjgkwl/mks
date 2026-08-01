package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Memo;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemoResponseDto {
    private Long memoId;
    private Long userId;
    private Long teamSpaceId;
    private String status;
    private String title;
    private String content;
    private Boolean isPinned;
    private LocalDateTime expiredAt; // V10 반영: 불 메모 만료 일시
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemoResponseDto from(Memo memo) {
        return MemoResponseDto.builder()
                .memoId(memo.getMemoId())
                .userId(memo.getUser() != null ? memo.getUser().getUserId() : null)
                .teamSpaceId(memo.getTeamSpace() != null ? memo.getTeamSpace().getTeamSpaceId() : null)
                .status(memo.getStatus() != null ? memo.getStatus().name() : null)
                .title(memo.getMTitle())
                .content(memo.getMContent())
                .isPinned(memo.getIsPinned())
                .expiredAt(memo.getExpiredAt())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}