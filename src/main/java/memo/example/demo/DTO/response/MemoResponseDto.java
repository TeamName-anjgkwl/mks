package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Memo;
import memo.example.demo.domain.MemoImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MemoResponseDto {
    private Long memoId;
    private Long userId;
    private Long teamSpaceId;
    private String status;
    private String title;
    private String content;
    private String richContent;
    private Boolean isPinned;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MemoImageResponseDto> images;

    public static MemoResponseDto from(Memo memo) {
        return from(memo, new ArrayList<>());
    }

    public static MemoResponseDto from(Memo memo, List<MemoImage> images) {
        return MemoResponseDto.builder()
                .memoId(memo.getMemoId())
                .userId(memo.getUser() != null ? memo.getUser().getUserId() : null)
                .teamSpaceId(memo.getTeamSpace() != null ? memo.getTeamSpace().getTeamSpaceId() : null)
                .status(memo.getStatus() != null ? memo.getStatus().name() : null)
                .title(memo.getMTitle())
                .content(memo.getMContent())
                .richContent(memo.getMRichContent())
                .isPinned(memo.getIsPinned())
                .expiredAt(memo.getExpiredAt())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .images(images != null ? images.stream()
                        .map(img -> MemoImageResponseDto.builder()
                                .imageId(img.getImageId())
                                .imageUrl(img.getImageUrl())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}