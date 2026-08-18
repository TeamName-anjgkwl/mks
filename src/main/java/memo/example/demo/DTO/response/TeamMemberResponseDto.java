package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.TeamMember;

@Getter
@Builder
public class TeamMemberResponseDto {
    private Long teamMemberId;
    private Long userId;
    private String nickname;
    private String role;

    public static TeamMemberResponseDto from(TeamMember teamMember) {
        return TeamMemberResponseDto.builder()
                .teamMemberId(teamMember.getTeamMemberId())
                .userId(teamMember.getUser() != null ? teamMember.getUser().getUserId() : null)
                .nickname(teamMember.getUser() != null ? teamMember.getUser().getNickname() : null)
                .role(teamMember.getRole() != null ? teamMember.getRole().name() : null)
                .build();
    }
}