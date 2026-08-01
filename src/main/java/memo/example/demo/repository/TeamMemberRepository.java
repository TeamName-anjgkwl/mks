package memo.example.demo.repository;

import memo.example.demo.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeamSpace_TeamSpaceId(Long teamSpaceId);
    List<TeamMember> findByUser_UserId(Long userId);
}