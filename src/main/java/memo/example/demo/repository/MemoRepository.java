package memo.example.demo.repository;

import memo.example.demo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUser_UserIdAndTeamSpaceIsNull(Long userId); // 개인 메모만
    List<Memo> findByTeamSpace_TeamSpaceId(Long teamSpaceId); // 팀 메모

    // 통합 검색용 (제목 또는 내용 포함)
    @Query("SELECT m FROM Memo m WHERE m.mTitle LIKE %:keyword% OR m.mContent LIKE %:keyword%")
    List<Memo> searchByKeyword(@Param("keyword") String keyword);
}