package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSettingsUpdateRequestDto {
    private String timezone;
    private String dateFormat;
    private String language;
    private Boolean use2fa;
    private Boolean allowPush;
    private Boolean allowEvent;
    private String twoFactorMethod; // 추가됨
}