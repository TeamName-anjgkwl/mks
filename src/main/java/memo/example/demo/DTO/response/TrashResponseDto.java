package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Memo;
import java.time.LocalDateTime;

@Getter
@Builder
public class TrashResponseDto {
    private Long memoId;
    private String title;
    private String status;
    private LocalDateTime deletedAt;

    public static TrashResponseDto from(Memo memo) {
        return TrashResponseDto.builder()
                .memoId(memo.getMemoId())
                .title(memo.getMTitle())
                .status(memo.getStatus() != null ? memo.getStatus().name() : null)
                .deletedAt(memo.getDeletedAt())
                .build();
    }
}