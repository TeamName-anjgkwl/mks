package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamFileRequestDto {
    private String fileName;
    private String fileUrl;
    private String fileSize;
}