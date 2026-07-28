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
    private LocalDateTime deletedAt;

    public static TrashResponseDto from(Memo memo) {
        return TrashResponseDto.builder()
                .memoId(memo.getMemoId())
                .title(memo.getMTitle())
                .deletedAt(memo.getDeletedAt())
                .build();
    }
}