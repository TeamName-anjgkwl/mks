package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.TeamSpace;
import java.time.LocalDateTime;

@Getter
@Builder
public class TeamSpaceResponseDto {
    private Long teamSpaceId;
    private String name;
    private Integer memberCount; // V10 반영: 참여 멤버 수 표시
    private LocalDateTime createdAt;

    public static TeamSpaceResponseDto from(TeamSpace teamSpace, Integer memberCount) {
        return TeamSpaceResponseDto.builder()
                .teamSpaceId(teamSpace.getTeamSpaceId())
                .name(teamSpace.getName())
                .memberCount(memberCount)
                .createdAt(teamSpace.getCreatedAt())
                .build();
    }
}