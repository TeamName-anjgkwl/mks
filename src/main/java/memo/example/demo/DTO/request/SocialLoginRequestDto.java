package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequestDto {
    private String provider; // KAKAO, GOOGLE 등
    private String providerId; // 소셜 고유 식별번호
    private String email;
    private String name;
    private String profileImageUrl;
    private String deviceName;
}