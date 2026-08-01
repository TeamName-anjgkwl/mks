package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamNoticeRequestDto {
    private String title;
    private String content;
    private Boolean isPinned;
}