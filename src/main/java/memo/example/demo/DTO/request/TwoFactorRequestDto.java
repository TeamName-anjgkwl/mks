package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TwoFactorRequestDto {
    private String action; // "SETUP", "SEND", "VERIFY"
    private String method; // "PHONE", "EMAIL"
    private String phoneNumber;
    private String email;
    private String code;
}