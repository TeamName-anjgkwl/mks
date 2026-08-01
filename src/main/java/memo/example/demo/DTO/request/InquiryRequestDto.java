package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryRequestDto {
    private String type; // ONE_ON_ONE, SUGGESTION
    private String title;
    private String content;
    private String attachmentUrl; // S3 업로드된 이미지 URL (선택적)
}