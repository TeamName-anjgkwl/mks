package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindIdPwRequestDto {
    private String type; // "ID" 또는 "PW"
    private String email;
}