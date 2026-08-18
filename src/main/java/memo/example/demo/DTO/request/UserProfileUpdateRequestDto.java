package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {
    private String nickname;
    private String profileImageUrl;
    private String name;
    private String email;
    private String phoneNumber;
}