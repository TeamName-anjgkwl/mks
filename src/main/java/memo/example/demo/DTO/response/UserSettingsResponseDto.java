package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.User;

@Getter
@Builder
public class UserSettingsResponseDto {
    private String timezone;
    private String dateFormat;
    private String language;
    private Boolean use2fa;
    private Boolean allowPush;
    private Boolean allowEvent;

    // 추가된 필드
    private String twoFactorMethod;
    private String twoFactorDestination;

    public static UserSettingsResponseDto from(User user, String maskedDestination) {
        return UserSettingsResponseDto.builder()
                .timezone(user.getTimezone())
                .dateFormat(user.getDateFormat())
                .language(user.getLanguage())
                .use2fa(user.getUse2fa())
                .allowPush(user.getAllowPush())
                .allowEvent(user.getAllowEvent())
                .twoFactorMethod(user.getTwoFactorMethod())
                .twoFactorDestination(maskedDestination)
                .build();
    }
}