package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoImageResponseDto {
    private Long imageId;
    private String imageUrl;
}
