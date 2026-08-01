package memo.example.demo.repository;

import memo.example.demo.domain.TeamNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamNoticeRepository extends JpaRepository<TeamNotice, Long> {
    // 특정 팀 스페이스의 공지사항 목록 조회 (V10 API 명세 매핑)
    List<TeamNotice> findByTeamSpace_TeamSpaceId(Long teamSpaceId);
}