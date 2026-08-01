package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresignedUrlResponseDto {
    private String presignedUrl;
    private String fileUrl;
}