package memo.example.demo.repository;

import memo.example.demo.domain.TeamSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSpaceRepository extends JpaRepository<TeamSpace, Long> {
}