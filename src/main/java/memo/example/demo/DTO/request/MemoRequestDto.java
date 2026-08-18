package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoRequestDto {
    private Long teamSpaceId;
    private String title;
    private String content;
    private String richContent;
    private String status;
    private String expiredAt;
}