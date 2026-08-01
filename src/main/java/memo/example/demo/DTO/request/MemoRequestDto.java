package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoRequestDto {
    private Long teamSpaceId; // Null 허용 (개인 메모 시)
    private String title;
    private String content;
    private String status; // NORMAL, FIRE, ICE 등
    private String expiredAt; // FIRE 메모 전용 (예: "2026-07-28T18:00:00")
}