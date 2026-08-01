package memo.example.demo.repository;

import memo.example.demo.domain.TeamFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamFileRepository extends JpaRepository<TeamFile, Long> {
    List<TeamFile> findByTeamSpace_TeamSpaceId(Long teamSpaceId);
}