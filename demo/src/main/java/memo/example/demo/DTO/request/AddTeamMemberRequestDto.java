package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTeamMemberRequestDto {
    private Long userId;
    private String role; // "LEADER", "MEMBER"
}